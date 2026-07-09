package com.prueba_tecnica_nicola.prueba.common.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.prueba_tecnica_nicola.prueba.product.domain.exception.InvalidProductException;
import com.prueba_tecnica_nicola.prueba.product.domain.exception.ProductNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String DEFAULT_VALIDATION_MESSAGE = "Error de validacion";
    private static final String DEFAULT_INVALID_VALUE_MESSAGE = "valor invalido";
    private static final String INTERNAL_ERROR_MESSAGE = "Error interno del servidor";

    private ApiError buildError(
        HttpStatus status,
        String message,
        String path,
        List<String> details
    ) {
        return new ApiError(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            path,
            details
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        List<String> details = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(error -> {
                if (error == null) {
                    return DEFAULT_INVALID_VALUE_MESSAGE;
                }
                String defaultMessage = Objects.toString(error.getDefaultMessage(), DEFAULT_INVALID_VALUE_MESSAGE);
                if (error instanceof FieldError fieldError) {
                    return fieldError.getField() + ": " + defaultMessage;
                }
                return defaultMessage;
            })
            .toList();

        return ResponseEntity.badRequest().body(
            buildError(HttpStatus.BAD_REQUEST, DEFAULT_VALIDATION_MESSAGE, request.getRequestURI(), details)
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(
            buildError(
                HttpStatus.BAD_REQUEST,
                "Body JSON invalido",
                request.getRequestURI(),
                Collections.emptyList()
            )
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(
        MethodArgumentTypeMismatchException ex,
        HttpServletRequest request
    ) {
        String parameterName = ex.getName() == null ? "parametro" : ex.getName();
        return ResponseEntity.badRequest().body(
            buildError(
                HttpStatus.BAD_REQUEST,
                "El parametro " + parameterName + " tiene un formato invalido",
                request.getRequestURI(),
                Collections.emptyList()
            )
        );
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ApiError> handleInvalidProductException(
        InvalidProductException ex,
        HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(
            buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), Collections.emptyList())
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFoundException(
        ProductNotFoundException ex,
        HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), Collections.emptyList())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            buildError(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR_MESSAGE, request.getRequestURI(), Collections.emptyList())
        );
    }
}