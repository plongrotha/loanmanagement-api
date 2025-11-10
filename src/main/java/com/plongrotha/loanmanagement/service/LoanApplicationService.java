package com.plongrotha.loanmanagement.service;

import java.util.List;

import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.model.enums.ApplicationStatus;
import com.plongrotha.loanmanagement.model.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.model.enums.LoanType;

public interface LoanApplicationService {

    List<LoanType> getAllLoanTypes();

    List<EmploymentStatus> getAllEmploymentStatuses();

    List<LoanApplication> getAllLoanApplications();

    List<LoanApplication> createApplicationsBulk(List<LoanApplication> loanApplications);

    LoanApplication createNewLoanApplication(LoanApplication loanApplication);

    List<LoanApplication> getAllLoanApplicationsByStatus(ApplicationStatus status);

    List<LoanApplication> getAllLoanApplicationsByEmploymentStatus(EmploymentStatus employmentStatus);

    List<LoanApplication> getAllLoanApplicationsByLoanType(LoanType loanType);

    LoanApplication updateLoanApplicationStatus(Long applicationId, ApplicationStatus newStatus);

}
