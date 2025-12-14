package com.plongrotha.loanmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LoanmanagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(LoanmanagementApplication.class, args);
	}
}
