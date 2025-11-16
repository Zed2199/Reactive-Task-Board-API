package com.example.taskboard.web.dto;

import com.example.taskboard.domain.Priority;
import com.example.taskboard.domain.TaskStatus;

import java.time.Instant;

public record TaskResponse(
   String id,
    String title,
    String description,
    TaskStatus status,
    Priority priority,
    Instant dueDate,
    Instant createdAt,
    Instant updatedAt
) {

}
