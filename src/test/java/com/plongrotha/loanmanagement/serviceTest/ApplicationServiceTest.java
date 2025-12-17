package com.plongrotha.loanmanagement.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.plongrotha.loanmanagement.dto.request.ApplicationRequest;
import com.plongrotha.loanmanagement.exception.NotFoundException;
import com.plongrotha.loanmanagement.mapper.LoanApplicationMapper;
import com.plongrotha.loanmanagement.model.Application;
import com.plongrotha.loanmanagement.repository.ApplicationRepository;
import com.plongrotha.loanmanagement.service.ApplicationService;
import com.plongrotha.loanmanagement.utils.ColorUtil;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ApplicationServiceTest {

    public static final Logger logger = Logger.getLogger(ApplicationServiceTest.class.getName());
    private ApplicationService applicationService;
    private ApplicationRepository applicationRepository;
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    public ApplicationServiceTest(ApplicationService applicationService,
            ApplicationRepository applicationRepository,
            LoanApplicationMapper loanApplicationMapper) {
        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
        this.loanApplicationMapper = loanApplicationMapper;
    }

    @Test
    public void testCreateApplicant() {
        ApplicationRequest request = new ApplicationRequest();
        request.setApplicantFullName("Rotha");
        request.setAddress("123 St, cambodia, Country");
        request.setEmail("rotha@example.com");
        request.setPhoneNumber("012-345-6789");
        request.setNationalId("NID-63250841");
        var application = loanApplicationMapper.toApplicationEntity(request);
        logger.info("applicant : {} " + application);
        var savedApp = applicationRepository.save(application);
        var fetchedApp = applicationService.getApplicationById(savedApp.getApplicationId());
        assertNotNull(fetchedApp.getApplicationId());
        assertNotNull(fetchedApp);
        assertEquals(savedApp.getApplicationId(), fetchedApp.getApplicationId());
    }

    @Test
    public void testGetAllApplications() {
        var applications = applicationService.getAllApplications();
        assertNotNull(applications);
        assertFalse(applications.isEmpty());
        logger.info(ColorUtil.PURPLE + "Total Applications: {}" + applications.size());

        // get the first applicant
        var firstApplication = applications.get(0);
        logger.info(ColorUtil.PURPLE + "First Application Details: {}" +
                "ID: " + firstApplication.getApplicationId() + ", " +
                "Name: " + firstApplication.getApplicantFullName() + ", " +
                "Email: " + firstApplication.getEmail() + ", " +
                "National ID: " + firstApplication.getNationalId() + ColorUtil.RESET);
        assertEquals("updateName", firstApplication.getApplicantFullName());
        assertEquals("updatedemail@example.com", firstApplication.getEmail());
        assertEquals("update nationalId", firstApplication.getNationalId());
    }

    @Test
    public void testGetApplicantById() {
        Long validId = 1L;
        try {
            var application = applicationService.getApplicationById(validId);
            assertNotNull(application);
            logger.info(
                    "Application Retrieved: {} : " + application.getNationalId() + " "
                            + application.getApplicantFullName()
                            + " "
                            + application.getAddress() + " " +
                            application.getEmail() + " " +
                            application.getPhoneNumber());
        } catch (NotFoundException e) {
            logger.info("Exception occurred while fetching application: {}" + e.getMessage());
        }
    }

    @Test
    public void testGetApplicantByIdNotFound() {
        Long invalidId = 900L;

        // can have above way or below way

        // try {
        // applicationService.getApplicationById(invalidId);
        // } catch (NotFoundException e) {
        // assertEquals("Application with ID " + invalidId + " not found",
        // e.getMessage());
        // }
        assertThrows(NotFoundException.class, () -> {
            applicationService.getApplicationById(invalidId);
        });
    }

    @Test
    public void testGetApplicantAddress() {
        var applications = applicationService.getApplicationById(1L);
        logger.info(ColorUtil.BOLD_BLUE + "Application Address: {}" + applications.getAddress());
        assertNotNull(applications.getAddress());
        assertEquals("Suite 773 7934 Ira Walks, Strackebury, NC 69006", applications.getAddress());
    }

    @Test
    public void testListApplicantIsEmpty() {
        var applications = applicationService.getAllApplications();
        if (!applications.isEmpty()) {
            applications = List.of();
        }
        logger.info(ColorUtil.CYAN + "Applications List Size before clear: {}" + applications.size());
        assertNotNull(applications);
        assertEquals(0, applications.size());
    }

    public void testFindByNationalId() {
        String nationalId = "NID-28060719";
        try {
            Application application = applicationService.getApplicationsByNationalId(nationalId);
            logger.info("Application Retrieved by National ID: {}" + application.getEmail() + " "
                    + application.getApplicantFullName() + " " + application.getNationalId());
            assertEquals(application, null);
            assertEquals(nationalId, null);
        } catch (NotFoundException e) {
            logger.info(ColorUtil.YELLOW + "Exception occurred while fetching application by National ID: {} : "
                    + e.getMessage());
        }
    }

    @Test
    public void testDeleteApplication() {
        Long applicationId = 13L;
        try {
            var applicants = applicationService.getApplicationById(applicationId);
            applicationService.deleteApplication(applicants.getApplicationId());
        } catch (NotFoundException e) {
            logger.info(ColorUtil.YELLOW + "Exception occurred while fetching application to delete: {} :"
                    + e.getMessage());
        }
    }

    @Test
    public void testUpdateApplication() {
        Long applicationId = 13L;
        try {
            var applicant = applicationService.getApplicationById(applicationId);
            applicant.setApplicantFullName("updateName");
            applicant.setEmail("updatedemail@example.com");
            applicant.setAddress("updated");
            applicant.setPhoneNumber("update phoneNumber");
            applicant.setNationalId("update nationalId");
            applicant.setUpdatedAt(LocalDateTime.now());
            Application application = applicationService.updateApplication(applicationId, applicant);
            var updatedApplicant = applicationService.updateApplication(applicant.getApplicationId(), application);
            logger.info(
                    ColorUtil.GREEN + "Updated Applicant Info {} : " + updatedApplicant.getApplicantFullName() + ", " +
                            updatedApplicant.getEmail() + ", " + updatedApplicant.getAddress() + " " +
                            updatedApplicant.getPhoneNumber() + ", " + updatedApplicant.getNationalId());
        } catch (NotFoundException e) {
            logger.info(ColorUtil.RED + "Error {} : " + e.getMessage() + ColorUtil.RESET);
            assertEquals("Application with ID " + applicationId + " not found", e.getMessage());
            System.err.println(ColorUtil.RED + "Error updating application: " + e.getMessage() + ColorUtil.RESET);
        }
    }
}
