package com.logicea.cardsapi.exception.handler;

import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.exception.AuthenticationException;
import com.logicea.cardsapi.exception.CardNotFoundException;
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

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalErrorWebExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(toList());
        log.error("Validation error occurred: {}{}", ex.getMessage(), new ErrorLogger(request));

        return new ResponseEntity<>(setApiResponse(ErrorCode.ERROR_1400.getMessage(), ErrorCode.ERROR_1400.getCode(),errors),
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
        log.error("Invalid Authentication Exception occurred: {}{}", ex.getMessage(), new ErrorLogger(request));
        return new ResponseEntity<>(setApiResponse(ErrorCode.ERROR_1007.getMessage(),
                ErrorCode.ERROR_1007.getCode(), errors), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Expired Exception occurred: {}{}", ex.getMessage(), new ErrorLogger(request));
        return new ResponseEntity<>(setApiResponse(ErrorCode.ERROR_1006.getMessage(),
                ErrorCode.ERROR_1006.getCode(), errors), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("No Handler Found Exception occurred: {}{}",ex.getMessage(), new ErrorLogger(request));
        return new ResponseEntity<>(setApiResponse(String.format(ErrorCode.ERROR_1404.getMessage(), request.getContextPath()),
                ErrorCode.ERROR_1404.getCode(), errors), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleServiceException(ServiceException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Service Exception occurred: {}{}", ex.getMessage(), new ErrorLogger(request));
        return new ResponseEntity<>(setApiResponse(String.format(ErrorCode.ERROR_1500.getMessage(), ex.getMessage()),
                ErrorCode.ERROR_1500.getCode(), errors), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Authentication Exception occurred: {}{}", ex.getMessage(), new ErrorLogger(request));
        return new ResponseEntity<>(setApiResponse(ErrorCode.ERROR_1007.getMessage(),
                ErrorCode.ERROR_1007.getCode(), errors), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCardNotFoundException(CardNotFoundException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("CardNotFoundException Exception occurred: {}{}", ex.getMessage(), new ErrorLogger(request));
        return new ResponseEntity<>(setApiResponse(ErrorCode.ERROR_1604.getMessage(),
                ErrorCode.ERROR_1604.getCode(), errors), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Illegal Argument Exception occurred: {}{}", ex.getMessage(), new ErrorLogger(request));
        return new ResponseEntity<>(setApiResponse(String.format(ErrorCode.ERROR_1500.getMessage(), ex.getMessage()),
                ErrorCode.ERROR_1500.getCode(), errors), new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Resource Not Found Exception occurred: {}{}", ex.getMessage(), new ErrorLogger(request));
        return new ResponseEntity<>(setApiResponse(String.format(ErrorCode.ERROR_1405.getMessage(), request.getMethod()),
                ErrorCode.ERROR_1405.getCode(), errors),
                new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiErrorResponse> handleGeneralExceptions(Exception ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("GeneralException occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(setApiResponse(String.format(ErrorCode.ERROR_1500.getMessage(), ex.getMessage()),
                ErrorCode.ERROR_1500.getCode(), errors),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ApiErrorResponse> handleRuntimeExceptions(RuntimeException ex, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Runtime Exception occurred: {}{}", ex.getMessage(), new ErrorLogger(request));
        return new ResponseEntity<>(setApiResponse("Runtime Exception occurred", ErrorCode.ERROR_1500.getCode(), errors),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static class ErrorLogger implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private final Timestamp timestamp;
        private final String requestUrl;
        private final String method;
        private final String headers;
        private final String parameters;

        public ErrorLogger(HttpServletRequest request) {
            this.timestamp = new Timestamp(System.currentTimeMillis());
            this.requestUrl = request.getRequestURI();
            this.method = request.getMethod();
            this.headers = this.getRequestHeaders(request);
            this.parameters = this.setParameters(request.getParameterMap());
        }

        private String getRequestHeaders(HttpServletRequest request) {
            StringBuilder builder = new StringBuilder(10000);
            Enumeration<?> names = request.getHeaderNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                Enumeration<?> values = request.getHeaders(name);
                while (values.hasMoreElements()) {
                    builder.append(String.format("%s  :  %s%n",name, values.nextElement()));
                }
            }
            return builder.toString();
        }

        private String setParameters(Map<String, String[]> parametersMap) {
            return parametersMap.entrySet()
                    .stream()
                    .map(entry -> String.format("%s = %s %n", entry.getKey(), Arrays.asList(entry.getValue())))
                    .collect(Collectors.joining());
        }

        @Override
        public String toString() {
            return "%nTimestamp  :  %s%nRequestURL :  %s%nMethod     :  %s%nHeaders  :  %s%nParameters :  %s%n"
                    .formatted(timestamp, requestUrl, method, headers, parameters);
        }
    }
}