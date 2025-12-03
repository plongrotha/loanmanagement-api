package com.plongrotha.loanmanagement.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.plongrotha.loanmanagement.dto.request.LoanRefundRequest;
import com.plongrotha.loanmanagement.dto.response.LoanfundResponse;
import com.plongrotha.loanmanagement.model.LoanApplication;
import com.plongrotha.loanmanagement.model.LoanRefundRefund;

@Mapper(componentModel = "spring", uses = { LoanApplicationMapper.class })
public interface LoanRefundMapper {

	@Mapping(source = "loanRefundId", target = "loanRefundId")
	@Mapping(source = "refundAmount", target = "refundAmount")
	@Mapping(source = "refundRequestedDate", target = "refundRequestedDate")
	@Mapping(source = "refundInitiatedDate", target = "refundInitiatedDate")
	@Mapping(source = "refundReadyDate", target = "refundReadyDate")
	@Mapping(source = "refundCompletedDate", target = "refundCompletedDate")
	@Mapping(source = "loanApplication.loanApplicationId", target = "loanApplicationId")
	LoanfundResponse toResponse(LoanRefundRefund loanRefundRefund);

	List<LoanfundResponse> toListResponse(List<LoanRefundRefund> list);

	@Mapping(target = "loanRefundId", ignore = true)
	@Mapping(target = "loanApplication", expression = "java(mapLoanApplicationId(loanRefundRefundRequest.getLoanApplicationId()))")
	@Mapping(target = "refundRequestedDate", expression = "java(java.time.LocalDateTime.now())")
	@Mapping(target = "refundInitiatedDate", ignore = true)
	@Mapping(target = "refundReadyDate", ignore = true)
	@Mapping(target = "remainAmount", ignore = true)
	@Mapping(target = "refundCompletedDate", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	LoanRefundRefund toEntity(LoanRefundRequest loanRefundRefundRequest);

	default LoanApplication mapLoanApplicationId(Long loanApplicationId) {
		if (loanApplicationId == null)
			return null;
		LoanApplication application = new LoanApplication();
		application.setLoanApplicationId(loanApplicationId);
		return application;
	}
}
