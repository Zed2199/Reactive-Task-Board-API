package com.example.taskboard.web.dto;

public record TaskStatsResponse(
        long total,
        long todo,
        long inProgress,
        long done
) {}
