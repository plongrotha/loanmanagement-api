package com.plongrotha.loanmanagement.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API Documentation - Loan Manangement API", description = "API documentation for Management Loan", version = "1.0.0", contact = @Contact(name = "plongRoth", email = "mrr.rothabetta31@gmail.com", url = "https://rothaporfolio.vercel.app/"), license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")), servers = {
		@Server(url = "http://localhost:8080", description = "Development Server"),
		@Server(url = "https://api.yourapp.com", description = "Production Server") })
public class OpenAPIConfig {

}
