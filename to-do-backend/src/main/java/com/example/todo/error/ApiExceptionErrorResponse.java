package com.example.todo.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.List;


public record ApiExceptionErrorResponse(List<String> errors, HttpStatus status, ZonedDateTime date) {
}
