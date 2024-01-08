package ru.netology.rest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.filter.log.LogDetail.ALL;
import static org.hamcrest.Matchers.*;

class MobileBankApiTestV1 {

    private RequestSpecification requestSpec;

    @BeforeEach
    void setUp() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setBasePath("/api/v1")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(ALL)
                .build();
    }

    @Test
    void shouldReturnDemoAccounts() {
        given()
                .spec(requestSpec)
                .when()
                .get("/demo/accounts")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("accounts.schema.json"))
                .body("", hasSize(greaterThan(0)))
                .body("[0].id", notNullValue())
                .body("[0].name", not(emptyOrNullString()))
                .body("[0].number", matchesPattern("^•• \\d{4}$"))
                .body("[0].balance", greaterThanOrEqualTo(0))
                .body("[0].currency", not(emptyOrNullString()));
    }
}
