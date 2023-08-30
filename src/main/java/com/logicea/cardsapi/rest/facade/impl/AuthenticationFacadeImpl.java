package com.logicea.cardsapi.rest.facade.impl;

import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.exception.AuthenticationException;
import com.logicea.cardsapi.rest.dto.request.LoginRequest;
import com.logicea.cardsapi.rest.dto.response.TokenResponse;
import com.logicea.cardsapi.rest.facade.AuthenticationFacade;
import com.logicea.cardsapi.security.TokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationFacadeImpl implements AuthenticationFacade {
    private final TokenManager tokenManager;
    private final AuthenticationManager authenticationManager;
    /**
     * Create authentication token response.
     *
     * @param request the request
     * @return the token response
     * @throws AuthenticationException the authentication exception
     */
    @Override
    public TokenResponse createAuthentication(LoginRequest request) throws AuthenticationException {
        final Authentication authentication = this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmailAddress(), request.getPassword()));
        if (!authentication.isAuthenticated()) {
            log.warn(String.format(ErrorCode.ERROR_1001.getMessage(), request.getEmailAddress()));
            throw new AuthenticationException(
                    String.format(ErrorCode.ERROR_1001.getMessage(), request.getEmailAddress()));
        }
        String token = this.tokenManager.generateToken(request.getEmailAddress());
        return TokenResponse.builder()
                .token(token)
                .build();
    }
}
