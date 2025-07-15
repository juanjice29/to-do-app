package com.example.todo.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiExceptionBussines extends RuntimeException{
    private HttpStatus status;
    public ApiExceptionBussines(String message,HttpStatus status){
        super(message);
        this.status=status;
    }
}
