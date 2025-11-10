package com.plongrotha.loanmanagement.exception;


public class ConflictException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConflictException() {
        super("Data already exists");
    }

    public ConflictException(String message) {
        super(message);
    }
}