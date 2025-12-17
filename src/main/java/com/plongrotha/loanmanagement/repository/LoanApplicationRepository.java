package com.plongrotha.loanmanagement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.plongrotha.loanmanagement.enums.ApplicationStatus;
import com.plongrotha.loanmanagement.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.model.LoanApplication;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

	List<LoanApplication> findAllByApplicationStatus(ApplicationStatus applicationStatus);

	void deleteByApplicationApplicationId(Long applicationId);

	@Query("SELECT la FROM LoanApplication la WHERE la.application.applicationId = :applicationId")
	LoanApplication findByApplicationId(Long applicationId);

	boolean existsByApplicationApplicationId(Long applicationLong);

	@Query("SELECT la FROM LoanApplication la WHERE la.employmentStatus = :employmentStatus")
	List<LoanApplication> findByEmploymentStatus(@Param("employmentStatus") EmploymentStatus employmentStatus);

	@Query("SELECT la FROM LoanApplication la WHERE DATE(la.updatedAt) = :today")
	List<LoanApplication> getLoanApplicationThatRecentUpdateToday(LocalDate today);

	@Query("SELECT la FROM LoanApplication la WHERE  la.application.nationalId = :nationalId")
	LoanApplication findByNationalID(String nationalId);
}