package com.example.taskboard.web.dto;

import java.util.List;

public record BulkCompleteRequest(
        List<String> ids
) {}