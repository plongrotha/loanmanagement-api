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
import com.plongrotha.loanmanagement.exception.NotFoundException;
import com.plongrotha.loanmanagement.mapper.LoanApplicationMapper;
import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.model.enums.ApplicationStatus;
import com.plongrotha.loanmanagement.model.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.model.enums.LoanType;
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
		List<LoanType> loanTypes = loanApplicationService.getAllLoanTypes();
		return ResponseUtil.success(loanTypes, "Loan types retrieved successfully");
	}

	@Operation(summary = "Get all employment statuses")
	@GetMapping("/employment-statuses")
	public ResponseEntity<ApiResponse<List<EmploymentStatus>>> getAllEmploymentStatuses() {
		List<EmploymentStatus> employmentStatuses = loanApplicationService.getAllEmploymentStatuses();
		return ResponseUtil.success(employmentStatuses, "Employment statuses retrieved successfully");
	}

	// in this controller i have 2 post method to create loan application
	@Operation(summary = "Create a new loan application")
	@PostMapping
	public ResponseEntity<ApiResponse<LoanApplicationResponse>> createLoanApplication(
			@RequestBody @Valid LoanApplicationRequest loanApplicationRequest,
			@RequestParam @Valid LoanType loanType,
			@RequestParam @Valid EmploymentStatus employmentStatus) {
		LoanApplication application = loanApplicationMapper.toEntity(loanApplicationRequest);
		application.setLoanType(loanType);
		application.setEmploymentStatus(employmentStatus);
		LoanApplication savedApplication = loanApplicationService.createNewLoanApplication(application);
		return ResponseUtil.success(loanApplicationMapper.toResponse(savedApplication),
				"Loan application created successfully");
	}

	@Operation(summary = "Create a new loan application V2")
	@PostMapping("/createV2")
	public ResponseEntity<ApiResponse<LoanApplicationResponse>> createLoanApplicationV2(
			@RequestBody @Valid LoanApplicationRequest loanApplicationRequest) {
		// convert request to entity
		LoanApplication application = loanApplicationMapper.toEntity(loanApplicationRequest);
		// call service to save application
		LoanApplication savedApplication = loanApplicationService.createNewLoanApplicationV2(application);
		// and then return response to client by convert it from entity to response
		return ResponseUtil.success(loanApplicationMapper.toResponse(savedApplication),
				"Loan application created successfully");
	}

	@Operation(summary = "Get loan applications by loan type")
	@GetMapping("/by-loan-type")
	public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getLoanApplicationsByLoanType(
			@RequestParam LoanType loanType) {
		List<LoanApplication> loanApplications = loanApplicationService.getAllLoanApplicationsByLoanType(loanType);
		List<LoanApplicationResponse> responses = loanApplicationMapper.toResponseList(loanApplications);
		return ResponseUtil.success(responses, "Loan applications retrieved successfully");
	}

	@Operation(summary = "Get loan applications by employment status")
	@GetMapping("/by-employment-status")
	public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getLoanApplicationsByEmploymentStatus(
			@RequestParam EmploymentStatus employmentStatus) {
		List<LoanApplication> loanApplications = loanApplicationService
				.getAllLoanApplicationsByEmploymentStatus(employmentStatus);
		List<LoanApplicationResponse> responses = loanApplicationMapper.toResponseList(loanApplications);
		return ResponseUtil.success(responses, "Loan applications retrieved successfully");
	}

	@Operation(summary = "Get loan applications by status")
	@GetMapping("/by-status")
	public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getLoanApplicationsByStatus(
			@RequestParam ApplicationStatus status) {
		List<LoanApplication> loanApplications = loanApplicationService.getAllLoanApplicationsByStatus(status);
		List<LoanApplicationResponse> responses = loanApplicationMapper.toResponseList(loanApplications);
		return ResponseUtil.success(responses, "Loan applications retrieved successfully");
	}

	@Operation(summary = "Approve a loan application")
	@PostMapping("/approve")
	public ResponseEntity<ApiResponse<Void>> approveLoanApplication(@RequestParam @Positive Long applicationId) {
		loanApplicationService.approveLoanApplication(applicationId);
		return ResponseUtil.success("Loan application approved successfully");
	}

	@GetMapping("/pending")
	@Operation(summary = "Get all loan applications with PENDING status")
	public ResponseEntity<ApiResponse<List<LoanApplication>>> getAllPendingLoanApplications() {
		List<LoanApplication> pendingApplications = loanApplicationService.getAllApplicatonStatusPending();
		if (pendingApplications.isEmpty()) {
			throw new NotFoundException("No pending loan applications found");
		}
		return ResponseUtil.success(pendingApplications, "Pending loan applications retrieved successfully");
	}

	@GetMapping("/{applicationId}")
	@Operation(summary = "Get loan application by ID")
	public ResponseEntity<ApiResponse<LoanApplicationResponse>> getLoanApplicationById(
			@PathVariable @Positive Long applicationId) {
		LoanApplication application = loanApplicationService.getLoanApplicationById(applicationId);
		LoanApplicationResponse response = loanApplicationMapper.toResponse(application);
		return ResponseUtil.success(response, "Loan application retrieved successfully");
	}

	@GetMapping("/application-statuses")
	@Operation(summary = "Get all application statuses")
	public ResponseEntity<ApiResponse<List<ApplicationStatus>>> getAllApplicationStatuses() {
		List<ApplicationStatus> applicationStatuses = loanApplicationService.getAllApplicationStatuses();
		return ResponseUtil.success(applicationStatuses, "Application statuses retrieved successfully");
	}

	@Operation(summary = "Reject a loan application")
	@PostMapping("/reject")
	public ResponseEntity<ApiResponse<Void>> rejectLoanApplication(@RequestParam @Positive Long applicationId) {
		loanApplicationService.rejectLoanApplication(applicationId);
		return ResponseUtil.success("Loan application rejected successfully");
	}

	@Operation(summary = "Get all loan applications")
	@GetMapping
	public ResponseEntity<ApiResponse<List<LoanApplication>>> getAllLoanApplication() {
		List<LoanApplication> loanApplications = loanApplicationService.getAllLoanApplications();
		return ResponseUtil.success(loanApplications, "Loan applications retrieved successfully");
	}

	@Operation(summary = "Retrieve all LoanApplicationResponse with pagination")
	@GetMapping("/page")
	public ResponseEntity<ApiResponse<PaginationDTO<LoanApplicationResponse>>> getAllLoanApplicationPagination(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		PaginationDTO<LoanApplication> dto = loanApplicationService.getAllLoanApplicationPagination(page, size);
		PaginationDTO<LoanApplicationResponse> response = loanApplicationMapper.toPaginationResponse(dto);
		return ResponseUtil.ok(response, "loanApplicaion retrieve succfully.");
	}

	@Operation(summary = "Retrieve all LoanApplicationResponse by RefundStatus")
	@GetMapping("/by-refund-status")
	public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getAllLoanApplicationByRefundStatus() {
		List<LoanApplication> applications = loanApplicationService.getAllLoanApplicationsByRefundStatus();
		List<LoanApplicationResponse> response = loanApplicationMapper.toResponseList(applications);
		return ResponseUtil.ok(response, "loanApplicaion retrieve succfully.");
	}

	@Operation(summary = "Get all loanApplication that Refund dept completed")
	@GetMapping("refund-completed")
	public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getAllLoanApplicationRefundCompleted() {
		List<LoanApplication> loanApplications = loanApplicationService.getAllLoanApplicationRefundCompleted();
		List<LoanApplicationResponse> response = loanApplicationMapper.toResponseList(loanApplications);
		return ResponseUtil.ok(response, "loanApplicaion refund completed retrieve successfully.");
	}

	@Operation(summary = "Retrieve all LoanApplicationResponse approved today")
	@GetMapping("/approved-today")
	public ResponseEntity<ApiResponse<List<LoanApplicationResponse>>> getAllLoanApplicationsApprovedToday() {
		List<LoanApplication> applications = loanApplicationService.getAllLoanRecentUpdatedToday();
		List<LoanApplicationResponse> response = loanApplicationMapper.toResponseList(applications);
		return ResponseUtil.ok(response, "Approved loans for today retrieved successfully.");
	}
}
