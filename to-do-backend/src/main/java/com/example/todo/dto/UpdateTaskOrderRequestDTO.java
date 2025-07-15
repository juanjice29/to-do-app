package com.example.todo.dto;

import com.example.todo.dto.validation.AtLeastOneNotNull;
import lombok.Data;

@Data
@AtLeastOneNotNull
public class UpdateTaskOrderRequestDTO {
    private Double orderBefore;
    private Double orderAfter;
}
