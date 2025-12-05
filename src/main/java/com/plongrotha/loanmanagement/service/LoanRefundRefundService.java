package com.plongrotha.loanmanagement.service;

import java.util.List;

import com.plongrotha.loanmanagement.model.LoanRefundRefund;

public interface LoanRefundRefundService {
	
	 // Create a new refund record
	LoanRefundRefund createRefund(LoanRefundRefund refund);

    // Find refund by ID
	LoanRefundRefund getRefundById(Long id);
	
	
	// load all loan refunded
	
	List<LoanRefundRefund> getAllLoans();

    // Get all refunds for a given loan application
    List<LoanRefundRefund> getRefundsByLoanApplicationId(Long loanApplicationId);

    // Update an existing refund record
    LoanRefundRefund updateRefund(LoanRefundRefund refund);

    // Delete a refund record
    void deleteRefund(Long id);
    
}
