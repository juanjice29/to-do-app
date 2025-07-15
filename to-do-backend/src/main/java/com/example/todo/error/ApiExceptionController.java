package com.example.todo.error;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionController {

    @ExceptionHandler(value= {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handlerNotValidRequest(MethodArgumentNotValidException exception){
        List<String> errors = new ArrayList<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : exception.getBindingResult().getGlobalErrors()) {
            errors.add(error.getDefaultMessage());
        }
        //List<String> errors= exception.getBindingResult().getFieldErrors().stream().map(n->n.getField() +", "+n.getDefaultMessage()).toList();
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiExceptionErrorResponse response=new ApiExceptionErrorResponse(errors, badRequest, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(response, badRequest);
    }
    @ExceptionHandler(value = ApiExceptionBussines.class)
    public ResponseEntity<Object> handlerBussines(ApiExceptionBussines exception){
        HttpStatus badRequest = exception.getStatus();
        ApiExceptionErrorResponse response=new ApiExceptionErrorResponse(List.of(exception.getMessage()), badRequest, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(response, badRequest);
    }
}
