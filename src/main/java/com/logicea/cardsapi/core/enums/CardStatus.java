package com.logicea.cardsapi.core.enums;

import java.util.Arrays;

public enum CardStatus {
    TO_DO("To Do"), IN_PROGRESS("In Progress"), DONE("Done");

    private final String value;
    CardStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CardStatus fromString(String value) {
        return Arrays.stream(CardStatus.values())
                .filter(env -> env.getValue().equalsIgnoreCase(value))
                .findFirst().orElse(CardStatus.TO_DO);
    }
}
