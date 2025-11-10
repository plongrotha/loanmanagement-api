package com.plongrotha.loanmanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plongrotha.loanmanagement.dto.request.LoanApplicationBulkRequest;
import com.plongrotha.loanmanagement.dto.request.LoanApplicationRequest;
import com.plongrotha.loanmanagement.dto.response.ApiResponse;
import com.plongrotha.loanmanagement.dto.response.LoanApplicationResponse;
import com.plongrotha.loanmanagement.mapper.LoanApplicationMapper;
import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.model.enums.ApplicationStatus;
import com.plongrotha.loanmanagement.model.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.model.enums.LoanType;
import com.plongrotha.loanmanagement.service.LoanApplicationService;
import com.plongrotha.loanmanagement.utils.ResponseUtil;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loan-applications")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;
    private final LoanApplicationMapper loanApplicationMapper;

    @Operation(summary = "Get all loan types")
    @GetMapping("/loan-types")
    public ResponseEntity<ApiResponse<List<LoanType>>> getAllLoanTypes() {
        List<LoanType> loanTypes = loanApplicationService.getAllLoanTypes();
        return ResponseUtil.success(loanTypes, "Loan types retrieved successfully");
    }

    @Operation(summary = "Get all employment statuses")
    @GetMapping("/employment-statuses")
    public ResponseEntity<ApiResponse<List<EmploymentStatus>>> getAllEmploymentStatuses() {
        List<EmploymentStatus> employmentStatuses = loanApplicationService.getAllEmploymentStatuses();
        return ResponseUtil.success(employmentStatuses, "Employment statuses retrieved successfully");
    }

    @Operation(summary = "Create a new loan application")
    @PostMapping
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> createLoanApplication(
            @RequestBody @Valid LoanApplicationRequest loanApplicationRequest,
            @RequestParam("loanType") LoanType loanType,
            @RequestParam("employmentStatus") EmploymentStatus employmentStatus) {
        LoanApplication application = loanApplicationMapper.toEntity(loanApplicationRequest);
        application.setLoanType(loanType);
        application.setEmploymentStatus(employmentStatus);
        LoanApplication savedApplication = loanApplicationService.createNewLoanApplication(application);
        return ResponseUtil.success(loanApplicationMapper.toResponse(savedApplication),
                "Loan application created successfully");
    }

    @Operation(summary = "Get loan applications by loan type")
    @GetMapping("/by-loan-type")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getLoanApplicationsByLoanType(
            @RequestParam("loanType") LoanType loanType) {
        List<LoanApplication> loanApplications = loanApplicationService.getAllLoanApplicationsByLoanType(loanType);
        List<LoanApplicationResponse> responses = loanApplicationMapper.toResponseList(loanApplications);
        return ResponseUtil.success(responses, "Loan applications retrieved successfully");
    }

    @Operation(summary = "Get loan applications by employment status")
    @GetMapping("/by-employment-status")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getLoanApplicationsByEmploymentStatus(
            @RequestParam("employmentStatus") EmploymentStatus employmentStatus) {
        List<LoanApplication> loanApplications = loanApplicationService
                .getAllLoanApplicationsByEmploymentStatus(employmentStatus);
        List<LoanApplicationResponse> responses = loanApplicationMapper.toResponseList(loanApplications);
        return ResponseUtil.success(responses, "Loan applications retrieved successfully");
    }

    @Operation(summary = "Get loan applications by status")
    @GetMapping("/by-status")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getLoanApplicationsByStatus(
            @RequestParam("status") ApplicationStatus status) {
        List<LoanApplication> loanApplications = loanApplicationService
                .getAllLoanApplicationsByStatus(status);
        List<LoanApplicationResponse> responses = loanApplicationMapper.toResponseList(loanApplications);
        return ResponseUtil.success(responses, "Loan applications retrieved successfully");
    }

    @Operation(summary = "Create bulk loan applications")
    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> createBulkApplication(
            @RequestBody @Valid List<LoanApplicationBulkRequest> loanApplicationRequests) {
        List<LoanApplication> applications = loanApplicationMapper.toEntity(loanApplicationRequests);
        List<LoanApplication> savedApplications = loanApplicationService.createApplicationsBulk(applications);
        return ResponseUtil.success(loanApplicationMapper.toResponseList(savedApplications),
                "Bulk loan applications created successfully");
    }
}
