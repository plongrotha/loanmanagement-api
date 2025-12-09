package com.plongrotha.loanmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.model.enums.ApplicationStatus;
import com.plongrotha.loanmanagement.model.enums.EmploymentStatus;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

	List<LoanApplication> findAllByApplicationStatus(ApplicationStatus applicationStatus);

	void deleteByApplicationApplicationId(Long applicationId);

	boolean existsByApplicationApplicationId(Long applicationLong);

	@Query("SELECT la FROM LoanApplication la WHERE la.employmentStatus = :employmentStatus")
	List<LoanApplication> findByEmploymentStatus(@Param("employmentStatus") EmploymentStatus employmentStatus);
}
