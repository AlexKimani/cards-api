package com.logicea.cardsapi.rest.facade;

import com.logicea.cardsapi.exception.AuthenticationException;
import com.logicea.cardsapi.rest.dto.request.LoginRequest;
import com.logicea.cardsapi.rest.dto.response.TokenResponse;

/**
 * The interface Authentication facade.
 */
public interface AuthenticationFacade {
    /**
     * Create authentication token response.
     *
     * @param request the request
     * @return the token response
     * @throws AuthenticationException the authentication exception
     */
    TokenResponse createAuthentication(LoginRequest request) throws AuthenticationException;
}
