package com.miguelpazatto.leadsmanager.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseApiTest {

    @LocalServerPort
    private int port;

    protected static String tokenAdmin;
    protected static String visitantToken;

    @BeforeAll
    void setup() {
        RestAssured.port = port;
        RestAssured.basePath = "";

        String adminLogin = System.getenv().get("ADMIN_LOGIN");
        String adminPassword = System.getenv().get("ADMIN_PASSWORD");

        tokenAdmin = requestLogin(adminLogin, adminPassword);

        visitantToken = requestLogin("visitant", "demo123");
    }

    private String requestLogin(String login, String password) {
        return given()
                .spec(jsonRequest)
                .body("""
                        {
                            "login": "%s",
                            "password": "%s"
                        }
                        """.formatted(login, password))
                .when()
                .post("auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    protected RequestSpecification adminRequest() {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenAdmin);
    }

    protected RequestSpecification visitantRequest() {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + visitantToken);
    }

    protected RequestSpecification jsonRequest = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .build();
}

