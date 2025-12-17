package com.plongrotha.loanmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plongrotha.loanmanagement.model.LoanDocument;

@Repository
public interface LoanDocumentReporsitory extends JpaRepository<LoanDocument, Long> {
    Optional<LoanDocument> findByImageName(String name);
}
