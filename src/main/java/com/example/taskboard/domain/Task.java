package com.example.taskboard.domain;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "tasks")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Task {

    @Id
    private String id;

    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private Instant dueDate;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;


}
