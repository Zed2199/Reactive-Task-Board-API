package com.example.taskboard.service;

import com.example.taskboard.domain.Priority;
import com.example.taskboard.domain.Task;
import com.example.taskboard.domain.TaskStatus;
import com.example.taskboard.web.dto.CreateTaskRequest;
import com.example.taskboard.web.dto.TaskResponse;
import com.example.taskboard.web.dto.UpdateTaskRequest;
import com.example.taskboard.web.event.TaskEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

    Flux<TaskResponse> getAllTasks(TaskStatus status, Priority priority);

    Mono<TaskResponse> getTaskById(String id);

    Mono<TaskResponse> createTask(CreateTaskRequest input);

    Mono<TaskResponse> updateTask(String id, UpdateTaskRequest input);

    Mono<Void> deleteTask(String id);

    Flux<TaskResponse> getTasksPaged(int page, int size, TaskStatus status, Priority priority);

    Flux<TaskEvent> streamTaskEvents();

}
