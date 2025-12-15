package com.plongrotha.loanmanagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.plongrotha.loanmanagement.enums.ApplicationStatus;
import com.plongrotha.loanmanagement.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.enums.LoanRefundStatus;
import com.plongrotha.loanmanagement.enums.LoanType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationResponse {

	private Long loanApplicationId;
	private LoanType loanType;
	private EmploymentStatus employmentStatus;
	private ApplicationStatus applicationStatus;
	private BigDecimal loanAmount;
	private BigDecimal paidAmount;
	private LoanRefundStatus loanRefundStatus;
	private String loanPurpose;
	private int loanDurationInMonths;
	private LocalDateTime updatedAt;
	private ApplicationResponse applicationResponse;
}
