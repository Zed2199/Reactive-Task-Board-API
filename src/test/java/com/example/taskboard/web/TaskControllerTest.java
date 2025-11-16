package com.example.taskboard.web;

import com.example.taskboard.TaskboardApplication;
import com.example.taskboard.domain.TaskStatus;
import com.example.taskboard.web.dto.CreateTaskRequest;
import com.example.taskboard.web.dto.TaskResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TaskboardApplication.class)
@AutoConfigureWebTestClient
public class TaskControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void createTask_shouldReturnCreated() {
        CreateTaskRequest request = new CreateTaskRequest(
                "TestTask",
                "desc",
                TaskStatus.TODO,
                null,
                null
        );

        webTestClient.post()
                .uri("/api/tasks")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TaskResponse.class)
                .value(resp -> assertThat(resp.title()).isEqualTo("TestTask"));
    }

    @Test
    void getTask_shouldReturn404() {
        webTestClient.get()
                .uri("/api/tasks/unknown-id")
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void streamEvents_shouldReceiveEvents() {
        var eventStream = webTestClient.get()
                .uri("/api/tasks/stream")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

        StepVerifier.create(eventStream.take(1))
                .then(() -> {
                    CreateTaskRequest req = new CreateTaskRequest("SSE task", "desc", TaskStatus.TODO, null, null);

                    webTestClient.post()
                            .uri("/api/tasks")
                            .bodyValue(req)
                            .exchange()
                            .expectStatus().isCreated();
                })
                .expectNextMatches(body -> body.contains("CREATED"))
                .verifyComplete();
    }

    @Test
    void fn_getAllTasks_shouldWork() {
        webTestClient.get()
                .uri("/fn/tasks")
                .exchange()
                .expectStatus().isOk();
    }


}