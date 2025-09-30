package dev.jake.lab6_api.controllers;

import dev.jake.lab6_api.exceptions.DuplicateResourceException;
import dev.jake.lab6_api.exceptions.ResourceNotFoundException;
import dev.jake.lab6_api.models.dto.http.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex,
                                                           HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                             HttpServletRequest request) {
        String message = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        return buildResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                                                                  HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateItem(DuplicateResourceException ex,
                                                                HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
    }

    // not-null postgres violation
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex,
                                                                HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT,
                "Database constraint violation: " + ex.getMostSpecificCause().getMessage(),
                request.getRequestURI());
    }


    // catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeError(RuntimeException ex,
                                                               HttpServletRequest request) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // helper for building responses
    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status,
                                                           String message,
                                                           String path
                                                           ){
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                message,
                status.value(),
                LocalDateTime.now(),
                status.getReasonPhrase(),
                path
        );

        return ResponseEntity.status(status).body(errorResponse);
    }
}
