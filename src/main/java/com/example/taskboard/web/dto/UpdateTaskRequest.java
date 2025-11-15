package com.example.taskboard.web.dto;

import com.example.taskboard.domain.Priority;
import com.example.taskboard.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record UpdateTaskRequest(
        @NotBlank
        @Size(max = 200)
        String title,

        @Size(max = 1000)
        String description,

        TaskStatus status,
        Priority priority,
        Instant dueDate
) {
}