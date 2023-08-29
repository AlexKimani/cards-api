package com.logicea.cardsapi.security.filter;

import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.core.service.impl.UserService;
import com.logicea.cardsapi.exception.AuthenticationException;
import com.logicea.cardsapi.security.TokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Component
public class ApiRequestFilter extends OncePerRequestFilter {
    private TokenManager tokenManager;
    private UserService userDetailsService;

    private static final String AUTHENTICATION_HEADER = "Authorization";
    private static final String AUTHENTICATION_PREFIX = "Bearer ";


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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authenticationToken = request.getHeader(AUTHENTICATION_HEADER);
        log.debug("Request URI: {} : Headers: {}", request.getRequestURI(), this.getRequestHeaders(request));
        if (authenticationToken != null || !request.getRequestURI().contains("authenticate")) {
            // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
            assert authenticationToken != null;
            if (!authenticationToken.startsWith(AUTHENTICATION_PREFIX)) {
                log.warn(String.format(ErrorCode.ERROR_1004.getCode()) + "\nHeaders: {}", request.getHeaderNames());
                throw new AuthenticationException(String.format(ErrorCode.ERROR_1004.getCode()));
            }
            authenticationToken = authenticationToken.replace(AUTHENTICATION_PREFIX, "");

            // Get token from username
            String username = this.getUsername(authenticationToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                this.setAuthenticationContext(authenticationToken, userDetails, request);
                MDC.put("Current Logged-In User:", username);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(String authenticationToken, UserDetails userDetails,
                                          HttpServletRequest request) {
        // it the token is valid, configure Spring to manually set authentication
        if (Boolean.TRUE.equals(this.tokenManager.validateToken(authenticationToken, userDetails))) {
            UsernamePasswordAuthenticationToken passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            passwordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource()
                    .buildDetails(request));
            // After setting the Authentication in the context, we specify
            // that the current user is authenticated. So it passes the
            // Spring Security Configurations successfully.
            SecurityContextHolder.getContext().setAuthentication(passwordAuthenticationToken);
        } else {
            log.error("Invalid token supplied : {}", authenticationToken);
            throw new AuthenticationException(ErrorCode.ERROR_1007.getMessage());
        }
    }

    private String getUsername(String authenticationToken) {
        return this.tokenManager.extractUsername(authenticationToken);
    }

    private HttpHeaders getRequestHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<?> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Enumeration<?> values = request.getHeaders(name);
            while (values.hasMoreElements()) {
                headers.add(name, (String) values.nextElement());
            }
        }
        return headers;
    }

    @Autowired
    public void setUserDetailsService(UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }
}
