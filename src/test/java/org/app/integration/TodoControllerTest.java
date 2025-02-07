package org.app.integration;

import io.restassured.http.ContentType;
import lombok.extern.java.Log;
import org.app.dto.todos.EditTaskRequestDto;
import org.app.dto.todos.TaskDto;
import org.app.dto.todos.TaskResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TodoControllerTest extends IntegrationUtils {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @LocalServerPort
    private int port;

    private String bearerToken;

    @BeforeEach
    void setUp() throws InterruptedException {
        bearerToken = super.getBearerToken(port);
    }

    @Test
    void shouldCreateAndFetchTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Test task");
        taskDto.setComment("Test comment");
        taskDto.setDueDate(LocalDate.now().plusDays(5));
        taskDto.setCompleted(false);

        TaskResponseDto createdTask = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .body(taskDto)
                .when()
                .port(port)
                .post("/api/user/todos")
                .then()
                .statusCode(200)
                .extract().as(TaskResponseDto.class);

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .port(port)
                .get("/api/user/todos/" + createdTask.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(createdTask.getId().intValue()))
                .body("title", equalTo("Test task"))
                .body("comment", equalTo("Test comment"))
                .body("dueDate", notNullValue())
                .body("completed", equalTo(false));
    }

    @Test
    void shouldUpdateTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Initial task");
        taskDto.setComment("Initial comment");
        taskDto.setDueDate(LocalDate.now().plusDays(3));
        taskDto.setCompleted(false);

        TaskResponseDto createdTask = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .body(taskDto)
                .when()
                .port(port)
                .post("/api/user/todos")
                .then()
                .statusCode(200)
                .extract().as(TaskResponseDto.class);

        EditTaskRequestDto editTaskDto = new EditTaskRequestDto();
        editTaskDto.setCompleted(true);

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .body(editTaskDto)
                .when()
                .port(port)
                .put("/api/user/todos/" + createdTask.getId())
                .then()
                .statusCode(200)
                .body("title", equalTo("Initial task"))
                .body("comment", equalTo("Initial comment"))
                .body("dueDate", notNullValue())
                .body("completed", equalTo(true));
    }

    @Test
    void shouldDeleteTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Task to delete");
        taskDto.setComment("Comment to delete");
        taskDto.setDueDate(LocalDate.now().plusDays(1));
        taskDto.setCompleted(false);

        TaskResponseDto createdTask = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .body(taskDto)
                .when()
                .port(port)
                .post("/api/user/todos")
                .then()
                .statusCode(200)
                .extract().as(TaskResponseDto.class);

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .port(port)
                .delete("/api/user/todos/" + createdTask.getId())
                .then()
                .statusCode(200)
                .body(equalTo("Deleted task id: " + createdTask.getId()));

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .port(port)
                .get("/api/user/todos/" + createdTask.getId())
                .then()
                .statusCode(404);
    }

    @Test
    void shouldFilterAndReturnTasks() {
        TaskDto taskDto1 = new TaskDto();
        taskDto1.setTitle("First title");
        taskDto1.setComment("Created task n1");
        taskDto1.setDueDate(LocalDate.now().plusDays(10));
        taskDto1.setCompleted(false);

        TaskDto taskDto2 = new TaskDto();
        taskDto2.setTitle("Second title");
        taskDto2.setComment("Created task n2");
        taskDto2.setDueDate(LocalDate.now().plusDays(5));
        taskDto2.setCompleted(true);

        TaskResponseDto createdTask1 = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .body(taskDto1)
                .when()
                .port(port)
                .post("/api/user/todos")
                .then()
                .statusCode(200)
                .extract().as(TaskResponseDto.class);

        TaskResponseDto createdTask2 = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .body(taskDto2)
                .when()
                .port(port)
                .post("/api/user/todos")
                .then()
                .statusCode(200)
                .extract().as(TaskResponseDto.class);

        List<TaskResponseDto> response1 = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/api/user/todos/filter?" +
                        "createdDate=" + createdTask1.getCreatedDate() +
                        "&dueDate=" + createdTask1.getDueDate() +
                        "&completed=" + createdTask1.getCompleted() +
                        "&title=" + createdTask1.getTitle().split(" ")[0])
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", TaskResponseDto.class);

        assertEquals(1, response1.size());

        List<TaskResponseDto> response2 = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/api/user/todos/filter?" +
                        "dueDate=" + createdTask2.getDueDate())
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", TaskResponseDto.class);

        assertEquals(1, response2.size());

        List<TaskResponseDto> response3 = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/api/user/todos/filter")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", TaskResponseDto.class);

        assertEquals(2, response3.size());
    }
}
