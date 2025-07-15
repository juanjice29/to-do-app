package com.example.todo.dto.validation;

import com.example.todo.dto.UpdateTaskOrderRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, UpdateTaskOrderRequestDTO> {

    @Override
    public boolean isValid(UpdateTaskOrderRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }
        return dto.getOrderBefore() != null || dto.getOrderAfter() != null;
    }
}
