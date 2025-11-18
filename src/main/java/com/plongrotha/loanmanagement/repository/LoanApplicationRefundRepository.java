package com.plongrotha.loanmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plongrotha.loanmanagement.model.LoanRefundRefund;

@Repository
public interface LoanApplicationRefundRepository extends JpaRepository<LoanRefundRefund, Long> {
	 List<LoanRefundRefund> findByLoanApplication_LoanApplicationId(Long loanApplicationId);
}
