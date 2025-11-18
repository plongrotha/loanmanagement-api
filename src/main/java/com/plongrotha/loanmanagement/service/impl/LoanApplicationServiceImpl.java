package com.plongrotha.loanmanagement.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;
import com.plongrotha.loanmanagement.exception.ConflictException;
import com.plongrotha.loanmanagement.exception.NotFoundException;
import com.plongrotha.loanmanagement.model.Application;
import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.model.enums.ApplicationStatus;
import com.plongrotha.loanmanagement.model.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.model.enums.LoanRefundStatus;
import com.plongrotha.loanmanagement.model.enums.LoanType;
import com.plongrotha.loanmanagement.repository.ApplicationRepository;
import com.plongrotha.loanmanagement.repository.LoanApplicationRepository;
import com.plongrotha.loanmanagement.service.LoanApplicationService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

	private final LoanApplicationRepository loanApplicationRepository;
	private final ApplicationRepository applicationRepository;
	private final Faker faker = new Faker();

	@Override
	public List<LoanType> getAllLoanTypes() {
		List<LoanType> loanTypes = List.of(LoanType.values());
		if (loanTypes.isEmpty())
			throw new NotFoundException("No loan types found");
		return loanTypes;
	}

	@Override
	public List<EmploymentStatus> getAllEmploymentStatuses() {
		List<EmploymentStatus> employmentStatuses = List.of(EmploymentStatus.values());
		if (employmentStatuses.isEmpty())
			throw new NotFoundException("No employment statuses found");
		return employmentStatuses;
	}

	@Override
	public List<LoanApplication> getAllLoanApplications() {
		List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
		if (loanApplications.isEmpty())
			throw new NotFoundException("No loan applications found");
		return loanApplications;
	}

	// create loan application v1
	@Override
	public LoanApplication createNewLoanApplication(LoanApplication loanApplication) {
		Application incomingApp = loanApplication.getApplication();
		if (incomingApp == null)
			throw new IllegalArgumentException("Application (applicant info) cannot be null");
		// Create new Application entity from incoming data
		Application newApplication = new Application();
		newApplication.setAddress(incomingApp.getAddress());
		newApplication.setApplicantFullName(incomingApp.getApplicantFullName());
		newApplication.setNationalId(incomingApp.getNationalId());
		newApplication.setPhoneNumber(incomingApp.getPhoneNumber());
		newApplication.setEmail(incomingApp.getEmail());
		applicationRepository.save(newApplication);

		// Create LoanApplication and attach the saved Application
		LoanApplication newLoan = new LoanApplication();
		newLoan.setApplication(newApplication); // ✅ important: attach saved application
		newLoan.setLoanType(loanApplication.getLoanType());
		newLoan.setEmploymentStatus(loanApplication.getEmploymentStatus());
		newLoan.setLoanAmount(loanApplication.getLoanAmount());
		newLoan.setLoanDurationInMonths(loanApplication.getLoanDurationInMonths());
		newLoan.setLoanPurpose(loanApplication.getLoanPurpose());
		return loanApplicationRepository.save(newLoan);
	}

	@Override
	public List<LoanApplication> getAllLoanApplicationsByStatus(ApplicationStatus status) {
		List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
		if (loanApplications.isEmpty()) {
			throw new NotFoundException("No loan applications found");
		}
		return loanApplications.stream().filter(application -> application.getApplicationStatus() == status).toList();
	}

	@Override
	public List<LoanApplication> getAllLoanApplicationsByEmploymentStatus(EmploymentStatus employmentStatus) {
		List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
		if (loanApplications.isEmpty()) {
			throw new NotFoundException("No loan applications found");
		}
		return loanApplications.stream().filter(application -> application.getEmploymentStatus() == employmentStatus)
				.toList();
	}

	@Override
	public List<LoanApplication> getAllLoanApplicationsByLoanType(LoanType loanType) {
		List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
		if (loanApplications.isEmpty()) {
			throw new NotFoundException("No loan applications found");
		}
		return loanApplications.stream().filter(application -> application.getLoanType() == loanType).toList();
	}

	// update loanApplication status
	@Override
	public LoanApplication updateLoanApplicationStatus(Long applicationId, ApplicationStatus newStatus) {
		LoanApplication application = loanApplicationRepository.findById(applicationId)
				.orElseThrow(() -> new NotFoundException("Loan application with ID " + applicationId + " not found"));
		if (application.getApplicationStatus() == ApplicationStatus.PENDING) {
			application.setApplicationStatus(newStatus);
		}
		return loanApplicationRepository.save(application);
	}

	// approve loanApplication when have have request for loaning
	@Override
	public void approveLoanApplication(Long applicationId) {
		// find the loanApplication is have in the database
		LoanApplication application = loanApplicationRepository.findById(applicationId)
				.orElseThrow(() -> new NotFoundException("Loan application with ID " + applicationId + " not found"));
		if (application.getApplicationStatus() == ApplicationStatus.PENDING) {
			application.setApplicationStatus(ApplicationStatus.APPROVED);
			application.setLoanRefundStatus(LoanRefundStatus.IN_PROGRESS);
			application.setUpdatedAt(LocalDateTime.now());
			loanApplicationRepository.save(application);
		}
	}

	// get all the value from the enumeration
	@Override
	public List<LoanApplication> getAllApplicatonStatusPending() {
		return loanApplicationRepository.findAll().stream()
				.filter(application -> application.getApplicationStatus() == ApplicationStatus.PENDING).toList();
	}

	// get loan application by id
	@Override
	public LoanApplication getLoanApplicationById(Long applicationId) {
		return loanApplicationRepository.findById(applicationId)
				.orElseThrow(() -> new NotFoundException("Loan application with ID " + applicationId + " not found"));
	}

	// get all the the laonApplication
	@Override
	public List<ApplicationStatus> getAllApplicationStatuses() {
		List<ApplicationStatus> applicationStatuses = List.of(ApplicationStatus.values());
		if (applicationStatuses.isEmpty()) {
			throw new NotFoundException("No application statuses found");
		}
		return applicationStatuses;
	}

	// reject the loan application if the owner don't want to give the loaing
	@Override
	public void rejectLoanApplication(Long applicationId) {
		LoanApplication application = loanApplicationRepository.findById(applicationId)
				.orElseThrow(() -> new NotFoundException("Loan application with ID " + applicationId + " not found"));
		if (application.getApplicationStatus() == ApplicationStatus.PENDING) {
			application.setApplicationStatus(ApplicationStatus.REJECTED);
			application.setUpdatedAt(LocalDateTime.now());
			loanApplicationRepository.save(application);
		} else if (application.getApplicationStatus() == ApplicationStatus.APPROVED) {
			throw new ConflictException("this loanApplicatoin is approved.");
		} else if (application.getApplicationStatus() == ApplicationStatus.REJECTED) {
			throw new ConflictException("this loanApplicatoin is rejected.");
		}
	}

	@Override
	public void deleteLoanApplicationStatusRejected(Long applicationId) {
		LoanApplication application = loanApplicationRepository.findById(applicationId)
				.orElseThrow(() -> new NotFoundException("Loan application with ID " + applicationId + " not found"));
		// if(application.getApplicationStatus() == ApplicationStatus.REJECTED) {
		// loanApplicationRepository.delete(application);
		// }
		// update from delete if application status Rejected now not like those
		loanApplicationRepository.delete(application);
	}

	// create v2 by this method all the request are in the body
	@Override
	public LoanApplication createNewLoanApplicationV2(LoanApplication loanApplication) {
		Application incomingApp = loanApplication.getApplication();
		if (incomingApp == null) {
			throw new IllegalArgumentException("Application (applicant info) cannot be null");
		}

		// Create new Application entity from incoming data
		Application newApplication = new Application();
		newApplication.setAddress(incomingApp.getAddress());
		newApplication.setApplicantFullName(incomingApp.getApplicantFullName());
		newApplication.setNationalId(incomingApp.getNationalId());
		newApplication.setPhoneNumber(incomingApp.getPhoneNumber());
		newApplication.setEmail(incomingApp.getEmail());

		applicationRepository.save(newApplication);

		// Create LoanApplication and attach the saved Application
		LoanApplication newLoan = new LoanApplication();
		newLoan.setApplication(newApplication); // ✅ important: attach saved application
		newLoan.setLoanType(loanApplication.getLoanType());
		newLoan.setEmploymentStatus(loanApplication.getEmploymentStatus());
		newLoan.setLoanAmount(loanApplication.getLoanAmount());
		newLoan.setLoanDurationInMonths(loanApplication.getLoanDurationInMonths());
		newLoan.setLoanPurpose(loanApplication.getLoanPurpose());
		return loanApplicationRepository.save(newLoan);
	}

	public void generateFakeLoanApplications() {
		for (int i = 0; i < 1000; i++) {
			Application app = new Application();
			app.setApplicantFullName(faker.name().fullName());
			app.setAddress(faker.address().fullAddress());
			app.setEmail(faker.internet().emailAddress());
			app.setPhoneNumber(faker.phoneNumber().cellPhone());
			app.setNationalId("NID-" + faker.number().digits(8));
			app.setCreatedAt(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 365)));

			// SAVE FIRST
			Application saved = applicationRepository.save(app);

			// // RELOAD to attach as MANAGED ENTITY
			Application managedApp = applicationRepository.findById(saved.getApplicationId())
					.orElseThrow();

			LoanApplication loanApp = new LoanApplication();

			loanApp.setApplication(managedApp); // IMPORTANT FIX
			loanApp.setLoanType(randomLoanType());
			loanApp.setEmploymentStatus(randomEmploymentStatus());
			loanApp.setApplicationStatus(ApplicationStatus.PENDING);
			loanApp.setLoanAmount(new BigDecimal(faker.number().numberBetween(100,
					50000)));
			loanApp.setPaidAmount(BigDecimal.ZERO);
			loanApp.setLoanDurationInMonths(faker.number().numberBetween(3, 36));
			loanApp.setLoanPurpose(faker.lorem().sentence(5));
			loanApp.setInterestRate(faker.number().randomDouble(2, 1, 10));
			loanApp.setCreatedAt(LocalDateTime.now());
			loanApp.setUpdatedAt(LocalDateTime.now());

			loanApplicationRepository.save(loanApp);
		}
	}

	private LoanType randomLoanType() {
		LoanType[] types = LoanType.values();
		return types[faker.number().numberBetween(0, types.length)];
	}

	private EmploymentStatus randomEmploymentStatus() {
		EmploymentStatus[] types = EmploymentStatus.values();
		return types[faker.number().numberBetween(0, types.length)];
	}
}
