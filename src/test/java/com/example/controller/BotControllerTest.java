package com.example.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
class BotControllerTest {

    @Test
    void testDownloadZip() {
        given()
                .when()
                .get("/api/v1/file-sharing-bot/download")
                .then()
                .statusCode(500)
                .body(containsString("The specified bucket does not exist"));
    }
}