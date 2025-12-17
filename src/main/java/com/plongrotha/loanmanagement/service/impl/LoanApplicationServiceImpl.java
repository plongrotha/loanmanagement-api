package com.plongrotha.loanmanagement.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.plaf.ColorUIResource;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;
import com.plongrotha.loanmanagement.dto.response.PaginationDTO;
import com.plongrotha.loanmanagement.enums.ApplicationStatus;
import com.plongrotha.loanmanagement.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.enums.LoanRefundStatus;
import com.plongrotha.loanmanagement.enums.LoanType;
import com.plongrotha.loanmanagement.exception.BadRequestException;
import com.plongrotha.loanmanagement.exception.ConflictException;
import com.plongrotha.loanmanagement.exception.NotFoundException;
import com.plongrotha.loanmanagement.model.Application;
import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.repository.ApplicationRepository;
import com.plongrotha.loanmanagement.repository.LoanApplicationRepository;
import com.plongrotha.loanmanagement.service.LoanApplicationService;
import com.plongrotha.loanmanagement.utils.ColorUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

	public static final Logger logger = Logger.getLogger(LoanApplicationServiceImpl.class.getName());
	private final LoanApplicationRepository loanApplicationRepository;
	private final ApplicationRepository applicationRepository;
	private final Faker faker = new Faker();

	@Override
	public List<LoanType> getAllLoanTypes() {
		List<LoanType> loanTypes = List.of(LoanType.values());
		if (loanTypes.isEmpty()) {
			return List.of();
		}
		logger.info(ColorUtil.BOLD_GREEN + "This is AllLoanTypes : " + ColorUtil.RESET + loanTypes);
		return loanTypes;
	}

	@Override
	public List<EmploymentStatus> getAllEmploymentStatuses() {
		List<EmploymentStatus> employmentStatuses = List.of(EmploymentStatus.values());
		if (employmentStatuses.isEmpty()) {
			return List.of();
		}
		return employmentStatuses;
	}

	@Override
	public List<LoanApplication> getAllLoanApplications() {
		List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
		if (loanApplications.isEmpty()) {
			return List.of();
		}
		return loanApplications;
	}

	@Override
	public LoanApplication getLoanApplicationByNationalID(String nationalId) {
		LoanApplication loanApplication = loanApplicationRepository.findByNationalID(nationalId);
		if (loanApplication == null) {
			logger.info(ColorUtil.BOLD_RED + "Loan application with National ID {} not found : " + ColorUtil.RESET
					+ nationalId);
			throw new NotFoundException("Loan application with National ID " + nationalId + " not found");
		}
		return loanApplication;
	}

	// create loan application v1
	@Override
	public LoanApplication createNewLoanApplication(LoanApplication loanApplication) {
		Application incomingApp = loanApplication.getApplication();
		if (incomingApp == null) {
			logger.info(ColorUtil.BOLD_RED + "Application (applicant info) cannot be null" + ColorUtil.RESET);
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
		newLoan.setApplication(newApplication);
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
			return List.of();
		}
		return loanApplications.stream().filter(application -> application.getApplicationStatus() == status).toList();
	}

	@Override
	public List<LoanApplication> getAllLoanApplicationsByEmploymentStatus(EmploymentStatus employmentStatus) {
		List<LoanApplication> loanApplications = loanApplicationRepository.findByEmploymentStatus(employmentStatus);
		if (loanApplications.isEmpty()) {
			return List.of();
		}
		return loanApplications;
	}

	@Override
	public List<LoanApplication> getAllLoanApplicationsByLoanType(LoanType loanType) {
		List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
		if (loanApplications.isEmpty()) {
			return List.of();
		}
		return loanApplications.stream().filter(application -> application.getLoanType() == loanType).toList();
	}

	// approve loanApplication when have have request for loaning
	@Override
	public void approveLoanApplication(Long applicationId) {
		// find the loanApplication is have in the database
		LoanApplication application = loanApplicationRepository.findById(applicationId)
				.orElseThrow(() -> new NotFoundException("Loan application with ID " + applicationId + " not found"));
		if (application.getApplicationStatus() == ApplicationStatus.APPROVED) {
			throw new ConflictException("This application is approve already");
		}
		if (application.getApplicationStatus() == ApplicationStatus.PENDING) {
			application.setApplicationStatus(ApplicationStatus.APPROVED);
			application.setLoanRefundStatus(LoanRefundStatus.IN_PROGRESS);
			application.setUpdatedAt(LocalDateTime.now());
			loanApplicationRepository.save(application);
		}
	}

	// get all the value from the enumeration
	@Override
	public List<LoanApplication> getAllApplicationStatusPending() {
		List<LoanApplication> loanApplications = loanApplicationRepository
				.findAllByApplicationStatus(ApplicationStatus.PENDING);
		if (loanApplications.isEmpty()) {
			return List.of();
		}
		return loanApplications;
	}

	// get loan application by id
	@Override
	public LoanApplication getLoanApplicationById(Long loanApplicationId) {
		return loanApplicationRepository.findById(loanApplicationId)
				.orElseThrow(() -> new NotFoundException(
						"Loan application with ID " + loanApplicationId + " not found"));
	}

	// get all the the ApplicationStatus enumeration value
	@Override
	public List<ApplicationStatus> getAllApplicationStatuses() {
		List<ApplicationStatus> applicationStatuses = List.of(ApplicationStatus.values());
		if (applicationStatuses.isEmpty()) {
			return List.of();
		}
		return applicationStatuses;
	}

	// reject the loan application if the owner don't want to give the loan
	@Override
	public void rejectLoanApplication(Long applicationId) {
		LoanApplication application = loanApplicationRepository.findById(applicationId)
				.orElseThrow(() -> new NotFoundException("Loan application with ID " + applicationId + " not found"));
		if (application.getApplicationStatus() == ApplicationStatus.PENDING) {
			application.setApplicationStatus(ApplicationStatus.REJECTED);
			application.setUpdatedAt(LocalDateTime.now());
			loanApplicationRepository.save(application);
		}
	}

	@Override
	public void deleteLoanApplicationStatusRejected(Long applicationId) {
		applicationRepository.findById(applicationId).ifPresentOrElse(applicationRepository::delete, () -> {
			throw new NotFoundException("No application with given ID found");
		});
		LoanApplication application = loanApplicationRepository.findById(applicationId)
				.orElseThrow(() -> new NotFoundException("Loan application with ID " + applicationId + " not found"));
		if (application.getApplicationStatus() == ApplicationStatus.REJECTED) {
			loanApplicationRepository.delete(application);
		}
		throw new ConflictException("Only rejected loan applications can be deleted.");
	}

	// create method version 2 by this method all the request are in the body
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
		boolean nationalIdExists = applicationRepository.exiexistsByNationalId(incomingApp.getNationalId());
		if (nationalIdExists) {
			throw new BadRequestException("An application with the provided national ID already exists.");
		}
		newApplication.setNationalId(incomingApp.getNationalId());
		newApplication.setPhoneNumber(incomingApp.getPhoneNumber());
		newApplication.setUpdatedAt(LocalDateTime.now());
		boolean emailExists = applicationRepository.existsByEmail(incomingApp.getEmail());
		if (emailExists) {
			throw new BadRequestException("An application with the provided email already exists.");
		}
		newApplication.setEmail(incomingApp.getEmail());

		applicationRepository.save(newApplication);

		// Create LoanApplication and attach the saved Application
		LoanApplication newLoan = new LoanApplication();
		newLoan.setApplication(newApplication);
		newLoan.setLoanType(loanApplication.getLoanType());
		newLoan.setEmploymentStatus(loanApplication.getEmploymentStatus());
		newLoan.setLoanAmount(loanApplication.getLoanAmount());
		newLoan.setLoanDurationInMonths(loanApplication.getLoanDurationInMonths());
		newLoan.setLoanPurpose(loanApplication.getLoanPurpose());
		newLoan.setCreatedAt(LocalDateTime.now());
		newLoan.setUpdatedAt(LocalDateTime.now());
		return loanApplicationRepository.save(newLoan);
	}

	@Override
	public PaginationDTO<LoanApplication> getAllLoanApplicationPagination(int page, int size) {
		Page<LoanApplication> loanPage = loanApplicationRepository.findAll(PageRequest.of(page, size));
		List<LoanApplication> filteredList = loanPage.getContent().stream()
				.filter(app -> app.getLoanRefundStatus() == LoanRefundStatus.COMPLETED
						|| app.getLoanRefundStatus() == LoanRefundStatus.IN_PROGRESS)
				.collect(Collectors.toList());

		// return just loanApplication that have status COMPLETED and IN_PROGRESS
		return PaginationDTO.<LoanApplication>builder()
				.content(filteredList)
				.empty(loanPage.isEmpty())
				.first(loanPage.isFirst())
				.last(loanPage.isLast())
				.pageNumber(loanPage.getNumber())
				.pageSize(loanPage.getSize())
				.totalElements(loanPage.getTotalElements())
				.totalPages(loanPage.getTotalPages())
				.numberOfElements(loanPage.getNumberOfElements())
				.build();

		// if return loanApplication no care about status
		// return PaginationDTO.fromPage(loanPage);
	}

	// at here i can add the parameter if i need to filter by choosing type but now
	// get all with in progress status
	@Override
	public List<LoanApplication> getAllLoanApplicationsByRefundStatus(LoanRefundStatus refundStatus) {
		List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
		return loanApplications.stream().filter(loan -> loan.getLoanRefundStatus() == refundStatus)
				.toList();
	}

	@Override
	public void deleteById(Long id) {
		Application application = applicationRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Application not found with id : " + id));
		applicationRepository.deleteById(id);
		boolean hasApplication = loanApplicationRepository.existsByApplicationApplicationId(id);
		if (hasApplication) {
			loanApplicationRepository.deleteByApplicationApplicationId(id);
		}
	}

	// get all loan recently updated today
	@Override
	public List<LoanApplication> getAllLoanRecentUpdatedToday() {
		List<LoanApplication> applications = loanApplicationRepository.getLoanApplicationThatRecentUpdateToday(
				LocalDate.now());
		if (applications.isEmpty()) {
			return List.of();
		}
		return applications;
	}

	@Override
	public void deleteLoanApplicationById(Long applicationId) {
		Application application = applicationRepository.findById(applicationId)
				.orElseThrow(() -> new NotFoundException("Application not found with id : " + applicationId));
		boolean hasApplication = loanApplicationRepository.existsByApplicationApplicationId(applicationId);
		if (hasApplication) {
			throw new DataIntegrityViolationException(
					" delete this application. Please delete associated loan applications first.");
		}
	}

	@Override
	public List<LoanApplication> getAllLoanApplicationRefundCompleted() {
		List<LoanApplication> loanApplicationRefundCompleted = loanApplicationRepository.findAll();
		List<LoanApplication> collect = loanApplicationRefundCompleted.stream()
				.filter(loan -> loan.getLoanRefundStatus() == LoanRefundStatus.COMPLETED).collect(Collectors.toList());
		if (collect.isEmpty()) {
			return List.of();
		}
		return collect;
	}

	// this for giving faking value to database if want to test
	// @PostConstruct
	public void generateFakeLoanApplications() {
		for (int i = 0; i < 100; i++) {
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
			Application managedApp = applicationRepository.findById(saved.getApplicationId()).orElseThrow();

			LoanApplication loanApp = new LoanApplication();

			loanApp.setApplication(managedApp); // IMPORTANT FIX
			loanApp.setLoanType(randomLoanType());
			loanApp.setEmploymentStatus(randomEmploymentStatus());
			loanApp.setApplicationStatus(ApplicationStatus.PENDING);
			loanApp.setLoanAmount(new BigDecimal(faker.number().numberBetween(100, 50000)));
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

	@Override
	public PaginationDTO<LoanApplication> getAllLoanApplicationRefundInProgress(int page, int size) {
		Page<LoanApplication> loanPage = loanApplicationRepository.findAll(PageRequest.of(page, size));
		List<LoanApplication> inProgressLoan = loanPage.getContent().stream()
				.filter(app -> app.getLoanRefundStatus() == LoanRefundStatus.IN_PROGRESS)
				.collect(Collectors.toList());
		// return just loanApplication that have status COMPLETED and IN_PROGRESS
		return PaginationDTO.<LoanApplication>builder()
				.content(inProgressLoan)
				.empty(loanPage.isEmpty())
				.first(loanPage.isFirst())
				.last(loanPage.isLast())
				.pageNumber(loanPage.getNumber())
				.pageSize(loanPage.getSize())
				.totalElements(loanPage.getTotalElements())
				.totalPages(loanPage.getTotalPages())
				.numberOfElements(loanPage.getNumberOfElements())
				.build();
	}
}
