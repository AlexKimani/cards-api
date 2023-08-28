package com.logicea.cardsapi.core.enums;

public enum ErrorCode {
    ERROR_1000("1000", "User Details Not Found for username %s."),
    ERROR_1001("1001", "User %s Not Authenticated."),
    ERROR_1002("1002", "User %s Not Authorized."),
    ERROR_1003("1003", "Invalid request; Missing required header %s"),
    ERROR_1004("1004", "Invalid Authentication Prefix."),
    ERROR_1005("1005", "Failed to extract username from token."),
    ERROR_1006("1006", "Failed: Expired token provided."),
    ERROR_1007("1007", "Authentication failed: Invalid or expired token"),
    ERROR_1400("1400", "Validation error occurred,"),
    ERROR_1404("1404", "Handler not found for URL resource: %s"),
    ERROR_1500("1500", "Unexpected error occurred: %s"),
    ERROR_1501("1501", "Offset index must not be less than zero!"),
    ERROR_1502("1502", "Limit must not be less than one!"),
    ERROR_1604("ERROR_1604", "This card does not exist."),
    ERROR_1609("ERROR_1609", "This card name already exists.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
