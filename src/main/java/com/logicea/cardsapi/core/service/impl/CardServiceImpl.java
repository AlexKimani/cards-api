package com.logicea.cardsapi.core.service.impl;

import com.logicea.cardsapi.core.entity.Card;
import com.logicea.cardsapi.core.entity.User;
import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.core.repository.CardRepository;
import com.logicea.cardsapi.core.repository.UserRepository;
import com.logicea.cardsapi.core.service.CardService;
import com.logicea.cardsapi.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    /**
     * Gets all cards by user.
     *
     * @return the all cards by user
     */
    @Override
    public Page<Card> getAllCards(Pageable pageable) {
        return this.cardRepository.findAll(pageable);
    }

    /**
     * Gets all cards created by user.
     *
     * @param username the username
     * @param sortField the sort field
     * @param pageable The Page setup details
     * @return the all cards created by user
     */
    @Override
    public Page<Card> getAllCardsCreatedByUser(String username, Pageable pageable) {
        User user = this.userRepository.findUserByEmail(username)
                .orElseThrow(() -> new ServiceException(String.format(ErrorCode.ERROR_1000.getMessage(), username)));
        return this.cardRepository.findCardsByCreatedBy(user, pageable);
    }

    /**
     * Create new card.
     *
     * @param card the card Entity
     * @return the card
     */
    @Override
    public Card createCard(Card card) {
        return this.cardRepository.save(card);
    }

    /**
     * Gets card by id.
     *
     * @param id the id
     * @return the card by id
     */
    @Override
    public Optional<Card> getCardById(long id) {
        return this.cardRepository.findCardById(id);
    }

    /**
     * Gets card by id and user id.
     *
     * @param id     the id
     * @param userid the userid
     * @return the card by id and user id
     */
    @Override
    public Optional<Card> getCardByIdAndUserId(long id, long userid) {
        User user = this.userRepository.findById(userid)
                .orElseThrow(() -> new ServiceException(String.format(ErrorCode.ERROR_1000.getMessage(), userid)));
        return this.cardRepository.findCardByIdAndCreatedBy(id, user);
    }

    /**
     * Update card entity.
     *
     * @param card the card
     * @return the card
     */
    @Override
    public Card updateCard(Card card) {
        this.cardRepository.findCardById(card.getId());
        return this.cardRepository.save(card);
    }

    /**
     * Delete card.
     *
     * @param card the card
     */
    @Override
    public void deleteCard(Card card) {
        this.cardRepository.delete(card);
    }

    /**
     * Delete all cards.
     */
    @Override
    public void deleteAllCards() {
        this.cardRepository.deleteAll();
    }
}
