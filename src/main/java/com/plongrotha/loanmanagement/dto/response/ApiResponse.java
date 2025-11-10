package com.plongrotha.loanmanagement.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int code;
    private boolean isSuccess;
    private String message;
    private T data;
    private LocalDateTime timestamp = LocalDateTime.now();
}
