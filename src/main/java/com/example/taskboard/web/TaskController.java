package com.example.taskboard.web;

import com.example.taskboard.domain.Priority;
import com.example.taskboard.domain.Task;
import com.example.taskboard.domain.TaskStatus;
import com.example.taskboard.service.TaskService;
import com.example.taskboard.web.dto.CreateTaskRequest;
import com.example.taskboard.web.dto.TaskResponse;
import com.example.taskboard.web.dto.UpdateTaskRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

}
