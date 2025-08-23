package com.springboot.airbnb.advices;

import com.springboot.airbnb.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), apiError.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(Exception ex) {
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return new ResponseEntity<>(new ApiResponse<>(apiError), apiError.getStatus());
    }



}
