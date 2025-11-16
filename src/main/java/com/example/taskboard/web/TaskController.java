package com.example.taskboard.web;

import com.example.taskboard.domain.Priority;
import com.example.taskboard.domain.TaskStatus;
import com.example.taskboard.service.TaskService;
import com.example.taskboard.web.dto.*;
import com.example.taskboard.web.event.TaskEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    Flux<TaskResponse> getAllTasks(
            @RequestParam(name = "status", required = false) TaskStatus status,
            @RequestParam(name = "priority", required = false) Priority priority
    ) {
        return taskService.getAllTasks(status, priority);
    }

    @GetMapping("/paged")
    Flux<TaskResponse> getTasksPaged(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "status", required = false) TaskStatus status,
            @RequestParam(name = "priority", required = false) Priority priority
    ) {
        return taskService.getTasksPaged(page, size, status, priority);
    }

    @GetMapping("/{id}")
    Mono<TaskResponse> fetchTaskById(@PathVariable String id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TaskResponse> createTask(@RequestBody @Valid CreateTaskRequest task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    Mono<TaskResponse> updateTask(@PathVariable String id, @RequestBody @Valid UpdateTaskRequest updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<Void> deleteTask(@PathVariable String id) {
        return taskService.deleteTask(id);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<ServerSentEvent<TaskEvent>> streamTaskEvents() {
        return taskService.streamTaskEvents()
                .map(event -> ServerSentEvent.<TaskEvent>builder()
                        .event(event.type())
                        .data(event)
                        .build());
    }

    @PostMapping("/complete")
    Mono<java.util.List<TaskResponse>> completeTasksBulk(@RequestBody BulkCompleteRequest request) {
        return taskService.completeTasksBulk(request.ids());
    }

    @GetMapping("/stats")
    Mono<TaskStatsResponse> getTaskStats() {
        return taskService.getTaskStats();
    }

    @GetMapping("/{id}/safe")
    Mono<TaskResponse> getTaskOrDefault(@PathVariable String id) {
        return taskService.getTaskByIdOrDefault(id);
    }


}
