package com.plongrotha.loanmanagement.utils;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.plongrotha.loanmanagement.dto.response.ApiResponse;

public class ResponseUtil {
    private ResponseUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(
                ApiResponse.<T>builder()
                        .code(HttpStatus.OK.value())
                        .isSuccess(true)
                        .message("Success")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(
                ApiResponse.<T>builder()
                        .code(HttpStatus.OK.value())
                        .isSuccess(true)
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    public static ResponseEntity<ApiResponse<Void>> success(String message) {
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(HttpStatus.OK.value())
                        .isSuccess(true)
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message, Map<String, Object> metadata) {
        return ResponseEntity.ok(
                ApiResponse.<T>builder()
                        .code(HttpStatus.OK.value())
                        .isSuccess(true)
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return success(data);
    }

    // 2. OK with data and message
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return success(data, message);
    }

    // 3. OK with data, message, and metadata
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message, Map<String, Object> metadata) {
        return success(data, message, metadata);
    }

    // 4. OK with message only
    public static ResponseEntity<ApiResponse<Void>> ok(String message) {
        return success(message);
    }

    // ==================== CREATED METHODS ====================

    // 1. Created with data
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<T>builder()
                        .isSuccess(true)
                        .message("Created successfully")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<T>builder()
                        .code(HttpStatus.CREATED.value())
                        .isSuccess(true)
                        .message("Created successfully")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // 2. Created with data and message
    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<T>builder()
                        .code(HttpStatus.CREATED.value())
                        .isSuccess(true)
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // ==================== ERROR METHODS ====================

    // 1. Error with message
    public static ResponseEntity<ApiResponse<Void>> error(String message) {
        return ResponseEntity.badRequest().body(
                ApiResponse.<Void>builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .isSuccess(false)
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // 2. Error with message and status
    public static ResponseEntity<ApiResponse<Void>> error(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(
                ApiResponse.<Void>builder()
                        .code(status.value())
                        .isSuccess(false)
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // 3. Error with data and message
    public static <T> ResponseEntity<ApiResponse<T>> error(T data, String message) {
        return ResponseEntity.badRequest().body(
                ApiResponse.<T>builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .isSuccess(false)
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // 4. Error with data, message, and status
    public static <T> ResponseEntity<ApiResponse<T>> error(T data, String message, HttpStatus status) {
        return ResponseEntity.status(status).body(
                ApiResponse.<T>builder()
                        .code(status.value())
                        .isSuccess(false)
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // ==================== SPECIFIC ERROR METHODS ====================

    public static ResponseEntity<ApiResponse<Void>> badRequest(String message) {
        return error(message, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<ApiResponse<Void>> unauthorized(String message) {
        return error(message, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<ApiResponse<Void>> forbidden(String message) {
        return error(message, HttpStatus.FORBIDDEN);
    }

    public static ResponseEntity<ApiResponse<Void>> notFound(String message) {
        return error(message, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<ApiResponse<Void>> conflict(String message) {
        return error(message, HttpStatus.CONFLICT);
    }

    public static ResponseEntity<ApiResponse<Void>> internalServerError(String message) {
        return error(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
