package com.plongrotha.loanmanagement.service;

import java.util.List;

import com.plongrotha.loanmanagement.model.Application;

public interface ApplicationService {

    Application updateApplication(Long applicationId, Application application);

    Application getApplicationById(Long applicationId);

    void deleteApplication(Long applicationId);

    List<Application> getAllApplications();

    Application createApplication(Application application);

    Application getApplicationsByNationalId(String nationalId);

}
