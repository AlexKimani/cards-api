package com.logicea.cardsapi.core.service;

import com.logicea.cardsapi.rest.dto.request.LoginRequest;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The interface User details service.
 */
public interface UserDetailsService {
    /**
     * Load user by user details user details.
     *
     * @param loginRequest the login request
     * @return the user details
     */
    UserDetails loadUserByUserDetails(LoginRequest loginRequest);

    /**
     * Load user by username.
     *
     * @param userEmail the user email
     * @return the user details
     */
    UserDetails loadUserByUserName(String userEmail);
}
