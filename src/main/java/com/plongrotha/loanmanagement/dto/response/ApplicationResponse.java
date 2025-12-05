package com.plongrotha.loanmanagement.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    private Long applicationId;
    private String applicantFullName;
    private String address;
    private String email;
    private String phoneNumber;
    private String nationalId;
    private LocalDateTime createdAt;	
    private LocalDateTime updatedAt;
}
