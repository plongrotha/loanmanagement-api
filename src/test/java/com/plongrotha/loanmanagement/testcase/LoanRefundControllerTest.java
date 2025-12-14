package com.plongrotha.loanmanagement.testcase;

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

    @Test
    public void getAllRefundLoanTest() {
        given().header("Content-Type", "application/json").when().get(baseUrl).then().statusCode(HttpStatus.OK.value())
                .body("message", equalTo("all loanApplication refund retrived successfully"))
                .body("data.size()", greaterThan(0));
    }

    @Test
    public void createRefundTest() {
        RestAssured.baseURI = baseUrl;
        String requestBody = """
                              {
                  "loanApplicationId": 1,
                  "refundAmount": 1.10
                }
                                        """;
        given().header("Content-Type", "application/json").body(requestBody).when().post().then()
                .statusCode(HttpStatus.CREATED.value())
                .body("message", equalTo("Loan Refund is created successfully."))
                .body("data.loanApplicationId", equalTo(1))
                .body("data.refundAmount", equalTo(1.10F))
                .body("success", equalTo(true));
    }

    @Test
    public void getRefundByIdTest() {
        Long refundId = 1L;
        given().header("Content-Type", "application/json").when().get(baseUrl + "/" + refundId).then().statusCode(200)
                .body("message", equalTo("loan refund Id : 1 retrieved successfully"))
                .body("data.loanRefundId", equalTo(1))
                .body("data.loanApplicationId", equalTo(3))
                .body("data.totalLoanAmount", equalTo(19884.0F))
                .body("data.refundAmount", equalTo(884.0F))
                .body("data.refundReadyDate", equalTo(null))
                .body("data.refundCompletedDate", equalTo(null))
                .body("data.createdAt", equalTo("2025-12-10T13:25:20.169584"))
                .body("data.updatedAt", equalTo("2025-12-10T13:25:20.148917"))
                .body("data.remainAmount", equalTo(19000.0F))
                .body("success", equalTo(true));
    }

}
