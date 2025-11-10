package com.plongrotha.loanmanagement.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.plongrotha.loanmanagement.dto.request.LoanApplicationRequest;
import com.plongrotha.loanmanagement.dto.response.LoanApplicationResponse;
import com.plongrotha.loanmanagement.model.LoanApplication;

@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {

    @Mapping(target = "applicationId", ignore = true)
    @Mapping(target = "applicationStatus", ignore = true)
    LoanApplication toEntity(LoanApplicationRequest request);

    LoanApplicationResponse toResponse(LoanApplication loanApplication);

    List<LoanApplicationResponse> toResponseList(List<LoanApplication> loanApplications);

}
