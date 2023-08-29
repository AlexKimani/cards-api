package com.logicea.cardsapi.integration.rest.facade.impl;

import com.logicea.cardsapi.AbstractIntegrationTest;
import com.logicea.cardsapi.core.service.impl.UserDetailsServiceImpl;
import com.logicea.cardsapi.rest.dto.request.LoginRequest;
import com.logicea.cardsapi.rest.dto.response.TokenResponse;
import com.logicea.cardsapi.rest.facade.impl.AuthenticationFacadeImpl;
import com.logicea.cardsapi.security.TokenManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AuthenticationFacadeImplTest extends AbstractIntegrationTest {
    @Autowired
    private AuthenticationFacadeImpl authenticationFacade;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private TokenManager tokenManager;

    private LoginRequest loginRequest;
    @BeforeEach
    void setUp() {
        this.loginRequest = this.setLoginRequest();
    }

    @AfterEach
    void tearDown() {
        this.loginRequest = null;
    }

    @Test
    @DisplayName(value = "Given a valid Login request object, should generate a valid authentication token")
    void testGivenAValidLoginRequestObjectShouldGenerateAValidAuthenticationToken() throws Exception {
        TokenResponse tokenResponse = this.authenticationFacade.createAuthentication(this.loginRequest);
        log.info("Token Response: {}", tokenResponse);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(this.loginRequest.getEmailAddress());
        log.info("Token Validation: {}", this.tokenManager.validateToken(tokenResponse.getToken(), userDetails));
    }

    private LoginRequest setLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setPassword("password");
        request.setEmailAddress("admin@test.com");
        return request;
    }
}