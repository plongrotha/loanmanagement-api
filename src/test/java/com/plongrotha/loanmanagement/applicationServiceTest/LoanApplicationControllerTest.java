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
        RestAssured.port = port;
        baseUrl = baseUrl + ":" + port + "/api/v1/applications";
    }

    @Test
    public void testGetApplicationById() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl + "/1")
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("message", equalTo(" Application retrieved successfully"))
                // Access nested fields inside "data"
                .body("data.applicationId", equalTo(1))
                .body("data.applicantFullName", equalTo("Todd Dicki"))
                .body("data.address", equalTo("Apt. 825 496 Hickle Divide, North Chelsieburgh, IA 40845"))
                .body("data.email", equalTo("waylon.bins@gmail.com"))
                .body("data.phoneNumber", equalTo("751.391.1365"))
                .body("data.nationalId", equalTo("NID-63250841"))
                .body("data.createdAt", notNullValue());
    }

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
