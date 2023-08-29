package com.logicea.cardsapi.rest.facade.impl;

import com.logicea.cardsapi.core.entity.Card;
import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.core.service.AuditedUserService;
import com.logicea.cardsapi.core.service.CardService;
import com.logicea.cardsapi.exception.CardNotFoundException;
import com.logicea.cardsapi.rest.dto.request.CardDeleteRequest;
import com.logicea.cardsapi.rest.dto.request.CardRequest;
import com.logicea.cardsapi.rest.dto.response.CardDeletionResponse;
import com.logicea.cardsapi.rest.dto.response.CardResponse;
import com.logicea.cardsapi.rest.facade.CardFacade;
import com.logicea.cardsapi.rest.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardFacadeImpl implements CardFacade {
    private final CardService cardService;
    private final AuditedUserService auditedUserService;

    /**
     * Gets all cards by user.
     *
     * @param pageable the pageable
     * @return the all cards by user
     */
    @Override
    public Page<CardResponse> getAllCards(Pageable pageable) {
        Page<Card> cards = this.cardService.getAllCards(pageable);
        return CardMapper.getPagedCardResponse(cards);
    }

    /**
     * Gets all cards created by user.
     *
     * @param sortField the sort field
     * @param pageable  The Page setup details
     * @return the all cards created by user
     */
    @Override
    public Page<CardResponse> getAllCardsCreatedByUser(String sortField, Pageable pageable) {
        Page<Card> cards = this.cardService.getAllCardsCreatedByUser(this.auditedUserService.getCurrentUser(), sortField, pageable);
        return CardMapper.getPagedCardResponse(cards);
    }

    /**
     * Create new card.
     *
     * @param card the card Entity
     * @return the card
     */
    @Override
    public CardResponse createCard(CardRequest card) {
        log.info("New card details to be created: {}", card);
        Card cardEntity = CardMapper.createCardEntity(card);
        CardResponse response = CardMapper.getCardResponse(
                this.cardService.createCard(cardEntity));
        log.info("Created card details: {}", response);
        return response;
    }

    /**
     * Gets card by id.
     *
     * @param id the id
     * @return the card by id
     */
    @Override
    public CardResponse getCardById(long id) {
        Card card = this.getCardObjectById(id);
        return CardMapper.getCardResponse(card);
    }

    /**
     * Update card.
     *
     * @param request
     * @return the card
     */
    @Override
    public CardResponse updateCard(long id, CardRequest request) {
        log.info("Update card request details: {}", request);
        Card existingCard = this.getCardObjectById(id);
        CardMapper.updateCardDetails(request, existingCard);
        Card updatedCard = this.cardService.updateCard(existingCard);
        CardResponse updatedResponse = CardMapper.getCardResponse(updatedCard);
        log.info("Updated card details: {}", updatedResponse);
        return updatedResponse;
    }

    /**
     * Delete card.
     *
     * @param request the card deletion request object
     * @return CardDeletionResponse
     */
    @Override
    public CardDeletionResponse deleteCard(CardDeleteRequest request) {
        Card card = this.getCardObjectById(request.getId());
        this.cardService.deleteCard(card);
        return CardMapper.setCardDeletionResponse(request.getId());
    }

    /**
     * Delete all cards.
     * @return CardDeletionResponse
     */
    @Override
    public CardDeletionResponse deleteAllCards() {
        log.info("Admin User: {} is about to delete records.", this.auditedUserService.getCurrentUser());
        this.cardService.deleteAllCards();
        return CardMapper.setDeleteAllCardsResponse();
    }

    private Card getCardObjectById(long id) {
        log.info("Get Card by ID: id: {} : userid: {} : username: {}", id, this.auditedUserService.getCurrentUserId(), this.auditedUserService.getCurrentUser());
        Card existingCard;
        if (!this.auditedUserService.checkIfCurrentUserIsAdmin()) {
            existingCard = this.cardService.getCardByIdAndUserId(id, this.auditedUserService.getCurrentUserId())
                    .orElseThrow(() -> new CardNotFoundException(String.format(ErrorCode.ERROR_1604.getMessage())));
        } else {
            existingCard = this.cardService.getCardById(id)
                    .orElseThrow(() -> new CardNotFoundException(String.format(ErrorCode.ERROR_1604.getMessage())));
        }
        return existingCard;
    }
}
