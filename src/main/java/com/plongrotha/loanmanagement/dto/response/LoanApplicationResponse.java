package com.plongrotha.loanmanagement.dto.response;

import java.math.BigDecimal;

import com.plongrotha.loanmanagement.model.enums.ApplicationStatus;
import com.plongrotha.loanmanagement.model.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.model.enums.LoanType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationResponse {

    private Long applicationId;
    private String applicantFullName;
    private LoanType loanType;
    private EmploymentStatus employmentStatus;
    private ApplicationStatus applicationStatus;
    private String loanPurpose;
    private BigDecimal loanAmount;
    private int loanDurationInMonths;
    private String address;
    private String phoneNumber;
    private String nationalId;
    private BigDecimal monthlyIncome;
}
