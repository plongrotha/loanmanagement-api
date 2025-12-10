package com.plongrotha.loanmanagement.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plongrotha.loanmanagement.enums.LoanRefundStatus;
import com.plongrotha.loanmanagement.exception.LoanAmountZeroException;
import com.plongrotha.loanmanagement.exception.NotFoundException;
import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.model.LoanRefundRefund;
import com.plongrotha.loanmanagement.repository.LoanApplicationRefundRepository;
import com.plongrotha.loanmanagement.repository.LoanApplicationRepository;
import com.plongrotha.loanmanagement.service.LoanRefundRefundService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanRefundRefundServiceImpl implements LoanRefundRefundService {

	private final LoanApplicationRefundRepository refundRepository;
	private final LoanApplicationRepository loanApplicationRepository;

	// need to fix it not correct calculation
	@Override
	public LoanRefundRefund createRefund(LoanRefundRefund refund) {
		LoanApplication application = loanApplicationRepository
				.findById(refund.getLoanApplication().getLoanApplicationId())
				.orElseThrow(() -> new NotFoundException("Loan Application with id "
						+ refund.getLoanApplication().getLoanApplicationId() + " not found."));

		// List<LoanRefundRefund> refunLists = refundRepository
		// .findByLoanApplication_LoanApplicationId(application.getLoanApplicationId());
		// LoanRefundRefund latestRefund = Collections.max(refunLists,
		// Comparator.comparing(LoanRefundRefund::getUpdatedAt));

		// update paid amount in loan application
		if (application.getLoanRefundStatus() == LoanRefundStatus.COMPLETED) {
			throw new LoanAmountZeroException("Loan amount is fully paid, no refund can be processed.");
		}

		if (application.getLoanRefundStatus() != LoanRefundStatus.IN_PROGRESS) {
			throw new LoanAmountZeroException("the application not approved yet");
		}

		BigDecimal paidAmount = application.getPaidAmount() == null ? BigDecimal.ZERO : application.getPaidAmount();
		BigDecimal totalLoanAmount = application.getLoanAmount();

		// Calculate current remaining amount
		BigDecimal currentRemainAmount = totalLoanAmount.subtract(paidAmount);

		// Validate that refund amount doesn't exceed remaining amount
		if (refund.getRefundAmount().compareTo(currentRemainAmount) > 0) {
			throw new IllegalArgumentException("Refund amount (" + refund.getRefundAmount()
					+ ") cannot exceed remaining loan amount (" + currentRemainAmount + ")");
		}

		// Update the cumulative paid amount
		BigDecimal newPaidAmount = paidAmount.add(refund.getRefundAmount());
		application.setPaidAmount(newPaidAmount);

		// Set up the refund record
		LoanRefundRefund loanRefundRefund = refund;
		loanRefundRefund.setTotalLoanAmount(totalLoanAmount);
		loanRefundRefund.setPaidAmount(newPaidAmount); // This should be cumulative paid amount
		loanRefundRefund.setRemainAmount(totalLoanAmount.subtract(newPaidAmount));
		loanRefundRefund.setLoanApplication(application);

		loanRefundRefund.setRefundInitiatedDate(LocalDateTime.now());
		loanRefundRefund.setRefundRequestedDate(LocalDateTime.now());
		loanRefundRefund.setUpdatedAt(LocalDateTime.now());
		loanRefundRefund.setCreatedAt(LocalDateTime.now());

		// Check if loan is fully paid
		if (totalLoanAmount.compareTo(newPaidAmount) == 0) {
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
		if (list.isEmpty()) {
			return List.of();
		}
		return list;
	}

	private void getLoanApplicationById(Long loanApplicationId) {
		loanApplicationRepository.findById(loanApplicationId).orElseThrow(
				() -> new NotFoundException("LoanApplication id : " + loanApplicationId + " is not found."));
	}

}
