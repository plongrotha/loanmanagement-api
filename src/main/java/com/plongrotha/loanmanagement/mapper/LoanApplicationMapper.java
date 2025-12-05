package com.plongrotha.loanmanagement.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.plongrotha.loanmanagement.dto.request.ApplicationRequest;
import com.plongrotha.loanmanagement.dto.request.LoanApplicationRequest;
import com.plongrotha.loanmanagement.dto.response.ApplicationResponse;
import com.plongrotha.loanmanagement.dto.response.LoanApplicationResponse;
import com.plongrotha.loanmanagement.dto.response.PaginationDTO;
import com.plongrotha.loanmanagement.model.Application;
import com.plongrotha.loanmanagement.model.LoanApplication;

@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {

    @Mapping(target = "loanApplicationId", ignore = true)
    @Mapping(target = "applicationStatus", ignore = true)
    @Mapping(target = "interestRate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "application", source = "request.applicationRequest")
    @Mapping(target = "paidAmount", ignore = true)
    @Mapping(target = "loanRefundStatus", ignore = true)
    LoanApplication toEntity(LoanApplicationRequest request);

    // applicationResponse
    @Mapping(target = "applicationResponse", source = "loanApplication.application")
    LoanApplicationResponse toResponse(LoanApplication loanApplication);

    List<LoanApplicationResponse> toResponseList(List<LoanApplication> loanApplications);

    // applicationId
    @Mapping(target = "applicationId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "loanApplications", ignore = true)
    Application toApplicationEntity(ApplicationRequest request);

    ApplicationResponse toApplicationResponse(Application application);

    List<ApplicationResponse> toApplicationResponseList(List<Application> applications);

    default LoanApplication fromId(Long id) {
        if (id == null)
            return null;
        LoanApplication app = new LoanApplication();
        app.setLoanApplicationId(id);
        return app;
    }

    default PaginationDTO<LoanApplicationResponse> toPaginationResponse(PaginationDTO<LoanApplication> paginationDTO) {
        if (paginationDTO == null) {
            return null;
        }
        return PaginationDTO.<LoanApplicationResponse>builder()
                .content(toResponseList(paginationDTO.getContent())) // Map the content
                .pageSize(paginationDTO.getPageSize())
                .pageNumber(paginationDTO.getPageNumber())
                .totalPages(paginationDTO.getTotalPages())
                .totalElements(paginationDTO.getTotalElements())
                .numberOfElements(paginationDTO.getNumberOfElements())
                .last(paginationDTO.isLast())
                .first(paginationDTO.isFirst())
                .empty(paginationDTO.isEmpty())
                .build();
    }

}
