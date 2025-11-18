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

    List<ApplicationStatus> getAllApplicationStatuses();

    List<LoanApplication> getAllLoanApplicationsByStatus(ApplicationStatus status);

    List<LoanApplication> getAllLoanApplicationsByEmploymentStatus(EmploymentStatus employmentStatus);

    List<LoanApplication> getAllLoanApplicationsByLoanType(LoanType loanType);

    LoanApplication createNewLoanApplication(LoanApplication loanApplication);

    LoanApplication createNewLoanApplicationV2(LoanApplication loanApplication);

    LoanApplication updateLoanApplicationStatus(Long applicationId, ApplicationStatus newStatus);

    void approveLoanApplication(Long applicationId);
    
    void rejectLoanApplication(Long applicationId);

    void deleteLoanApplicationStatusRejected(Long applicationId);
    
    List<LoanApplication> getAllApplicatonStatusPending();

    LoanApplication getLoanApplicationById(Long applicationId);
}
