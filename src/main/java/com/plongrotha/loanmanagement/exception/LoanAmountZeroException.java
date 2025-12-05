package com.plongrotha.loanmanagement.exception;

public class LoanAmountZeroException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public LoanAmountZeroException(String message) {
        super(message);
    }
}