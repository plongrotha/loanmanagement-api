package com.plongrotha.loanmanagement.controllerTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoanRefundControllerTest {
    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        baseUrl = baseUrl + ":" + port + "/api/v1/refund-application";
    }

    // @Test
    public void getAllRefundLoanTest() {
        given().header("Content-Type", "application/json").when().get(baseUrl).then().statusCode(HttpStatus.OK.value())
                .body("message", equalTo("all loanApplication refund retrived successfully"))
                .body("data.size()", greaterThan(0));
    }

    // @Test
    public void createRefundTest() {
        RestAssured.baseURI = baseUrl;
        String requestBody = """
                              {
                  "loanApplicationId": 0,
                  "refundAmount": 0
                }
                                        """;
        given().header("Content-Type", "application/json").body(requestBody).when().post().then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                // .body("message", equalTo("Loan Refund is created successfully."))
                // .body("data.loanApplicationId", equalTo(1))
                // .body("data.refundAmount", equalTo(0))
                .body("instance", equalTo("/api/v1/refund-application"))
                .body("errors.refundAmount", equalTo("refundAmount must be positive and greater than 0"))
        // .body("data.paidAmount", equalTo(43.50F))
        ;
    }

    // @Test
    public void getRefundByIdTest() {
        Long refundId = 1L;
        given().header("Content-Type", "application/json").when().get(baseUrl + "/" + refundId).then().statusCode(200)
                .body("message", equalTo("loan refund Id : " + refundId + " retrieved successfully"))
                .body("data.loanRefundId", equalTo(1))
                .body("data.loanApplicationId", equalTo(1))
                .body("data.totalLoanAmount", equalTo(90000.0F))
                .body("data.refundReadyDate", equalTo(null))
                .body("data.refundCompletedDate", equalTo(null))
                .body("data.createdAt", equalTo("2025-12-14T15:30:11.681905"))
                .body("data.updatedAt", equalTo("2025-12-14T15:30:11.640712"))
                .body("data.remainAmount", equalTo(89998.1F))
                .body("success", equalTo(true));
    }

}
