package com.plongrotha.loanmanagement.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRefundRequest {

	@NotNull(message = "loanApplicationId can not be null")
	@Positive(message = "no have loanApplicationId is negative or zero")
	private Long loanApplicationId;

	@Positive(message = "refundAmount must be positive and greater than 0")
	private BigDecimal refundAmount;
}
