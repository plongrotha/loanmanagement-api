package com.plongrotha.loanmanagement.applicationServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoanApplicationControllerTest {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl + ":" + port + "/api/v1/applications";
        RestAssured.port = port;
    }

    @Test
    public void testGetAllLoanApplcation() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl)
                .then()
                .statusCode(200)
                .body("[0].applicantFullName", equalTo("Todd Dicki"));
    }

    @Test
    public void testGetStudentById() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl + "/1")
                .then()
                .statusCode(200)
                // .body("applicationId", equalTo(1))
                .body("applicantFullName", equalTo("Todd Dicki"))
                .body("email", equalTo("waylon.bins@gmail.com"))
                .body("phoneNumber", equalTo("751.391.1365"));
    }

    // {
    // "applicationId":1,"applicantFullName":"Todd Dicki","address":"Apt. 825 496
    // Hickle Divide, North Chelsieburgh, IA
    // 40845","email":"waylon.bins@gmail.com","phoneNumber":"751.391.1365","nationalId":"NID-63250841","createdAt":"2025-12-10T13:14:45.922028",
    // },

    @Test
    public void testResponseHeaders() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl)
                .then()
                .statusCode(200)
                .header("Content-Type", containsString("application/json"));
    }
}
