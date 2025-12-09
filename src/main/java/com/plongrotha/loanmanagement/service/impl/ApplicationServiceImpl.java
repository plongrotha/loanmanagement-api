package com.plongrotha.loanmanagement.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.plongrotha.loanmanagement.exception.NotFoundException;
import com.plongrotha.loanmanagement.model.Application;
import com.plongrotha.loanmanagement.repository.ApplicationRepository;
import com.plongrotha.loanmanagement.repository.LoanApplicationRepository;
import com.plongrotha.loanmanagement.service.ApplicationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    @Override
    public Application updateApplication(Long applicationId, Application application) {
        Application existingApplication = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application with ID " + applicationId + " not found"));
        existingApplication.setAddress(application.getAddress());
        existingApplication.setPhoneNumber(application.getPhoneNumber());
        existingApplication.setNationalId(application.getNationalId());
        existingApplication.setApplicantFullName(application.getApplicantFullName());
        applicationRepository.save(existingApplication);
        return existingApplication;
    }

    @Override
    public Application getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application with ID " + applicationId + " not found"));
    }

    @Override
    public void deleteApplication(Long applicationId) {
        boolean hasByAppicationId = loanApplicationRepository.existsByApplicationApplicationId(applicationId);
        if (hasByAppicationId) {
            loanApplicationRepository.deleteById(applicationId);
        }
        Application existingApplication = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application with ID " + applicationId + " not found"));
        applicationRepository.delete(existingApplication);
    }

    @Override
    public List<Application> getAllApplications() {
        List<Application> applications = applicationRepository.findAll();
        if (!applications.isEmpty()) {
            return applications;
        }
        return List.of();
    }
}
