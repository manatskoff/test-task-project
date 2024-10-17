package com.example.task.controller;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

import com.example.task.service.UserService;
import io.restassured.RestAssured;

import java.math.BigDecimal;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTestContainer {

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        System.out.println("UserControllerTestContainer");
    }

    @Test
    void transferMoney() {
        Long currentUserId = 1L;
        Long toUserId = 2L;

        var currentUser = userService.getCurrentUser();
        userService.transferMoney(currentUserId, toUserId, BigDecimal.valueOf(10L));

        given()
                .contentType(io.restassured.http.ContentType.JSON)
                .when()
                .get("/api/users/transfer")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }
}