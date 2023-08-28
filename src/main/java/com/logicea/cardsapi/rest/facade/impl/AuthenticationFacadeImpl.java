package com.logicea.cardsapi.rest.facade.impl;

import com.logicea.cardsapi.core.service.UserDetailsService;
import com.logicea.cardsapi.exception.AuthenticationException;
import com.logicea.cardsapi.rest.dto.request.LoginRequest;
import com.logicea.cardsapi.rest.dto.response.TokenResponse;
import com.logicea.cardsapi.rest.facade.AuthenticationFacade;
import com.logicea.cardsapi.security.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationFacadeImpl implements AuthenticationFacade {
    private final TokenManager tokenManager;
    private final UserDetailsService userDetailsService;

    /**
     * Create authentication token response.
     *
     * @param request the request
     * @return the token response
     * @throws AuthenticationException the authentication exception
     */
    @Override
    public TokenResponse createAuthentication(LoginRequest request) throws AuthenticationException {
        final UserDetails userDetails = this.userDetailsService.loadUserByUserDetails(request);
        String token = this.tokenManager.generateToken(userDetails);
        return TokenResponse.builder()
                .token(token)
                .build();
    }
}
