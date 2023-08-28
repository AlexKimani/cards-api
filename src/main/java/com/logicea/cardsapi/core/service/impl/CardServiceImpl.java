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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
     * @param username the username
     * @return the all cards by user
     */
    @Override
    public Page<Card> getAllCards(String username, Pageable pageable) {
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
    public Page<Card> getAllCardsCreatedByUser(String username, String sortField, Pageable pageable) {
        User user = this.userRepository.findUserByEmail(username)
                .orElseThrow(() -> new ServiceException(String.format(ErrorCode.ERROR_1000.getMessage(), username)));
        List<Card> userCards = new ArrayList<>(user.getCards().stream().toList());
        if ("name".equalsIgnoreCase(sortField)) {
            userCards.sort(Card.nameComparator);
        } else if ("color".equalsIgnoreCase(sortField)) {
            userCards.sort(Card.colorComparator);
        } else if ("status".equalsIgnoreCase(sortField)) {
            userCards.sort(Card.statusComparator);
        } else if ("date_created".equalsIgnoreCase(sortField)) {
            userCards.sort(Card.dateCreatedComparator);
        } else {
            Collections.sort(userCards);
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), userCards.size());
        List<Card> pageContent = userCards.subList(start, end);
        return new PageImpl<>(pageContent, pageable, userCards.size());
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
        this.cardRepository.findCardById(card.getId());
    }

    /**
     * Delete all cards.
     */
    @Override
    public void deleteAllCards() {
        this.cardRepository.deleteAll();
    }
}
