package com.example.reactivedev.repository;

import com.example.reactivedev.domain.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {



}
