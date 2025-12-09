package com.plongrotha.loanmanagement.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plongrotha.loanmanagement.exception.LoanAmountZeroException;
import com.plongrotha.loanmanagement.exception.NotFoundException;
import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.model.LoanRefundRefund;
import com.plongrotha.loanmanagement.model.enums.LoanRefundStatus;
import com.plongrotha.loanmanagement.repository.LoanApplicationRefundRepository;
import com.plongrotha.loanmanagement.repository.LoanApplicationRepository;
import com.plongrotha.loanmanagement.service.LoanRefundRefundService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanRefundRefundServiceImpl implements LoanRefundRefundService {

	private final LoanApplicationRefundRepository refundRepository;
	private final LoanApplicationRepository loanApplicationRepository;

	@Override
	public LoanRefundRefund createRefund(LoanRefundRefund refund) {
		LoanApplication application = loanApplicationRepository
				.findById(refund.getLoanApplication().getLoanApplicationId())
				.orElseThrow(() -> new NotFoundException("Loan Application with id "
						+ refund.getLoanApplication().getLoanApplicationId() + " not found."));

		// update paid amount in loan application
		if (application.getLoanRefundStatus() == LoanRefundStatus.COMPLETED) {
			throw new LoanAmountZeroException("Loan amount is fully paid, no refund can be processed.");
		}

		if (application.getLoanRefundStatus() != LoanRefundStatus.IN_PROGRESS) {
			throw new LoanAmountZeroException("the application not approved yet");
		}

		BigDecimal paidAmount = application.getPaidAmount() == null ? BigDecimal.ZERO : application.getPaidAmount();

		// and the i set the new value when deptor refund the money back to the owner
		application.setPaidAmount(paidAmount.add(refund.getRefundAmount()));

		LoanRefundRefund loanRefundRefund = refund;
		loanRefundRefund.setTotalLoanAmount(application.getLoanAmount());
		loanRefundRefund.setPaidAmount(refund.getRefundAmount());

		loanRefundRefund
				.setRemainAmount(loanRefundRefund.getTotalLoanAmount().subtract(loanRefundRefund.getPaidAmount()));
		loanRefundRefund.setLoanApplication(application);

		loanRefundRefund.setRefundInitiatedDate(LocalDateTime.now());
		loanRefundRefund.setRefundRequestedDate(LocalDateTime.now());
		loanRefundRefund.setUpdatedAt(LocalDateTime.now());
		loanRefundRefund.setCreatedAt(LocalDateTime.now());

		if (application.getLoanAmount().compareTo(application.getPaidAmount()) == 0) {
			application.setLoanRefundStatus(LoanRefundStatus.COMPLETED);
			loanRefundRefund.setRefundCompletedDate(LocalDateTime.now());
		}
		loanApplicationRepository.save(application);
		return refundRepository.save(loanRefundRefund);
	}

	@Override
	public LoanRefundRefund getRefundById(Long id) {
		LoanRefundRefund refund = refundRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("loan refund is not found"));
		return refund;
	}

	@Override
	public List<LoanRefundRefund> getRefundsByLoanApplicationId(Long loanApplicationId) {
		getLoanApplicationById(loanApplicationId);
		List<LoanRefundRefund> loanRefundRefunds = refundRepository
				.findByLoanApplication_LoanApplicationId(loanApplicationId);
		if (!loanRefundRefunds.isEmpty()) {
			return loanRefundRefunds;
		}
		return List.of();
	}

	@Override
	public LoanRefundRefund updateRefund(LoanRefundRefund refund) {
		return refundRepository.save(refund);
	}

	@Override
	public void deleteRefund(Long id) {
		refundRepository.deleteById(id);
	}

	@Override
	public List<LoanRefundRefund> getAllLoans() {
		List<LoanRefundRefund> list = refundRepository.findAll();
		if (list.isEmpty())
			throw new NotFoundException("no have loanApplicatoins in database.");
		return list;
	}

	private void getLoanApplicationById(Long loanApplicationId) {
		loanApplicationRepository.findById(loanApplicationId).orElseThrow(
				() -> new NotFoundException("LoanApplication id : " + loanApplicationId + " is not found."));
	}

}
