package com.plongrotha.loanmanagement.service.impl;

import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.plongrotha.loanmanagement.enums.LoanRefundStatus;
import com.plongrotha.loanmanagement.exception.NotFoundException;
import com.plongrotha.loanmanagement.model.Application;
import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.repository.ApplicationRepository;
import com.plongrotha.loanmanagement.repository.LoanApplicationRepository;
import com.plongrotha.loanmanagement.service.ApplicationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final CacheManager cacheManager;

    @Override
    public Application createApplication(Application application) {
        return applicationRepository.save(application);
    }

    // create for test purpose
    @Override
    public Application getApplicationsByNationalId(String nationalId) {
        return null;
    }

    @Override
    public Application updateApplication(Long applicationId, Application application) {
        Application existingApplication = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application with ID " + applicationId + " not found"));
        existingApplication.setAddress(application.getAddress());
        existingApplication.setPhoneNumber(application.getPhoneNumber());
        existingApplication.setNationalId(application.getNationalId());
        existingApplication.setEmail(application.getEmail());
        existingApplication.setApplicantFullName(application.getApplicantFullName());
        applicationRepository.save(existingApplication);
        return existingApplication;
    }

    // @Cacheable(value = "applicant", key = "#applicationId")
    @Override
    public Application getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application with ID " + applicationId + " not found"));
    }

    @CacheEvict(value = "applicant", key = "#applicationId")
    @Override
    public void deleteApplication(Long applicationId) {
        boolean hasByAppicationId = loanApplicationRepository.existsByApplicationApplicationId(applicationId);
        LoanApplication loanApplication = loanApplicationRepository.findByApplicationId(applicationId);
        if (hasByAppicationId) {
            if (loanApplication.getLoanRefundStatus() == LoanRefundStatus.IN_PROGRESS) {
                throw new IllegalArgumentException("this application is not yet refund completed");
            }
            loanApplicationRepository.deleteById(applicationId);
        }

        // when loan
        Application existingApplication = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application with ID " + applicationId + " not found"));
        applicationRepository.delete(existingApplication);
    }

    // list all the Application to the client
    @Override
    public List<Application> getAllApplications() {
        List<Application> applications = applicationRepository.findAll();
        if (!applications.isEmpty()) {
            return applications;
        }
        return List.of();
    }
}
