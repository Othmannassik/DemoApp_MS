package com.example.transaction_ms.webs;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.*;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(ConstraintViolationException exception){
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        Map<String, List<String>> listMap = new HashMap<>();
        constraintViolations.forEach(cv -> {
            List<String> fieldErrors = listMap.get(cv.getPropertyPath().toString());
            if (fieldErrors == null){
                listMap.put(cv.getPropertyPath().toString(), new ArrayList<>());
            }
            listMap.get(cv.getPropertyPath().toString()).add(cv.getMessage());
        });
        return ResponseEntity.badRequest().body(listMap);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleRunTimeException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }
}
