package com.plongrotha.loanmanagement.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loan_documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;
    private String fileName;
    private byte[] fileData;
    private String fileType;
    private LocalDateTime uploadAt;
    private String fileUrl;

    // @JsonIgnore
    // @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    // @JoinColumn(name = "loan_application_id")
    // private LoanApplication loanApplication;
}
