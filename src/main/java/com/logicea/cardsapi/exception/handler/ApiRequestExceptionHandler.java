package com.logicea.cardsapi.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.rest.dto.response.ApiErrorResponse;
import com.logicea.cardsapi.rest.dto.response.error.ApiSubError;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiRequestExceptionHandler extends OncePerRequestFilter {
    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException ex) {
            List<String> errors = Collections.singletonList(ex.getMessage());
            List<ApiSubError> subErrors = new ArrayList<>();
            errors.forEach(error -> {
                ApiSubError subError = new ApiSubError();
                subError.setMessage(error);
                subErrors.add(subError);
            });
            ObjectMapper mapper = new ObjectMapper();
            ApiErrorResponse apiResponse = ApiErrorResponse.builder()
                    .timestamp(System.currentTimeMillis())
                    .code(ErrorCode.ERROR_1006.getCode())
                    .message(ErrorCode.ERROR_1006.getMessage())
                    .status(HttpStatus.UNAUTHORIZED.toString())
                    .subErrors(subErrors)
                    .build();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getOutputStream().print(mapper.writeValueAsString(apiResponse));
        }
    }
}
