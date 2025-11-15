package com.example.taskboard.web.dto;

public record TaskResponse(
   String id,
    String title,
    String description,
    String status,
    String priority,
    String dueDate,
    String createdAt,
    String updatedAt
) {

}
