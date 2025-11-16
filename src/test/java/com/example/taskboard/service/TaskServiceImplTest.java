package com.example.taskboard.service;

import com.example.taskboard.domain.Priority;
import com.example.taskboard.domain.Task;
import com.example.taskboard.domain.TaskStatus;
import com.example.taskboard.exceptions.TaskNotFoundException;
import com.example.taskboard.mapper.TaskMapper;
import com.example.taskboard.repository.TaskRepository;
import com.example.taskboard.web.dto.CreateTaskRequest;
import com.example.taskboard.web.dto.UpdateTaskRequest;
import com.example.taskboard.web.event.TaskEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {

    private TaskRepository taskRepository;
    private TaskServiceImpl taskService;

    @BeforeEach
    void setup() {
        taskRepository = mock(TaskRepository.class);
        Sinks.Many<TaskEvent> sink = Sinks.many().multicast().onBackpressureBuffer();
        TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

        taskService = new TaskServiceImpl(taskRepository, taskMapper, sink);
    }

    @Test
    void createTask_shouldCreateAndReturnTaskResponse() {
        CreateTaskRequest req = new CreateTaskRequest("Test", "desc", TaskStatus.TODO, Priority.MEDIUM, null);

        Task saved = Task.builder()
                .id("123")
                .title("Test")
                .description("desc")
                .status(TaskStatus.TODO)
                .priority(Priority.MEDIUM)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(taskService.createTask(req))
                .expectNextMatches(resp ->
                        resp.id().equals("123") &&
                                resp.title().equals("Test"))
                .verifyComplete();

        verify(taskRepository, times(1)).save(any(Task.class));
    }


    @Test
    void getTaskById_shouldReturnNotFoundWhenEmpty() {
        when(taskRepository.findById("missing")).thenReturn(Mono.empty());

        StepVerifier.create(taskService.getTaskById("missing"))
                .expectError(TaskNotFoundException.class)
                .verify();
    }

    @Test
    void updateTask_shouldUpdateFields() {
        Task existing = Task.builder()
                .id("123")
                .title("Old")
                .build();

        when(taskRepository.findById("123")).thenReturn(Mono.just(existing));
        when(taskRepository.save(existing)).thenReturn(Mono.just(existing));

        UpdateTaskRequest req = new UpdateTaskRequest("NewTitle", "desc", TaskStatus.IN_PROGRESS, Priority.HIGH, null);

        StepVerifier.create(taskService.updateTask("123", req))
                .expectNextMatches(resp -> resp.title().equals("NewTitle"))
                .verifyComplete();
    }

}
