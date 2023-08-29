package com.logicea.cardsapi.core.service;

/**
 * The interface Audited user service.
 */
public interface AuditedUserService {
    /**
     * Gets current user id.
     *
     * @return the current user id
     */
    Long getCurrentUserId();

    /**
     * Gets current user.
     *
     * @return the current user
     */
    String getCurrentUser();

    /**
     * Check if current user is admin boolean.
     *
     * @return the boolean
     */
    boolean checkIfCurrentUserIsAdmin();
}
