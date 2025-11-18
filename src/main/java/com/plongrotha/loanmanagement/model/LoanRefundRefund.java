package com.plongrotha.loanmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loan_application_refunds ")
public class LoanRefundRefund {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loan_refund_id")
	private Long loanRefundId;

	@ManyToOne
	@JoinColumn(name = "loan_application_id")
	private LoanApplication loanApplication;

	@Column(name = "refund_amount")
	private BigDecimal refundAmount;
	
	@Column(name = "total_paid_amount")
	private BigDecimal totalPaidAmount;

	@Column(name = "refund_requested_date")
	private LocalDateTime refundRequestedDate;

	@Column(name = "refund_initiated_date")
	private LocalDateTime refundInitiatedDate;

	@Column(name = "refund_ready_date")
	private LocalDateTime refundReadyDate;

	@Column(name = "refund_completed_date")
	private LocalDateTime refundCompletedDate;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist
	void Oncreate() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	void Onupdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
