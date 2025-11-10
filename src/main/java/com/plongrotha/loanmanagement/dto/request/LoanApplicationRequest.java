package com.plongrotha.loanmanagement.dto.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.plongrotha.loanmanagement.model.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.model.enums.LoanType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationRequest {

    @NotBlank(message = "Applicant full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String applicantFullName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{8,15}$", message = "Phone number must be between 8 and 15 digits")
    private String phoneNumber;

    @NotBlank(message = "National ID is required")
    @Size(max = 20, message = "National ID must not exceed 20 characters")
    private String nationalId;

    @NotNull(message = "Monthly income is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly income must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Monthly income must be a valid amount")
    private BigDecimal monthlyIncome;

    @JsonIgnore
    private LoanType loanType;

    @JsonIgnore
    private EmploymentStatus employmentStatus;

    @NotNull(message = "Loan amount is required")
    @Positive(message = "Loan amount must be greater than 0")
    private BigDecimal loanAmount;

    @Min(value = 1, message = "Loan duration must be at least 1 month")
    private int loanDurationInMonths;

    @DecimalMin(value = "0.1", inclusive = false, message = "Interest rate must be greater than 0")
    private double interestRate;

    @NotBlank(message = "Loan purpose is required")
    @Size(max = 255, message = "Loan purpose must not exceed 255 characters")
    private String loanPurpose;
}
