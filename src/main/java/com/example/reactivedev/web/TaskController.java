package com.example.reactivedev.web;

import com.example.reactivedev.domain.Task;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @GetMapping
    Flux<Task> listAllTasks() {
        return null;
    }

    @GetMapping("/{id}")
    Mono<Task> fetchTaskById(@PathVariable String id) {
        return  null;
    }

    @PostMapping
    Mono<Task> createTask(@RequestBody Task task) {
        return null;
    }

    @PutMapping("/{id}")
    Mono<Task> updateTask(@PathVariable String id, @RequestBody Task task) {
        return  null;
    }

    @DeleteMapping("/{id}")
    Mono<Void> deleteTask(@PathVariable String id) {
        return null;
    }

}
