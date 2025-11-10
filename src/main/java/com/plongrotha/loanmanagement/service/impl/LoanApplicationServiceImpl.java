package com.plongrotha.loanmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plongrotha.loanmanagement.exception.NotFoundException;
import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.model.enums.ApplicationStatus;
import com.plongrotha.loanmanagement.model.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.model.enums.LoanType;
import com.plongrotha.loanmanagement.repository.LoanApplicationRepository;
import com.plongrotha.loanmanagement.service.LoanApplicationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;

    @Override
    public List<LoanType> getAllLoanTypes() {
        List<LoanType> loanTypes = List.of(LoanType.values());
        if (loanTypes.isEmpty()) {
            throw new NotFoundException("No loan types found");
        }
        return loanTypes;
    }

    @Override
    public List<EmploymentStatus> getAllEmploymentStatuses() {

        List<EmploymentStatus> employmentStatuses = List.of(EmploymentStatus.values());
        if (employmentStatuses.isEmpty()) {
            throw new NotFoundException("No employment statuses found");
        }
        return employmentStatuses;
    }

    @Override
    public List<LoanApplication> getAllLoanApplications() {
        List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
        if (loanApplications.isEmpty()) {
            throw new NotFoundException("No loan applications found");
        }
        return loanApplications;
    }

    @Override
    public LoanApplication createNewLoanApplication(LoanApplication loanApplication) {
        LoanApplication newLoan = new LoanApplication();
        newLoan.setApplicantFullName(loanApplication.getApplicantFullName());
        newLoan.setAddress(loanApplication.getAddress());
        newLoan.setPhoneNumber(loanApplication.getPhoneNumber());
        newLoan.setNationalId(loanApplication.getNationalId());
        newLoan.setMonthlyIncome(loanApplication.getMonthlyIncome());
        newLoan.setLoanType(loanApplication.getLoanType());
        newLoan.setEmploymentStatus(loanApplication.getEmploymentStatus());
        newLoan.setLoanAmount(loanApplication.getLoanAmount());
        newLoan.setLoanDurationInMonths(loanApplication.getLoanDurationInMonths());
        newLoan.setInterestRate(loanApplication.getInterestRate());
        newLoan.setLoanPurpose(loanApplication.getLoanPurpose());
        return loanApplicationRepository.save(newLoan);
    }

    @Override
    public List<LoanApplication> getAllLoanApplicationsByStatus(ApplicationStatus status) {

        List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
        if (loanApplications.isEmpty()) {
            throw new NotFoundException("No loan applications found");
        }
        return loanApplications.stream()
                .filter(application -> application.getApplicationStatus() == status)
                .toList();
    }

    @Override
    public List<LoanApplication> getAllLoanApplicationsByEmploymentStatus(EmploymentStatus employmentStatus) {
        List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
        if (loanApplications.isEmpty()) {
            throw new NotFoundException("No loan applications found");
        }
        return loanApplications.stream()
                .filter(application -> application.getEmploymentStatus() == employmentStatus)
                .toList();
    }

    @Override
    public List<LoanApplication> getAllLoanApplicationsByLoanType(LoanType loanType) {
        List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
        if (loanApplications.isEmpty()) {
            throw new NotFoundException("No loan applications found");
        }
        return loanApplications.stream()
                .filter(application -> application.getLoanType() == loanType)
                .toList();
    }

    @Override
    public List<LoanApplication> createApplicationsBulk(List<LoanApplication> loanApplications) {
        List<LoanApplication> saveApplications = new ArrayList<>();
        for (LoanApplication loanApplication : loanApplications) {
            saveApplications.add(createNewLoanApplication(loanApplication));
        }
        return saveApplications;
    }

    @Override
    public LoanApplication updateLoanApplicationStatus(Long applicationId, ApplicationStatus newStatus) {
        LoanApplication application = loanApplicationRepository.findById(applicationId).orElseThrow(
                () -> new NotFoundException("Loan application with ID " + applicationId + " not found"));

        if (application.getApplicationStatus() == ApplicationStatus.PENDING) {
            application.setApplicationStatus(newStatus);
        }

        return loanApplicationRepository.save(application);
    }
}
