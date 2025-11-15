package com.example.taskboard.web.functional;

import com.example.taskboard.domain.Priority;
import com.example.taskboard.domain.TaskStatus;
import com.example.taskboard.service.TaskService;
import com.example.taskboard.web.dto.CreateTaskRequest;
import com.example.taskboard.web.dto.TaskResponse;
import com.example.taskboard.web.dto.UpdateTaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TaskHandler {

    private final TaskService taskService;

    public Mono<ServerResponse> getAllTasks(ServerRequest request) {
        TaskStatus status = request.queryParam("status")
                .map(TaskStatus::valueOf)
                .orElse(null);

        Priority priority = request.queryParam("priority")
                .map(Priority::valueOf)
                .orElse(null);

        Flux<TaskResponse> tasks = taskService.getAllTasks(status, priority);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tasks, TaskResponse.class);
    }

    public Mono<ServerResponse> getAllTasksPaged(ServerRequest request) {
        int page = request.queryParam("page")
                .map(Integer::parseInt)
                .orElse(0);

        int size = request.queryParam("size")
                .map(Integer::parseInt)
                .orElse(10);

        TaskStatus status = request.queryParam("status")
                .map(TaskStatus::valueOf)
                .orElse(null);

        Priority priority = request.queryParam("priority")
                .map(Priority::valueOf)
                .orElse(null);

        Flux<TaskResponse> tasks = taskService.getTasksPaged(page, size, status, priority);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tasks, TaskResponse.class);
    }

    public Mono<ServerResponse> getTaskById(ServerRequest request) {
        String id = request.pathVariable("id");

        return taskService.getTaskById(id)
                .flatMap(task ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(task)
                );
    }

    public Mono<ServerResponse> createTask(ServerRequest request) {
        return request.bodyToMono(CreateTaskRequest.class)
                .flatMap(taskService::createTask)
                .flatMap(created ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(created)
                );
    }

    public Mono<ServerResponse> updateTask(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(UpdateTaskRequest.class)
                .flatMap(dto -> taskService.updateTask(id, dto))
                .flatMap(updated ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(updated)
                );
    }

    public Mono<ServerResponse> deleteTask(ServerRequest request) {
        String id = request.pathVariable("id");

        return taskService.deleteTask(id)
                .then(ServerResponse.noContent().build());
    }
}
