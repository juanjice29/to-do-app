package com.example.todo.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskUpdateRequestDTO {
    @Size(min = 5, max = 50)
    private String title;
    private String description;
    private Boolean completed;
}
