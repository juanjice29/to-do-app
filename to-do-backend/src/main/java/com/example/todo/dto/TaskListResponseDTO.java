package com.example.todo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskListResponseDTO {
    private Long id;
    private String title;
    private boolean completed;
    private Double order;
    private LocalDateTime createdAt;
}