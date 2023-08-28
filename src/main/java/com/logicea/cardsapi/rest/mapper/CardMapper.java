package com.logicea.cardsapi.rest.mapper;

import com.logicea.cardsapi.core.entity.Card;
import com.logicea.cardsapi.core.enums.CardStatus;
import com.logicea.cardsapi.rest.dto.request.CardRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardMapper {
    public static Card createCardEntity(CardRequest request) {
        Card card = new Card();
        card.setName(request.getName());
        card.setDescription(request.getDescription() != null ? request.getDescription() : "");
        card.setColor(request.getColor() != null ? request.getColor() : "");
        card.setStatus(request.getStatus() != null ? CardStatus.fromString(request.getStatus()) : CardStatus.TO_DO);
        return card;
    }
}
