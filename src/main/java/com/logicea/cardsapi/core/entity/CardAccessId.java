package com.logicea.cardsapi.core.entity;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class CardAccessId implements Serializable {
    private Long user;
    private Long card;

    // Equals and hashCode methods
}
