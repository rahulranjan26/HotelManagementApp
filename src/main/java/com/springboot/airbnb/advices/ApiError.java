package com.springboot.airbnb.advices;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class ApiError {
    private String message;
    private HttpStatus status;
    List<String> subErrors;
}
