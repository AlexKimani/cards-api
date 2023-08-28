package com.logicea.cardsapi.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "card_access")
@IdClass(CardAccessId.class)
public class CardAccess {
    @Id
    @ManyToOne
    private User user;

    @Id
    @ManyToOne
    private Card card;

    public CardAccess() {
        super();
    }

    // Getters and setters
}
