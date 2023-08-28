package com.logicea.cardsapi.exception.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.rest.dto.response.ApiErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        log.error("Access Denied for request details: {}", request);
        createFailedAuthorizationErrorResponse(response);
    }

    static void createFailedAuthorizationErrorResponse(HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ApiErrorResponse apiResponse = ApiErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .code(ErrorCode.ERROR_1007.getCode())
                .message(ErrorCode.ERROR_1007.getMessage())
                .status(HttpStatus.UNAUTHORIZED.toString())
                .build();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getOutputStream().print(mapper.writeValueAsString(apiResponse));
    }
}
