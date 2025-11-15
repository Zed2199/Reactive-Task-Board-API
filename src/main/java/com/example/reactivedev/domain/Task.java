package com.example.reactivedev.domain;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document("tasks")
public class Task {

    @Id
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime dueDate;

}
