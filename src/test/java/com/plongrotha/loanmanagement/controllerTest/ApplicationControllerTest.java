package com.plongrotha.loanmanagement.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationControllerTest {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        baseUrl = baseUrl + ":" + port + "/api/v1/applications";
    }

    // @Test
    public void testGetAllApplications() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl)
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("message", equalTo("Applications retrieved successfully"))
                .body("data.size()", greaterThan(0));
    }

    // @Test
    public void testUpdateApplication() {
        Long applicationId = 102L;
        String requestBody = """
                                           {
                  "applicantFullName": "Rotha Updated",
                  "address": "123 Updated St, cambodia Update, Country Updated",
                  "email": "rotha.updated@gmail.com",
                  "phoneNumber": "012-345-6789",
                  "nationalId": "NID-63250841"
                }
                 """;
        given().header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .put(baseUrl + "/" + applicationId)
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("message", equalTo("Application updated successfully"))
                .body("data.applicationId", equalTo(102))
                .body("data.applicantFullName", equalTo("Rotha Updated"))
                .body("data.address", equalTo("123 Updated St, cambodia Update, Country Updated"))
                .body("data.email", equalTo("rotha.updated@gmail.com"))
                .body("data.phoneNumber", equalTo("012-345-6789"))
                .body("data.nationalId", equalTo("NID-63250841"));

    }

    // @Test
    public void testGetApplicationById() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(baseUrl + "/1")
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                // Application retrieved successfully
                .body("message", equalTo("Application retrieved successfully"))
                // Access nested fields inside "data"
                .body("data.applicationId", equalTo(1))
                .body("data.applicantFullName", equalTo("Plong Rotha2"))
                .body("data.address", equalTo("Phnom Penh, Cambodia"))
                .body("data.email", equalTo("rotha243@gmail.com"))
                .body("data.phoneNumber", equalTo("0123456789"))
                .body("data.nationalId", equalTo("KH-123456789"))
                .body("data.createdAt", notNullValue());
    }

    // @Test
    public void testDeleteApplication() {
        Long applicationId = 12L;
        given()
                .when()
                .delete(baseUrl + "/" + applicationId)
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("message", equalTo("Application deleted successfully"));
    }
}
