package com.example.taskboard.repository;

import com.example.taskboard.domain.Priority;
import com.example.taskboard.domain.Task;
import com.example.taskboard.domain.TaskStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.Instant;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {

    Flux<Task> findByStatus(TaskStatus status);

    Flux<Task> findByPriority(Priority priority);

    Flux<Task> findByStatusAndPriority(TaskStatus status, Priority priority);

    Flux<Task> findByTitleContainingIgnoreCase(String titlePart);

}
