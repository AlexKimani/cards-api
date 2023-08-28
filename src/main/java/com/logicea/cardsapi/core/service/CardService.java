package com.logicea.cardsapi.core.service;

import com.logicea.cardsapi.core.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * The interface Card service.
 */
public interface CardService {
    /**
     * Gets all cards by user.
     *
     * @param username the username
     * @param pageable the pageable
     * @return the all cards by user
     */
    Page<Card> getAllCards(String username, Pageable pageable);

    /**
     * Gets all cards created by user.
     *
     * @param username  the username
     * @param sortField the sort field
     * @param pageable  The Page setup details
     * @return the all cards created by user
     */
    Page<Card> getAllCardsCreatedByUser(String username, String sortField, Pageable pageable);

    /**
     * Create new card.
     *
     * @param card the card Entity
     * @return the card
     */
    Card createCard(Card card);

    /**
     * Gets card by id.
     *
     * @param id the id
     * @return the card by id
     */
    Optional<Card> getCardById(long id);

    /**
     * Update card.
     *
     * @param card the card
     * @return the card
     */
    Card updateCard(Card card);

    /**
     * Delete card.
     *
     * @param card the card
     */
    void deleteCard(Card card);

    /**
     * Delete all cards.
     */
    void deleteAllCards();
}
