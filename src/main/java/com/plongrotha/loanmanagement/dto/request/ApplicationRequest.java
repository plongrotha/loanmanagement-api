package com.plongrotha.loanmanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequest {

	@NotEmpty(message = "full name cannot be empty")
    @NotNull(message = "Applicant full name is required")
    private String applicantFullName;

	@NotEmpty(message = " address cannot be empty")
    @NotNull(message = "Address is required")
    private String address;
    
	@NotEmpty(message = " email cannot be empty")
    @Schema(example = "@gmail.com")
    @Email(message = "Email is required")
    private String email;

	@NotEmpty(message = "phoneNumber cannot be empty")
    @NotNull(message = "Phone number is required")
    private String phoneNumber;

	@NotEmpty(message = "nationalId cannot be empty")
    @NotNull(message = "National ID is required")
    private String nationalId;

}
