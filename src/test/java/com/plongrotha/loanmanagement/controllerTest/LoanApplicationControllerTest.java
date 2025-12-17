package com.plongrotha.loanmanagement.controllerTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Ignore;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class LoanApplicationControllerTest {

	@LocalServerPort
	private int port;

	private String baseUrl = "http://localhost";

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		baseUrl = baseUrl + ":" + port + "/api/v1/loan-applications";
	}

	@AfterEach
	public void cleanUpdate() {
	}

	@Test
	public void testCompleteApplicationFlow() {
		// Create -> Get -> Update -> Delete
		given().contentType(ContentType.JSON).when().get(baseUrl + "/1").then().statusCode(200);
	}

	@Ignore
	@Test
	public void testCreateApplication() {
		RestAssured.baseURI = baseUrl + "/createV2";
		String requestBody = """
								 {
				  "applicationRequest": {
				    "applicantFullName": "Lim Sophea",
				    "address": "Street 51, Phnom Penh, Cambodia",
				    "email": "lim.sophea@gmail.com",
				    "phoneNumber": "0961234502",
				    "nationalId": "KH-100000002"
				  },
				  "loanType": "EDUCATION_LOAN",
				  "employmentStatus": "STUDENT",
				  "loanAmount": 18000,
				  "loanDurationInMonths": 24,
				  "loanPurpose": "University expenses"
				}
						""";

		given().header("Content-Type", "application/json").body(requestBody).when().post().then().statusCode(201)
				.body("code", equalTo(201))
				.body("message", equalTo("Loan application created successfully"))
				.body("data.applicationResponse.applicationId", notNullValue())
				.body("data.applicationResponse.applicantFullName", equalTo("Lim Sophea"))
				.body("data.applicationResponse.address", equalTo("Street 51, Phnom Penh, Cambodia"))
				.body("data.applicationResponse.email", equalTo("lim.sophea@gmail.com"))
				.body("data.applicationResponse.phoneNumber", equalTo("0961234502"))
				.body("data.applicationResponse.nationalId", equalTo("KH-100000002"));
	}

	// @Test
	public void testGetLoanApplicationById() {
		given().contentType(ContentType.JSON).when().get(baseUrl + "/291").then().statusCode(HttpStatus.OK.value())
				.body("code", equalTo(200)).body("message", equalTo("Loan application retrieved successfully"))
				// Access nested fields inside "data"
				.body("data.loanApplicationId", equalTo(291)).body("data.loanType", equalTo("PERSONAL_LOAN"))
				.body("data.employmentStatus", equalTo("STUDENT"))
				.body("data.applicationStatus", equalTo("PENDING")).body("data.loanAmount", equalTo(12000F))
				.body("data.applicationResponse.applicationId", equalTo(291))
				.body("data.applicationResponse.applicantFullName", equalTo("Sok Vanna"))
				.body("data.applicationResponse.address",
						equalTo("Street 63, Phnom Penh, Cambodia"))
				.body("data.applicationResponse.email", equalTo("sok.vanna@gmail.com"))
				.body("data.applicationResponse.phoneNumber", equalTo("0961234501"))
				.body("data.applicationResponse.nationalId", equalTo("KH-100000001"));
	}

	@Ignore
	@Test
	public void testRejectLoanApplication() {
		Long id = 202L;
		given().headers("Content-Type", "application/json").when().post(baseUrl + "/reject?applicationId=" + id).then()
				.statusCode(200)
				.body("message", equalTo("Loan application rejected successfully"))
				.body("success", equalTo(true));
	}

	@Ignore
	@Test
	public void testApproveLoanApplication() {
		Long id = 201L;
		given().headers("Content-Type", "application/json").when().post(baseUrl + "/approve?applicationId=" + id).then()
				.statusCode(200)
				.body("message", equalTo("Loan application approved successfully"));
	}

	// @Test
	public void TestGetAllApplicationStatuses() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/application-statuses").then()
				.statusCode(200)
				.body("message", equalTo("Application statuses retrieved successfully"))
				.body("data.size()", greaterThan(0))
				.body("data[0]", equalTo("APPROVED"))
				.body("data[1]", equalTo("PENDING"))
				.body("data[2]", equalTo("REJECTED"));
	}

	@Ignore
	@Test
	public void TestGetAllLoanTypes() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/loan-types").then().statusCode(200)
				.body("message", equalTo("Loan types retrieved successfully"))
				.body("data.size()", greaterThan(0));
	}

	@Ignore
	@Test
	public void TestgetAllEmploymentStatus() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/employment-statuses").then()
				.statusCode(200)
				.body("message", equalTo("Employment statuses retrieved successfully"))
				.body("data.size()", greaterThan(0));
	}

	@Ignore
	@Test
	public void TestgetAllLoanRecentUpdatedToday() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/recent-updated-today").then()
				.statusCode(200)
				.body("message", equalTo("loan Applications retrieved successfully."))
				.body("data.size()", is(6));
	}

	@Ignore
	@Test
	public void TestGetAllLoanApplicationRefundCompleted() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/refund-completed").then()
				.statusCode(200)
				.body("message", equalTo("loanApplicaion refund completed retrieve successfully."))
				.body("data.size()", greaterThan(0));
	}

	@Ignore
	@Test
	public void TestGetAllPendingLoanApplications() {
		given()
				.headers("Content-Type", "application/json")
				.when()
				.get(baseUrl + "/pending")
				.then()
				.statusCode(200)
				.body("message", equalTo("Pending loan applications retrieved successfully"))

				.body("data[0].loanApplicationId", equalTo(201))
				.body("data[0].loanType", equalTo("AUTO_LOAN"))
				.body("data[0].employmentStatus", equalTo("STUDENT"))
				.body("data[0].applicationStatus", equalTo("PENDING"))
				.body("data[0].loanAmount", equalTo(5928F))
				.body("data[0].paidAmount", equalTo(0.0F))
				.body("data[0].loanRefundStatus", equalTo(null))
				.body("data[0].loanPurpose",
						equalTo("Nisi cumque nihil eaque itaque occaecati libero."))
				.body("data[0].loanDurationInMonths", equalTo(8))
				.body("data[0].updatedAt", equalTo("2025-12-14T15:28:31.363135"))
				// nested object
				.body("data[0].applicationResponse.applicationId", equalTo(201))
				.body("data[0].applicationResponse.applicantFullName", equalTo("Hosea Kihn"))
				.body("data[0].applicationResponse.address",
						equalTo("7604 Julianna Pass, North Tadville, CA 36965-1205"))
				.body("data[0].applicationResponse.email", equalTo("virgilio.hackett@hotmail.com"))
				.body("data[0].applicationResponse.phoneNumber", equalTo("766-108-2298"))
				.body("data[0].applicationResponse.nationalId", equalTo("NID-48754533"))
				.body("data.size()", greaterThan(0));
	}

	@Ignore
	@Test
	public void TestGetAllLoanApplicationByRefundStatus() {
		String refundStatus = "?refundStatus=COMPLETED";
		given().header("Content-Type", "application/json").when()
				.get(baseUrl + "/by-refund-status" + refundStatus).then()
				.statusCode(200)
				.body("message", equalTo("loanApplicaion retrieve successfully."))
				.body("data.size()", greaterThan(0));
	}

	@Ignore
	@Test
	public void TestGetLoanApplicationByApplicationStatus() {
		String status = "?status=PENDING";
		given().header("Content-Type", "application/json").when().get(baseUrl + "/by-status" + status).then()
				.statusCode(200)
				.body("message", equalTo("Loan applications retrieved successfully"))
				.body("data.size()", greaterThan(0));
	}

	@Ignore
	@Test
	public void TestFetLoanApplicationByNationalID() {
		String nationalId = "?nationalId=NID-16513905";
		given().header("Content-Type", "application/json").when().get(baseUrl + "/by-national-id" + nationalId).then()
				.statusCode(200)
				.body("message", equalTo("Loan application retrieved successfully"));
	}

	@Ignore
	@Test
	public void TestGetLoanApplicationsByLoanType() {
		String type = "?loanType=PERSONAL_LOAN";
		given().header("Content-Type", "application/json").when().get(baseUrl + "/by-loan-type" + type).then()
				.statusCode(200)
				.body("message", equalTo("Loan applications retrieved successfully"))
				.body("data.size()", greaterThan(0));
	}

	@Ignore
	@Test
	public void TestGetAllLoanApplicationRefundInProgressPagination() {
		given().contentType(ContentType.JSON).when().get(baseUrl + "/refund-in-progress?page=0&size=10").then()
				.statusCode(HttpStatus.OK.value()).body("code", equalTo(200)).header("Content-Type",
						containsString("application/json"))
				.body("message", equalTo("loanApplicaion retrieve successfully."));
	}

	// ------------------------------------------------------

	@Ignore
	@Test
	public void testResponseHeadersTest() {
		given().contentType(ContentType.JSON).when().get(baseUrl).then().statusCode(200).header("Content-Type",
				containsString("application/json"));
	}

	@Ignore
	@Test
	public void getAllLoanApplicationPaginationTest() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/page?page=0&size=30").then()
				.statusCode(200)
				.body("message", equalTo("loanApplicaion retrieve successfully."))
				.body("data.size()", greaterThan(0));
	}

	@Ignore
	@Test
	public void getAllEmploymentStatusTest() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/employment-statuses").then()
				.statusCode(200)
				.body("message", equalTo("Employment statuses retrieved successfully"))
				.body("data.size()", greaterThan(0))
				.body("data[0]", equalTo("EMPLOYED"))
				.body("data[1]", equalTo("SELF_EMPLOYED"))
				.body("data[2]", equalTo("UNEMPLOYED"))
				.body("data[3]", equalTo("STUDENT"))
				.body("data[4]", equalTo("RETIRED"))
				.body("success", equalTo(true));
	}

	@Ignore
	@Test
	public void createLoanApplication() {
		String requestBody = """
							 {
				  "applicationRequest": {
				    "applicantFullName": "Phan Rotha",
				    "address": "Sen Sok, Phnom Penh, Cambodia",
				    "email": "phan.rotha@gmail.com",
				    "phoneNumber": "0961234507",
				    "nationalId": "KH-100000007"
				  },
				  "loanType": "BUSINESS_LOAN",
				  "employmentStatus": "SELF_EMPLOYED",
				  "loanAmount": 45000,
				  "loanDurationInMonths": 48,
				  "loanPurpose": "Expand retail shop"
				}
						""";
		RestAssured.baseURI = baseUrl;
		given().when().header("Content-Type", "application/json").body(requestBody).when().post(baseUrl).then()
				.statusCode(201)
				.body("message", equalTo("Loan application created successfully"));
	}

	@Ignore
	@Test
	public void getLoanApplicationsByEmploymentStatusTest() {
		String emstatus = "employmentStatus=STUDENT_LOAN";
		given().header("Content-Type", "application/json").when().get(baseUrl + "/by-employment-status?" + emstatus)
				.then()
				.statusCode(200)
				.body("message", equalTo("No loan applications found for the specified employment status"));
		// .body("data[0].loanType", equalTo("STUDENT_LOAN"))
		// .body("data.size()", greaterThan(0));
	}

}