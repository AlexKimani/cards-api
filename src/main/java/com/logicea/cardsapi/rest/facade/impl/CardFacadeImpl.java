package com.logicea.cardsapi.rest.facade.impl;

import com.logicea.cardsapi.rest.dto.request.CardDeleteRequest;
import com.logicea.cardsapi.rest.dto.request.CardRequest;
import com.logicea.cardsapi.rest.dto.response.CardResponse;
import com.logicea.cardsapi.rest.facade.CardFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class CardFacadeImpl implements CardFacade {

    /**
     * Gets all cards by user.
     *
     * @param username the username
     * @param pageable the pageable
     * @return the all cards by user
     */
    @Override
    public Page<CardResponse> getAllCards(String username, Pageable pageable) {
        return null;
    }

    /**
     * Gets all cards created by user.
     *
     * @param username  the username
     * @param sortField the sort field
     * @param pageable  The Page setup details
     * @return the all cards created by user
     */
    @Override
    public Page<CardResponse> getAllCardsCreatedByUser(String username, String sortField, Pageable pageable) {
        return null;
    }

    /**
     * Create new card.
     *
     * @param card the card Entity
     * @return the card
     */
    @Override
    public CardResponse createCard(CardRequest card) {
        return null;
    }

    /**
     * Gets card by id.
     *
     * @param id the id
     * @return the card by id
     */
    @Override
    public CardResponse getCardById(long id) {
        return null;
    }

    /**
     * Update card.
     *
     * @param request@return the card
     */
    @Override
    public CardResponse updateCard(CardRequest request) {
        return null;
    }

    /**
     * Delete card.
     *
     * @param request
     */
    @Override
    public void deleteCard(CardDeleteRequest request) {

    }

    /**
     * Delete all cards.
     */
    @Override
    public void deleteAllCards() {

    }
}
