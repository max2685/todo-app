package org.app.integration;


import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.app.dto.RegisterUserRequestDto;
import org.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    @Test
    void shouldSaveUserAndLogin() {
        RegisterUserRequestDto registerRequest = new RegisterUserRequestDto();
        registerRequest.setUsername("max123321@gmail.com");
        registerRequest.setPassword("Password1!");
//        userRepository.save(todos);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .log().all()
                .when()
                .port(port)
                .post("/public/register")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        String responseBody = response.getBody().asString();
        System.out.println("response: " + responseBody);

//        AuthRequestDto authRequestDto = new AuthRequestDto();
//        registerRequest.setUsername("max123321@gmail.com");
//        registerRequest.setPassword("Password1!");
//
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(authRequestDto)
//                .log().all()
//                .when()
//                .baseUri("http://localhost")
//                .port(8080)
//                .post("/public/login")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .extract().response();

    }
}
