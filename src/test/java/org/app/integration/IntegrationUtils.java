package org.app.integration;

import io.restassured.http.ContentType;
import org.app.Utils;
import org.app.dto.auth.AuthRequestDto;
import org.app.dto.auth.RegisterUserRequestDto;
import org.app.dto.todos.TaskDto;
import org.app.dto.todos.TaskResponseDto;

import static io.restassured.RestAssured.given;

public class IntegrationUtils {

    String getBearerToken(int port) throws InterruptedException {
        String username = Utils.generateRandomEmail();
        String password = Utils.generateRandomPassword();

        RegisterUserRequestDto registerRequest = new RegisterUserRequestDto();
        registerRequest.setUsername(username);
        registerRequest.setPassword(password);

        given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .when()
                .port(port)
                .post("/public/register")
                .then()
                .statusCode(200);

        AuthRequestDto authRequestDto = new AuthRequestDto();
        authRequestDto.setUsername(username);
        authRequestDto.setPassword(password);
        Thread.sleep(500);

        return given()
                .contentType(ContentType.JSON)
                .body(authRequestDto)
                .when()
                .port(port)
                .post("/public/login")
                .then()
                .statusCode(200)
                .extract().body().asString().replace("Bearer ", "");
    }

    void createTask(TaskDto taskDto, String bearerToken, int port) {
        given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .body(taskDto)
                .when()
                .port(port)
                .post("/api/user/todos")
                .then()
                .statusCode(200)
                .extract().as(TaskResponseDto.class);
    }
}
