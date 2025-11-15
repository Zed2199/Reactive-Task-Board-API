package com.example.taskboard.service;

import com.example.taskboard.domain.Priority;
import com.example.taskboard.domain.Task;
import com.example.taskboard.domain.TaskStatus;
import com.example.taskboard.exceptions.TaskNotFoundException;
import com.example.taskboard.mapper.TaskMapper;
import com.example.taskboard.repository.TaskRepository;
import com.example.taskboard.web.dto.CreateTaskRequest;
import com.example.taskboard.web.dto.TaskResponse;
import com.example.taskboard.web.dto.UpdateTaskRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Flux<TaskResponse> getAllTasks(TaskStatus status, Priority priority) {

        log.debug("Getting all tasks for status {} and priority {}", status, priority);

        Flux<Task> tasks;

        if (status != null && priority != null) {
            tasks = taskRepository.findByStatusAndPriority(status, priority);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status);
        } else if (priority != null) {
            tasks = taskRepository.findByPriority(priority);
        }else {
            tasks = taskRepository.findAll();
        }
        return tasks
                .doOnComplete(() -> log.debug("Retrieved all tasks"))
                .map(taskMapper::toResponse);
    }

    @Override
    public Mono<TaskResponse> getTaskById(String id) {
        return taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new TaskNotFoundException(id)))
                .doOnNext(task -> log.debug("Found task id={}", task.getId()))
                .doOnError(ex -> log.error("Error while fetching task id={}", id, ex))
                .map(taskMapper::toResponse);
    }


    @Override
    public Mono<TaskResponse> createTask(CreateTaskRequest request) {

        log.info("Creating task with title: {}", request.title());

        Task task = taskMapper.toEntity(request);
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }
        if (task.getPriority() == null) {
            task.setPriority(Priority.MEDIUM);
        }
        return taskRepository.save(task)
                .doOnNext(saved -> log.info("Task created with ID: {}", saved.getId()))
                .map(taskMapper::toResponse);
    }

    @Override
    public Mono<TaskResponse> updateTask(String id, UpdateTaskRequest updatedTask) {
        return taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new TaskNotFoundException(id)))
                .flatMap(existing -> {
                    taskMapper.updateEntityFromDto(updatedTask, existing);
                    return taskRepository.save(existing);
                })
                .map(taskMapper::toResponse);
    }

    @Override
    public Mono<Void> deleteTask(String id) {
        return taskRepository.existsById(id)
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return taskRepository.deleteById(id);
                    } else {
                        return Mono.error(new TaskNotFoundException(id));
                    }
                });
    }

    @Override
    public Flux<TaskResponse> getTasksPaged(int page, int size, TaskStatus status, Priority priority) {
        log.debug("Getting paged tasks - page: {}, size: {}, status: {}, priority: {}",
                page, size, status, priority);

        Flux<Task> tasks;

        if (status != null && priority != null) {
            tasks = taskRepository.findByStatusAndPriority(status, priority);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status);
        } else if (priority != null) {
            tasks = taskRepository.findByPriority(priority);
        } else {
            tasks = taskRepository.findAll();
        }

        return tasks
                .skip((long) page * size)
                .take(size)
                .doOnComplete(() -> log.debug("Retrieved paged tasks"))
                .map(taskMapper::toResponse);
    }

}
