package com.plongrotha.loanmanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plongrotha.loanmanagement.dto.request.ApplicationRequest;
import com.plongrotha.loanmanagement.dto.response.ApiResponse;
import com.plongrotha.loanmanagement.dto.response.ApplicationResponse;
import com.plongrotha.loanmanagement.mapper.LoanApplicationMapper;
import com.plongrotha.loanmanagement.service.ApplicationService;
import com.plongrotha.loanmanagement.utils.ResponseUtil;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@CrossOrigin
public class ApplicationController {

    private final ApplicationService applicationService;
    private final LoanApplicationMapper loanApplicationMapper;

    @Operation(summary = "Delete application by ID")
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<Void>> deleteApplication(@PathVariable @Positive Long applicationId) {
        applicationService.deleteApplication(applicationId);
        return ResponseUtil.success("Application deleted successfully");
    }

    @Operation(summary = "Get application by ID")
    @GetMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<ApplicationResponse>> getApplicationById(
            @PathVariable @Positive Long applicationId) {
        var app = applicationService.getApplicationById(applicationId);
        var dto = loanApplicationMapper.toApplicationResponse(app);
        return ResponseUtil.success(dto, "Application retrieved successfully");
    }

    @Operation(summary = "Update application by ID")
    @PutMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<ApplicationResponse>> updateApplication(
            @PathVariable @Positive Long applicationId,
            @RequestBody ApplicationRequest request) {
        var application = loanApplicationMapper.toApplicationEntity(request);
        var dto = applicationService.updateApplication(applicationId, application);
        return ResponseUtil.success(loanApplicationMapper.toApplicationResponse(dto),
                "Application updated successfully");
    }

    @Operation(summary = "Get all applications")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getAllApplications() {
        var applications = applicationService.getAllApplications();
        var dto = loanApplicationMapper.toApplicationResponseList(applications);
        String message = dto.isEmpty() ? "No applications found" : "Applications retrieved successfully";
        return ResponseUtil.success(dto, message);
    }
}