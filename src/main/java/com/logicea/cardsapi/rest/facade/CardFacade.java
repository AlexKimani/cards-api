package com.logicea.cardsapi.rest.facade;

import com.logicea.cardsapi.rest.dto.request.CardDeleteRequest;
import com.logicea.cardsapi.rest.dto.request.CardRequest;
import com.logicea.cardsapi.rest.dto.response.CardDeletionResponse;
import com.logicea.cardsapi.rest.dto.response.CardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardFacade {
    /**
     * Gets all cards by user.
     *
     * @param pageable the pageable
     * @return the all cards by user
     */
    Page<CardResponse> getAllCards(Pageable pageable);

    /**
     * Gets all cards created by user.
     *
     * @param sortField the sort field
     * @param pageable  The Page setup details
     * @return the all cards created by user
     */
    Page<CardResponse> getAllCardsCreatedByUser(Pageable pageable);

    /**
     * Create new card.
     *
     * @param card the card Entity
     * @return the card
     */
    CardResponse createCard(CardRequest card);

    /**
     * Gets card by id.
     *
     * @param id the id
     * @return the card by id
     */
    CardResponse getCardById(long id);

    /**
     * Update card.
     *
     * @param id the card id
     * @param card the card
     * @return the card
     */
    CardResponse updateCard(long id, CardRequest request);

    /**
     * Delete card.
     *
     * @param card the card
     */
    CardDeletionResponse deleteCard(CardDeleteRequest request);

    /**
     * Delete all cards.
     */
    CardDeletionResponse deleteAllCards();
}
