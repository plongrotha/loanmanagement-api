package com.plongrotha.loanmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.plongrotha.loanmanagement.model.enums.ApplicationStatus;
import com.plongrotha.loanmanagement.model.enums.EmploymentStatus;
import com.plongrotha.loanmanagement.model.enums.LoanRefundStatus;
import com.plongrotha.loanmanagement.model.enums.LoanType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loan_applications")
public class LoanApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loan_application_id")
	private Long loanApplicationId;

	@Enumerated(EnumType.STRING)
	@Column(name = "loan_type")
	private LoanType loanType;

	@Enumerated(EnumType.STRING)
	private LoanRefundStatus loanRefundStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "employment_status")
	private EmploymentStatus employmentStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "application_status")
	private ApplicationStatus applicationStatus = ApplicationStatus.PENDING;

	@Column(name = "loan_amount")
	private BigDecimal loanAmount;

	@Column(name = "paid_amount")
	private BigDecimal paidAmount;

	@Column(name = "loan_duration_in_months")
	private int loanDurationInMonths;

	@JsonIgnore
	@Column(name = "interest_rate")
	private double interestRate;

	@Column(name = "loan_purpose")
	private String loanPurpose;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// add relationship to Application
	@ManyToOne
	@JoinColumn(name = "application_id")
	private Application application;

	@PrePersist
	void onCreate() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
