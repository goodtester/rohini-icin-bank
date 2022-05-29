package com.rohini.icinbank.util;

import java.time.LocalDateTime;

import com.rohini.icinbank.domain.common.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    private ResponseUtil() {}

    public static <T> ResponseEntity<Object> build(String message, T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(build(message, data), httpStatus);
    }

    private static <T> ApiResponse<T> build(String message, T data) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .data(data)
                .build();
    }
}