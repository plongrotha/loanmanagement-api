package com.plongrotha.loanmanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plongrotha.loanmanagement.dto.request.LoanApplicationRequest;
import com.plongrotha.loanmanagement.dto.response.ApiResponse;
import com.plongrotha.loanmanagement.dto.response.LoanApplicationResponse;
import com.plongrotha.loanmanagement.dto.response.PaginationDTO;
import com.plongrotha.loanmanagement.enums.ApplicationStatus;
import com.plongrotha.loanmanagement.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.enums.LoanRefundStatus;
import com.plongrotha.loanmanagement.enums.LoanType;
import com.plongrotha.loanmanagement.mapper.LoanApplicationMapper;
import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.service.LoanApplicationService;
import com.plongrotha.loanmanagement.utils.ResponseUtil;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loan-applications")
@CrossOrigin
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;
    private final LoanApplicationMapper loanApplicationMapper;

    @Operation(summary = "Get all loan types")
    @GetMapping("/loan-types")
    public ResponseEntity<ApiResponse<List<LoanType>>> getAllLoanTypes() {
        var loanTypes = loanApplicationService.getAllLoanTypes();
        return ResponseUtil.success(loanTypes, "Loan types retrieved successfully");
    }

    @Operation(summary = "Get all employment statuses")
    @GetMapping("/employment-statuses")
    public ResponseEntity<ApiResponse<List<EmploymentStatus>>> getAllEmploymentStatuses() {
        var employmentStatuses = loanApplicationService.getAllEmploymentStatuses();
        return ResponseUtil.success(employmentStatuses, "Employment statuses retrieved successfully");
    }

    // in this controller i have 2 post method to create loan application
    @Operation(summary = "Create a new loan application")
    @PostMapping
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> createLoanApplication(
            @RequestBody @Valid LoanApplicationRequest loanApplicationRequest) {
        var application = loanApplicationMapper.toEntity(loanApplicationRequest);

        var savedApplication = loanApplicationService.createNewLoanApplication(application);
        return ResponseUtil.created(loanApplicationMapper.toResponse(savedApplication),
                "Loan application created successfully");
    }

    @Operation(summary = "Create a new loan application V2")
    @PostMapping("/createV2")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> createLoanApplicationV2(
            @RequestBody @Valid LoanApplicationRequest loanApplicationRequest) {
        // convert request to entity
        var application = loanApplicationMapper.toEntity(loanApplicationRequest);
        // call service to save application
        var savedApplication = loanApplicationService.createNewLoanApplicationV2(application);
        // and then return response to client by convert it from entity to response
        return ResponseUtil.created(loanApplicationMapper.toResponse(savedApplication),
                "Loan application created successfully");
    }

    @Operation(summary = "Get loan applications by loan type")
    @GetMapping("/by-loan-type")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getLoanApplicationsByLoanType(
            @RequestParam LoanType loanType) {
        var loanApplications = loanApplicationService.getAllLoanApplicationsByLoanType(loanType);
        var dto = loanApplicationMapper.toResponseList(loanApplications);
        return ResponseUtil.success(dto, "Loan applications retrieved successfully");
    }

    @Operation(summary = "Get loan applications by employment status")
    @GetMapping("/by-employment-status")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getLoanApplicationsByEmploymentStatus(
            @RequestParam EmploymentStatus employmentStatus) {
        var loanApplications = loanApplicationService.getAllLoanApplicationsByEmploymentStatus(employmentStatus);
        var dto = loanApplicationMapper.toResponseList(loanApplications);
        if (loanApplications.isEmpty()) {
            return ResponseUtil.success(List.of(), "No loan applications found for the specified employment status");
        }
        return ResponseUtil.success(dto, "Loan applications retrieved successfully");
    }

    @Operation(summary = "Get loan applications by status")
    @GetMapping("/by-status")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getLoanApplicationsByStatus(
            @RequestParam ApplicationStatus status) {
        var loanApplications = loanApplicationService.getAllLoanApplicationsByStatus(status);
        var dto = loanApplicationMapper.toResponseList(loanApplications);
        return ResponseUtil.success(dto, "Loan applications retrieved successfully");
    }

    // note need to update {applicationId}/approve to @RequestParam
    @Operation(summary = "Approve a loan application")
    @PostMapping("/approve")
    public ResponseEntity<ApiResponse<Void>> approveLoanApplication(@RequestParam @Positive Long applicationId) {
        loanApplicationService.approveLoanApplication(applicationId);
        return ResponseUtil.success("Loan application approved successfully");
    }

    @GetMapping("/pending")
    @Operation(summary = "Get all loan applications with PENDING status")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getAllPendingLoanApplications() {
        var pendingApplications = loanApplicationService.getAllApplicationStatusPending();
        var dto = loanApplicationMapper.toResponseList(pendingApplications);
        return ResponseUtil.success(dto, "Pending loan applications retrieved successfully");
    }

    @GetMapping("/{loanApplicationById}")
    @Operation(summary = "Get loan application by ID")
    public ResponseEntity<ApiResponse<LoanApplicationResponse>> getLoanApplicationById(
            @PathVariable @Positive Long loanApplicationById) {
        var app = loanApplicationService.getLoanApplicationById(loanApplicationById);
        var dto = loanApplicationMapper.toResponse(app);
        return ResponseUtil.success(dto, "Loan application retrieved successfully");
    }

    @GetMapping("/application-statuses")
    @Operation(summary = "Get all application statuses")
    public ResponseEntity<ApiResponse<List<ApplicationStatus>>> getAllApplicationStatuses() {
        var applicationStatuses = loanApplicationService.getAllApplicationStatuses();
        return ResponseUtil.success(applicationStatuses, "Application statuses retrieved successfully");
    }

    // note need to update {applicationId}/reject to @RequestParam
    @Operation(summary = "Reject a loan application")
    @PostMapping("/reject")
    public ResponseEntity<ApiResponse<Void>> rejectLoanApplication(@RequestParam @Positive Long applicationId) {
        loanApplicationService.rejectLoanApplication(applicationId);
        return ResponseUtil.success("Loan application rejected successfully");
    }

    @Operation(summary = "Get all loan applications")
    @GetMapping
    public ResponseEntity<ApiResponse<List<LoanApplication>>> getAllLoanApplication() {
        var loanApplications = loanApplicationService.getAllLoanApplications();
        return ResponseUtil.success(loanApplications, loanApplications.isEmpty() ? "No loan applications found"
                : "Loan applications retrieved successfully");
    }

    @Operation(summary = "Retrieve all LoanApplicationResponse with pagination")
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<PaginationDTO<LoanApplicationResponse>>> getAllLoanApplicationPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var list = loanApplicationService.getAllLoanApplicationPagination(page, size);
        var dto = loanApplicationMapper.toPaginationResponse(list);
        return ResponseUtil.ok(dto, "loanApplicaion retrieve successfully.");
    }

    @Operation(summary = "Retrieve all LoanApplicationResponse by RefundStatus")
    @GetMapping("/by-refund-status")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getAllLoanApplicationByRefundStatus(
            @RequestParam LoanRefundStatus refundStatus) {
        var app = loanApplicationService.getAllLoanApplicationsByRefundStatus(refundStatus);
        var dto = loanApplicationMapper.toResponseList(app);
        return ResponseUtil.ok(dto, "loanApplicaion retrieve successfully.");
    }

    @Operation(summary = "Get all loanApplication that Refund dept completed")
    @GetMapping("refund-completed")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getAllLoanApplicationRefundCompleted() {
        var loanApplications = loanApplicationService.getAllLoanApplicationRefundCompleted();
        var dto = loanApplicationMapper.toResponseList(loanApplications);
        return ResponseUtil.ok(dto, "loanApplicaion refund completed retrieve successfully.");
    }

    @Operation(summary = "Retrieve all LoanApplicationResponse recent updated today")
    @GetMapping("/recent-updated-today")
    public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getAllLoanRecentUpdatedToday() {
        var applications = loanApplicationService.getAllLoanRecentUpdatedToday();
        var dto = loanApplicationMapper.toResponseList(applications);
        return ResponseUtil.ok(dto, "loan Applications retrieved successfully.");
    }

    @GetMapping("/refund-in-progress")
    @Operation(summary = "Retrieve all LoanApplicationResponse with refund in progress with pagination")
    public ResponseEntity<ApiResponse<PaginationDTO<LoanApplicationResponse>>> getAllLoanApplicationRefundInProgress(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var listApp = loanApplicationService.getAllLoanApplicationRefundInProgress(page, size);
        var dto = loanApplicationMapper.toPaginationResponse(listApp);
        return ResponseUtil.ok(dto, "loanApplicaion retrieve successfully.");
    }
}
