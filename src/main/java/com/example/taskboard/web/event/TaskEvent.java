package com.example.taskboard.web.event;

import com.example.taskboard.web.dto.TaskResponse;

public record TaskEvent(
        String type,
        TaskResponse payload
) {}