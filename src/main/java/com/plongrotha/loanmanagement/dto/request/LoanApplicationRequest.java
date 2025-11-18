package com.plongrotha.loanmanagement.dto.request;

import java.math.BigDecimal;

import com.plongrotha.loanmanagement.model.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.model.enums.LoanType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationRequest {

	
    private ApplicationRequest applicationRequest;
    
    private LoanType loanType;

    private EmploymentStatus employmentStatus;

    @Schema(example = "100")
    @NotNull(message = "Loan amount is required")
    @Positive(message = "Loan amount must be greater than 0")
    private BigDecimal loanAmount;

    @Min(value = 1, message = "Loan duration must be at least 1 month")
    private int loanDurationInMonths;

    @NotBlank(message = "Loan purpose is required")
    @Size(max = 255, message = "Loan purpose must not exceed 255 characters")
    private String loanPurpose;
}
