package com.plongrotha.loanmanagement.dto.response;

import java.math.BigDecimal;

import com.plongrotha.loanmanagement.model.enums.ApplicationStatus;
import com.plongrotha.loanmanagement.model.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.model.enums.LoanRefundStatus;
import com.plongrotha.loanmanagement.model.enums.LoanType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationResponse {

	private ApplicationResponse applicationResponse;
	private Long loanApplicationId;
	private LoanType loanType;
	private EmploymentStatus employmentStatus;
	private ApplicationStatus applicationStatus;
	private BigDecimal loanAmount;
	private BigDecimal paidAmount;
	private LoanRefundStatus loanRefundStatus;
	private String loanPurpose;
	private int loanDurationInMonths;
}
