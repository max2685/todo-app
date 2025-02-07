package org.app.integration;

import io.restassured.http.ContentType;
import lombok.extern.java.Log;
import org.app.dto.todos.TaskDto;
import org.app.dto.todos.TaskResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Log
public class TodoControllerFilterPagingSortTest extends IntegrationUtils {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @LocalServerPort
    private int port;

    private String bearerToken;

    private TaskDto taskDto1;
    private TaskDto taskDto2;
    private TaskDto taskDto3;

    @BeforeAll
    void setupTestData() throws InterruptedException {
        bearerToken = super.getBearerToken(port);

        taskDto1 = new TaskDto();
        taskDto1.setTitle("First title");
        taskDto1.setComment("Created task n1");
        taskDto1.setDueDate(LocalDate.now().plusDays(10));
        taskDto1.setCompleted(false);

        taskDto2 = new TaskDto();
        taskDto2.setTitle("Second title");
        taskDto2.setComment("Created task n2");
        taskDto2.setDueDate(LocalDate.now().plusDays(5));
        taskDto2.setCompleted(true);

        taskDto3 = new TaskDto();
        taskDto3.setTitle("Third title");
        taskDto3.setComment("Created task n3");
        taskDto3.setDueDate(LocalDate.now().plusDays(3));
        taskDto3.setCompleted(true);

        super.createTask(taskDto1, bearerToken, port);
        super.createTask(taskDto2, bearerToken, port);
        super.createTask(taskDto3, bearerToken, port);
    }


    @Test
    void shouldFilterByTitle() {
        List<TaskResponseDto> response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/api/user/todos/filtering-pagination-sorting?title=First")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", TaskResponseDto.class);

        assertEquals(1, response.size());
        assertEquals(taskDto1.getTitle(), response.get(0).getTitle());
    }

    @Test
    void shouldFilterByCompletedStatus() {
        List<TaskResponseDto> response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/api/user/todos/filtering-pagination-sorting?completed=true")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", TaskResponseDto.class);

        assertEquals(2, response.size());
        assertEquals(taskDto2.getCompleted(), response.get(0).getCompleted());
        assertEquals(taskDto3.getCompleted(), response.get(1).getCompleted());
    }

    @Test
    void shouldSortByTitleAscending() {
        List<TaskResponseDto> response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/api/user/todos/filtering-pagination-sorting?sort=%5B%7B%22field%22%3A%22title%22%2C%22direction%22%3A%22asc%22%7D%5D")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", TaskResponseDto.class);

        assertEquals(3, response.size());
        assertEquals(taskDto1.getTitle(), response.get(0).getTitle());
        assertEquals(taskDto2.getTitle(), response.get(1).getTitle());
        assertEquals(taskDto3.getTitle(), response.get(2).getTitle());
    }

    @Test
    void shouldSortByDueDateDescending() {
        List<TaskResponseDto> response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/api/user/todos/filtering-pagination-sorting?sort=%5B%7B%22field%22%3A%22dueDate%22%2C%22direction%22%3A%22desc%22%7D%5D")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", TaskResponseDto.class);

        assertEquals(3, response.size());
        assertTrue(response.get(0).getDueDate().isAfter(response.get(1).getDueDate()));
        assertTrue(response.get(1).getDueDate().isAfter(response.get(2).getDueDate()));
    }

    @Test
    void shouldReturnCorrectPaging() {
        List<TaskResponseDto> response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/api/user/todos/filtering-pagination-sorting?page=0&size=2")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content", TaskResponseDto.class);

        assertEquals(2, response.size());
    }
}
