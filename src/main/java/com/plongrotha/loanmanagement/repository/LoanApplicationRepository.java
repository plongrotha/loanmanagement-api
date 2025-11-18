package com.plongrotha.loanmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.model.enums.ApplicationStatus;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    List<LoanApplication> findAllByApplicationStatus(ApplicationStatus applicationStatus);
}
