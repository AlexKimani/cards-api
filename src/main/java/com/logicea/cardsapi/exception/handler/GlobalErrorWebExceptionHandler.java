package com.logicea.cardsapi.exception.handler;

import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.exception.AuthenticationException;
import com.logicea.cardsapi.exception.ServiceException;
import com.logicea.cardsapi.rest.dto.response.ApiErrorResponse;
import com.logicea.cardsapi.rest.dto.response.error.ApiSubError;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalErrorWebExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(toList());
        log.error("Validation error occurred: {}", ex.getMessage());

        return new ResponseEntity<>(setApiResponse("Validation error occurred", ErrorCode.ERROR_1400.getCode(),errors),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private ApiErrorResponse setApiResponse(String message, String statusCode, List<String> errors) {
        List<ApiSubError> subErrors = new ArrayList<>();
        errors.forEach(error -> {
            ApiSubError subError = new ApiSubError();
            subError.setMessage(error);
            subErrors.add(subError);
        });
        return ApiErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .status(statusCode)
                .message(message)
                .code(statusCode)
                .subErrors(subErrors)
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Invalid Authentication Exception occurred: {}", ex.getMessage(),ex);
        return new ResponseEntity<>(setApiResponse("Invalid Authentication Exception error occurred: "+ ErrorCode.ERROR_1007.getMessage(),
                ErrorCode.ERROR_1007.getCode(), errors), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Expired Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(setApiResponse(ErrorCode.ERROR_1006.getMessage(),
                ErrorCode.ERROR_1006.getCode(), errors), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("No Handler Found Exception occurred: URL: {}",request.getContextPath());
        return new ResponseEntity<>(setApiResponse(String.format(ErrorCode.ERROR_1404.getMessage(), request.getContextPath()),
                ErrorCode.ERROR_1404.getCode(), errors), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleServiceException(ServiceException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Service Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(setApiResponse(ErrorCode.ERROR_1500.getMessage(),
                ErrorCode.ERROR_1500.getCode(), errors), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Authentication Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(setApiResponse(ErrorCode.ERROR_1007.getMessage(),
                ErrorCode.ERROR_1007.getCode(), errors), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Illegal Argument Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(setApiResponse("Illegal Argument Exception occurred ",
                ErrorCode.ERROR_1500.getCode(), errors), new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Resource Not Found Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(setApiResponse(String.format(ErrorCode.ERROR_1500.getMessage(), request.getMethod()),
                ErrorCode.ERROR_1500.getCode(), errors),
                new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiErrorResponse> handleGeneralExceptions(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("GeneralException occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(setApiResponse(ErrorCode.ERROR_1500.getMessage(),ErrorCode.ERROR_1500.getCode(), errors),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ApiErrorResponse> handleRuntimeExceptions(RuntimeException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Runtime Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(setApiResponse("Runtime Exception occurred", ErrorCode.ERROR_1500.getCode(), errors),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}