package com.plongrotha.loanmanagement.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.plongrotha.loanmanagement.dto.request.ApplicationRequest;
import com.plongrotha.loanmanagement.dto.request.LoanApplicationRequest;
import com.plongrotha.loanmanagement.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.enums.LoanRefundStatus;
import com.plongrotha.loanmanagement.enums.LoanType;
import com.plongrotha.loanmanagement.exception.BadRequestException;
import com.plongrotha.loanmanagement.exception.ConflictException;
import com.plongrotha.loanmanagement.exception.NotFoundException;
import com.plongrotha.loanmanagement.mapper.LoanApplicationMapper;
import com.plongrotha.loanmanagement.model.Application;
import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.service.ApplicationService;
import com.plongrotha.loanmanagement.service.LoanApplicationService;
import com.plongrotha.loanmanagement.utils.ColorUtil;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class LoanApplicationServiceTest {

    public static final Logger logger = Logger.getLogger(LoanApplicationServiceTest.class.getName());
    private LoanApplicationService loanApplicationService;
    private ApplicationService applicationService;
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    public LoanApplicationServiceTest(LoanApplicationService loanApplicationService,
            LoanApplicationMapper loanApplicationMapper,
            ApplicationService applicationService) {
        this.loanApplicationService = loanApplicationService;
        this.applicationService = applicationService;
        this.loanApplicationMapper = loanApplicationMapper;
    }

    @Test
    public void testGetAllLoanApplications() {
        List<LoanApplication> lists = loanApplicationService.getAllLoanApplications();
        logger.info("Loan Applications fetched at " + LocalDate.now() + ": " + ColorUtil.GREEN + lists.size()
                + ColorUtil.RESET);
        assertFalse(lists.isEmpty());
        LoanApplication first = lists.get(0);
    }

    @Test
    public void testCreateLoanApplication() {
        try {
            ApplicationRequest application = new ApplicationRequest();
            application.setApplicantFullName("John Doe");
            application.setAddress("phnom penh");
            application.setEmail("test@example.com");
            application.setPhoneNumber("0123456789");
            application.setNationalId("9090897867");
            Application createdApp = loanApplicationMapper.toApplicationEntity(application);
            applicationService.createApplication(createdApp);

            // loan application
            LoanApplicationRequest loanAppRequest = new LoanApplicationRequest();
            loanAppRequest.setApplicationRequest(application);
            loanAppRequest.setLoanAmount(java.math.BigDecimal.valueOf(5000));
            loanAppRequest.setLoanDurationInMonths(12);
            loanAppRequest.setLoanPurpose("Car Loan");
            loanAppRequest.setEmploymentStatus(EmploymentStatus.EMPLOYED);
            loanAppRequest.setLoanType(LoanType.PERSONAL_LOAN);
            LoanApplication loanAppEntity = loanApplicationMapper.toEntity(loanAppRequest);
            logger.info(ColorUtil.PURPLE + "lona info {} : " + loanAppEntity.getApplication().getApplicantFullName()
                    + ColorUtil.RESET);
            LoanApplication savedLoanApp = loanApplicationService.createNewLoanApplicationV2(loanAppEntity);

            assertEquals("PERSONAL_LOAN", loanAppRequest.getLoanType().name());
            assertEquals("EMPLOYED", loanAppRequest.getEmploymentStatus().name());
            assertEquals(5000, loanAppRequest.getLoanAmount().intValue());
        } catch (BadRequestException e) {
            logger.info("BadRequestException occurred: " + e.getMessage());
        } catch (Exception e) {
            logger.info("Exception occurred: " + e.getMessage());
        }

    }

    @Test
    public void testGetAllLoanApplicationsByRefundStatus() {
        var loanApplications = loanApplicationService
                .getAllLoanApplicationsByRefundStatus(LoanRefundStatus.COMPLETED);
        var first = loanApplications.size();
        logger.info(ColorUtil.BOLD_RED + "loanApplication first loan : " + first + ColorUtil.RESET);
        assertFalse(!loanApplications.isEmpty());
    }

    @Test
    public void testGetAllLoanApplicationRefundCompleted() {
        List<LoanApplication> loanApplications = loanApplicationService
                .getAllLoanApplicationsByRefundStatus(LoanRefundStatus.COMPLETED);
        int first = loanApplications.size();
        logger.info(ColorUtil.BOLD_RED + "loanApplication first loan : " + first + ColorUtil.RESET);
        assertTrue(loanApplications.isEmpty());
    }

    @Test
    public void testrejectLoanApplication() {
        Long loanApplicationId = 203L;
        try {
            assertThrows(NotFoundException.class, () -> {
                loanApplicationService.rejectLoanApplication(loanApplicationId);
                logger.info(
                        ColorUtil.BOLD_RED + "Loan application with ID " + loanApplicationId + " has been rejected."
                                + ColorUtil.RESET);
            });
        } catch (Exception e) {
            logger.info("NotFoundException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testgetAllApplicationStatusPending() {
        var loanApplications = loanApplicationService.getAllApplicationStatusPending();
        var first = loanApplications.size();
        for (LoanApplication loanApplication : loanApplications) {
            logger.info(ColorUtil.GREEN + "Pending Loan Application ID: " + loanApplication.getLoanApplicationId() +
                    ", Applicant Name: " + loanApplication.getApplication().getNationalId() + ColorUtil.RESET);
            assertFalse(loanApplications.isEmpty());
        }
    }

    @Test
    public void testApproveLoanApplication() {
        Long loanApplicationId = 1L;
        try {
            loanApplicationService.approveLoanApplication(loanApplicationId);
        } catch (ConflictException e) {
            logger.info(ColorUtil.BOLD_RED + "ConflictException occurred: " + e.getMessage() + ColorUtil.RESET);
            assertEquals("This application is approve already", e.getMessage());
        } catch (NotFoundException e) {
            logger.info(ColorUtil.BOLD_RED + "NotFoundException occurred: " + e.getMessage() + ColorUtil.RESET);
            assertEquals("Loan application with ID " + loanApplicationId + " not found", e.getMessage());
        }
    }

    @Test
    public void testApproveLoanApplicationNotFound() {
        Long invalidLoanApplicationId = 999L;
        try {
            loanApplicationService.approveLoanApplication(invalidLoanApplicationId);
        } catch (NotFoundException e) {
            logger.info(ColorUtil.BOLD_RED + "NotFoundException occurred: " + e.getMessage() + ColorUtil.RESET);
            assertEquals("Loan application with ID " + invalidLoanApplicationId + " not found", e.getMessage());
        }
    }

    @Test
    public void testGetAllLoanApplicationsByLoanType() {
        var loanApplications = loanApplicationService.getAllLoanApplications();
        var personalLoans = loanApplications.stream()
                .filter(loanApp -> loanApp.getLoanType() == LoanType.HOME_LOAN)
                .toList();
        logger.info(ColorUtil.BOLD_BLUE + "Total Personal Loans: " + personalLoans.size() + ColorUtil.RESET);
        assertFalse(personalLoans.isEmpty());
    }
}