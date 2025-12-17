package com.plongrotha.loanmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.plongrotha.loanmanagement.model.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Application a WHERE a.nationalId = ?1")
    boolean exiexistsByNationalId(String nationalId);

    // @Query("SELECT a FROM Application a WHERE a.nationalId = ?1")
    // Application findByNationalId(String nationalId);
}
