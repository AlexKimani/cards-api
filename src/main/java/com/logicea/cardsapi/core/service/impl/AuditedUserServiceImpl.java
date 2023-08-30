package com.logicea.cardsapi.core.service.impl;

import com.logicea.cardsapi.core.service.AuditedUserService;
import com.logicea.cardsapi.security.models.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * The type Audited user service.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditedUserServiceImpl implements AuditedUserService {
    /**
     * Gets current user id.-
     *
     * @return the current user id
     */
    @Override
    public Long getCurrentUserId() {
        UserInfo userDetails = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }

    /**
     * Gets current user.
     *
     * @return the current user
     */
    @Override
    public String getCurrentUser() {
        UserInfo userDetails = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * Check if current user is admin boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean checkIfCurrentUserIsAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication object: {}", authentication);
        return authentication != null && authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority()
                        .equalsIgnoreCase("ROLE_ADMIN"));
    }
}
