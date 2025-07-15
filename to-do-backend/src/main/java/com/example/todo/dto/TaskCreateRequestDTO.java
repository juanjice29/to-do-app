package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskCreateRequestDTO {
    @NotBlank()
    @Size(min=5,max = 50)
    private String title;
    @NotBlank()
    private String description;
    private boolean completed;
}
