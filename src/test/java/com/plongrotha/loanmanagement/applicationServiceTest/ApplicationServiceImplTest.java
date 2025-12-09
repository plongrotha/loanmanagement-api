package com.plongrotha.loanmanagement.applicationServiceTest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.plongrotha.loanmanagement.model.Application;
import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.repository.ApplicationRepository;
import com.plongrotha.loanmanagement.repository.LoanApplicationRepository;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @SuppressWarnings("unused")
    private Application existingApplication;

    @SuppressWarnings("unused")
    private LoanApplication existingLoanApplication;
}
