package com.plongrotha.loanmanagement.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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

    @Column(name = "image_name")
    private String imageName;

    @Lob
    @Column(name = "image_data", length = 1000000)
    private byte[] imageData;
    private String imageType;
    private LocalDateTime uploadAt;
}
