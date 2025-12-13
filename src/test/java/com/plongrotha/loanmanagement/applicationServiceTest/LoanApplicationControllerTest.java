package com.plongrotha.loanmanagement.applicationServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LoanApplicationControllerTest {

	@LocalServerPort
	private int port;

	private String baseUrl = "http://localhost";

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		baseUrl = baseUrl + ":" + port + "/api/v1/loan-applications";
	}

	@Test
	public void testCompleteApplicationFlow() {
		// Create -> Get -> Update -> Delete
		given().contentType(ContentType.JSON).when().get(baseUrl + "/1").then().statusCode(200);
	}

	@Test
	public void testCreateApplication() {
		RestAssured.baseURI = baseUrl + "/createV2";
		String requestBody = """
				               {
				  "applicationRequest": {
				    "applicantFullName": "Plong Rotha2",
				    "address": "Phnom Penh, Cambodia",
				    "email": "rotha243@gmail.com",
				    "phoneNumber": "0123456789",
				    "nationalId": "KH-123456789"
				  },
				  "loanType": "PERSONAL_LOAN",
				  "employmentStatus": "EMPLOYED",
				  "loanAmount": 90000,
				  "loanDurationInMonths": 12,
				  "loanPurpose": "Home renovation"
				}
				                """;

		given().header("Content-Type", "application/json").body(requestBody).when().post().then().statusCode(201)
				.body("code", equalTo(201))
				.body("message", equalTo("Loan application created successfully"))
				.body("data.applicationResponse.applicationId", notNullValue())
				.body("data.applicationResponse.applicantFullName", equalTo("Plong Rotha2"))
				.body("data.applicationResponse.address", equalTo("Phnom Penh, Cambodia"))
				.body("data.applicationResponse.email", equalTo("rotha243@gmail.com"))
				.body("data.applicationResponse.phoneNumber", equalTo("0123456789"))
				.body("data.applicationResponse.nationalId", equalTo("KH-123456789"));
	}

	@Test
	public void testGetLoanApplicationById() {
		given().contentType(ContentType.JSON).when().get(baseUrl + "/4").then().statusCode(HttpStatus.OK.value())
				.body("code", equalTo(200)).body("message", equalTo("Loan application retrieved successfully"))
				// Access nested fields inside "data"
				.body("data.loanApplicationId", equalTo(4)).body("data.loanType", equalTo("AUTO_LOAN"))
				.body("data.loanRefundStatus", equalTo("COMPLETED")).body("data.employmentStatus", equalTo("EMPLOYED"))
				.body("data.applicationStatus", equalTo("APPROVED")).body("data.loanAmount", equalTo(47330.0F))
				.body("data.paidAmount", equalTo(47330.0F)).body("data.applicationResponse.applicationId", equalTo(4))
				.body("data.applicationResponse.applicantFullName", equalTo("Plong Rotha"))
				.body("data.applicationResponse.address",
						equalTo("Suite 946 8149 Roy Prairie, South Madonna, NH 10148"))
				.body("data.applicationResponse.email", equalTo("jarred.padberg@yahoo.com"))
				.body("data.applicationResponse.phoneNumber", equalTo("0963471034"))
				.body("data.applicationResponse.nationalId", equalTo("KHM-u64567658"))
				.body("data.applicationResponse.createdAt", equalTo("2025-12-10T13:14:46.112361"));
	}

	@Test
	public void testGetAllLoanApplications() {
		given().contentType(ContentType.JSON).when().get(baseUrl).then().statusCode(HttpStatus.OK.value())
				.body("code", equalTo(200))
				.body("message", equalTo("Loan applications retrieved successfully"));
	}

	@Test
	public void testResponseHeaders() {
		given().contentType(ContentType.JSON).when().get(baseUrl).then().statusCode(200).header("Content-Type",
				containsString("application/json"));
	}
}
