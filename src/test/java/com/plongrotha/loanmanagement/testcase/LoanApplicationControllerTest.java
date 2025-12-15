package com.plongrotha.loanmanagement.testcase;

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
	public void testCompleteApplicationFlowTest() {
		// Create -> Get -> Update -> Delete
		given().contentType(ContentType.JSON).when().get(baseUrl + "/1").then().statusCode(200);
	}

	@Test
	public void testCreateApplicationTest() {
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
	public void testGetLoanApplicationByIdTest() {
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
	public void testGetAllLoanApplicationsTest() {
		given().contentType(ContentType.JSON).when().get(baseUrl).then().statusCode(HttpStatus.OK.value())
				.body("code", equalTo(200))
				.body("message", equalTo("No loan applications found"));
	}

	@Test
	public void testResponseHeadersTest() {
		given().contentType(ContentType.JSON).when().get(baseUrl).then().statusCode(200).header("Content-Type",
				containsString("application/json"));
	}

	@Test
	public void testGetAllLoanApplicationPaginationTest() {
		given().contentType(ContentType.JSON).when().get(baseUrl + "/refund-in-progress?page=0&size=10").then()
				.statusCode(HttpStatus.OK.value()).body("code", equalTo(200)).header("Content-Type",
						containsString("application/json"))
				.body("message", equalTo("loanApplicaion retrieve successfully."));

	}

	@Test
	public void getAllLoanApplicationPaginationTest() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/page?page=0&size=30").then()
				.statusCode(200)
				.body("message", equalTo("loanApplicaion retrieve successfully."))
				.body("data.size()", greaterThan(0));
	}

	@Test
	public void getAllPendingLoanApplicationsTest() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/pending").then().statusCode(200)
				.body("message", equalTo("Pending loan applications retrieved successfully"))
				.body("data[0].loanApplicationId", equalTo(28))
				.body("data[0].loanType", equalTo("HOME_LOAN"))
				.body("data[0].employmentStatus", equalTo("EMPLOYED"))
				.body("data[0].applicationStatus", equalTo("PENDING"))
				.body("data[0].loanAmount", equalTo(45198.0F))
				.body("data[0].paidAmount", equalTo(0.0F))
				.body("data[0].loanPurpose", equalTo("Vel aut et delectus rerum laboriosam sit assumenda sapiente."))
				.body("data[0].loanDurationInMonths", equalTo(27))
				.body("data[0].updatedAt", equalTo("2025-12-10T13:14:46.746306"))
				.body("data.size()", greaterThan(0));
	}

	@Test
	public void getLoanApplicationsByLoanTypeTest() {
		String type = "?loanType=PERSONAL_LOAN";
		given().header("Content-Type", "application/json").when().get(baseUrl + "/by-loan-type" + type).then()
				.statusCode(200)
				.body("message", equalTo("Loan applications retrieved successfully"))
				.body("data.size()", greaterThan(0));
	}

	@Test
	public void getAllLoanTypeTest() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/loan-types").then().statusCode(200)
				.body("message", equalTo("Loan types retrieved successfully"))
				.body("data.size()", greaterThan(0));
	}

	// refund-completed
	@Test
	public void getAllLoanApplicationRefundCompletedTest() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/refund-completed").then()
				.statusCode(200)
				.body("message", equalTo("loanApplicaion refund completed retrieve successfully."))
				.body("data.size()", greaterThan(0));
	}

	@Test
	public void getAllEmploymentTest() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/employment-statuses").then()
				.statusCode(200)
				.body("message", equalTo("Employment statuses retrieved successfully"))
				.body("data.size()", greaterThan(0));
	}

	@Test
	public void approveLoanApplicationTest() {
		Long id = 71L;
		given().headers("Content-Type", "application/json").when().post(baseUrl + "/approve?applicationId=" + id).then()
				.statusCode(200)
				.body("message", equalTo("Loan application approved successfully"));
	}

	@Test
	public void getLoanApplicationByEmploymentStatusTest() {
		String status = "?status=APPROVED";
		given().header("Content-Type", "application/json").when().get(baseUrl + "/by-status" + status).then()
				.statusCode(200)
				.body("message", equalTo("Loan applications retrieved successfully"))
				.body("data.size()", greaterThan(0));
	}

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

	@Test
	public void rejectLoanApplicationTest() {
		Long id = 27L;
		given().headers("Content-Type", "application/json").when().post(baseUrl + "/reject?applicationId=" + id).then()
				.statusCode(200)
				.body("message", equalTo("Loan application rejected successfully"))
				.body("success", equalTo(true));
	}

	@Test
	public void createLoanApplication() {
		String requestBody = """
						{
				  "applicationRequest": {
				    "applicantFullName": "Sok Dara",
				    "address": "Phnom Penh, Cambodia",
				    "email": "sok.dara@gmail.com",
				    "phoneNumber": "012345678",
				    "nationalId": "NID123456789"
				  },
				  "loanType": "PERSONAL_LOAN",
				  "employmentStatus": "EMPLOYED",
				  "loanAmount": 5000,
				  "loanDurationInMonths": 24,
				  "loanPurpose": "Home renovation"
				}
				""";
		RestAssured.baseURI = baseUrl;
		given().when().header("Content-Type", "application/json").body(requestBody).when().post(baseUrl).then()
				.statusCode(201)
				.body("message", equalTo("Loan application created successfully"));
	}

	@Test
	public void getLoanApplicationsByEmploymentStatusTest() {
		String emstatus = "employmentStatus=SELF_EMPLOYED";
		given().header("Content-Type", "application/json").when().get(baseUrl + "/by-employment-status?" + emstatus)
				.then()
				.statusCode(200)
				.body("message", equalTo("No loan applications found for the specified employment status"));
		// .body("data[0].loanType", equalTo("STUDENT_LOAN"))
		// .body("data.size()", greaterThan(0));
	}

	@Test
	public void getAllApplicationStatusesTest() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/application-statuses").then()
				.statusCode(200)
				.body("message", equalTo("Application statuses retrieved successfully"))
				.body("data.size()", greaterThan(0))
				.body("data[0]", equalTo("APPROVED"))
				.body("data[1]", equalTo("PENDING"))
				.body("data[2]", equalTo("REJECTED"));
	}

	@Test
	public void getAllLoanApplicationByRefundStatusTest() {
		String refundStatus = "?refundStatus=IN_PROGRESS";
		given().header("Content-Type", "application/json").when()
				.get(baseUrl + "/by-refund-status" + refundStatus).then()
				.statusCode(200)
				.body("message", equalTo("loanApplicaion retrieve successfully."))
				.body("data.size()", greaterThan(0));
	}

	@Test
	public void getAllLoanRecentUpdatedTodayTest() {
		given().headers("Content-Type", "application/json").when().get(baseUrl + "/recent-updated-today").then()
				.statusCode(200)
				.body("message", equalTo("loan Applications retrieved successfully."))
				.body("data.size()", is(0));
	}
}