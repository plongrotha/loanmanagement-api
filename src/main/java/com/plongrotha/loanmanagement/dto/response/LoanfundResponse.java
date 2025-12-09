package com.plongrotha.loanmanagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanfundResponse {

	private Long loanRefundId;
	private Long loanApplicationId;
	private BigDecimal totalLoanAmount;
	private BigDecimal refundAmount;
	private BigDecimal paidAmount;
	private BigDecimal remainAmount;
	private LocalDateTime refundRequestedDate;
	private LocalDateTime refundInitiatedDate;
	private LocalDateTime refundReadyDate;
	private LocalDateTime refundCompletedDate;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
