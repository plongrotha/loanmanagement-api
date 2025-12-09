package com.plongrotha.loanmanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plongrotha.loanmanagement.dto.request.LoanRefundRequest;
import com.plongrotha.loanmanagement.dto.response.ApiResponse;
import com.plongrotha.loanmanagement.dto.response.LoanfundResponse;
import com.plongrotha.loanmanagement.mapper.LoanRefundMapper;
import com.plongrotha.loanmanagement.service.LoanRefundRefundService;
import com.plongrotha.loanmanagement.utils.ResponseUtil;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/refund-application")
public class LoanRefundController {

	private final LoanRefundRefundService loanRefundRefundService;
	private final LoanRefundMapper loanRefundMapper;

	@Operation(summary = "Get All Loan Application")
	@GetMapping
	public ResponseEntity<ApiResponse<List<LoanfundResponse>>> getAllLoanApplicationRefund() {
		var loanRefundRefunds = loanRefundRefundService.getAllLoans();
		var dto = loanRefundMapper.toListResponse(loanRefundRefunds);
		String message = dto.isEmpty() ? "No loanApplication refund records found"
				: "all loanApplication refund retrived successfully";
		return ResponseUtil.ok(dto, message);
	}

	@Operation(summary = "Create Loan Refund")
	@PostMapping
	public ResponseEntity<ApiResponse<LoanfundResponse>> createLoanRefund(
			@RequestBody @Valid LoanRefundRequest request) {
		var loanRefundRefund = loanRefundMapper.toEntity(request);
		var saved = loanRefundRefundService.createRefund(loanRefundRefund);
		return ResponseUtil.created(loanRefundMapper.toResponse(saved), "Loan Refund successfully.");
	}

	@Operation(summary = "Get a Refund by RefundId")
	@GetMapping("/{refundId}")
	public ResponseEntity<ApiResponse<LoanfundResponse>> getRefundById(@PathVariable @Positive Long refundId) {
		var loanRefundRefund = loanRefundRefundService.getRefundById(refundId);
		return ResponseUtil.ok(loanRefundMapper.toResponse(loanRefundRefund),
				"loan refund Id : " + refundId + " retrieved successfully");
	}

	@Operation(summary = "Get Refunds by Loan Application Id")
	@GetMapping("/loan/{loanApplicationId}")
	public ResponseEntity<ApiResponse<List<LoanfundResponse>>> getRefundsByLoanApplication(
			@PathVariable @Positive Long loanApplicationId) {
		var refunds = loanRefundRefundService.getRefundsByLoanApplicationId(loanApplicationId);
		var dto = loanRefundMapper.toListResponse(refunds);
		String message = dto.isEmpty() ? "No Refund records found for Loan Application Id: " + loanApplicationId
				: "List of Refund records for Loan Application Id: " + loanApplicationId + " retrieved successfully";
		return ResponseUtil.ok(dto, message);
	}
}
