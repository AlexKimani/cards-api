package com.logicea.cardsapi.unit.rest.facade.impl;

import com.logicea.cardsapi.core.entity.Card;
import com.logicea.cardsapi.core.entity.User;
import com.logicea.cardsapi.core.enums.CardStatus;
import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.core.service.AuditedUserService;
import com.logicea.cardsapi.core.service.CardService;
import com.logicea.cardsapi.core.service.impl.AuditedUserServiceImpl;
import com.logicea.cardsapi.rest.dto.request.CardDeleteRequest;
import com.logicea.cardsapi.rest.dto.request.CardRequest;
import com.logicea.cardsapi.rest.dto.response.CardDeletionResponse;
import com.logicea.cardsapi.rest.dto.response.CardResponse;
import com.logicea.cardsapi.rest.facade.impl.CardFacadeImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CardFacadeImplTest {
    @Mock
    private AuditedUserService auditedUserService;

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardFacadeImpl cardFacade;

    private Pageable pageable;
    private Card card;
    private User user;
    private CardRequest cardRequest;

    @BeforeEach
    void setUp() {
        this.pageable = PageRequest.of(1, 5);
        this.card = this.setCard();
        this.user = this.setUser();
        this.cardRequest = this.setCardRequest();
    }

    @AfterEach
    void tearDown() {
        this.pageable = null;
        this.card = null;
        this.user = null;
        this.cardRequest = null;
    }

    @Test
    @DisplayName(value = "Given a pageable object should return a Paged cards list")
    void getAllCards() {
        doReturn(this.setPagedCards(this.pageable)).when(this.cardService).getAllCards(this.pageable);
        Page<Card> cards = assertDoesNotThrow(() -> this.cardService.getAllCards(this.pageable));
        verify(this.cardService, times(1)).getAllCards(pageable);
        assertNotNull(cards);
    }

    @Test
    void getAllCardsCreatedByUser() {
        doReturn(this.user.getEmail()).when(this.auditedUserService).getCurrentUser();
        doReturn(this.setPagedCards(pageable)).when(this.cardService).getAllCardsCreatedByUser(this.user.getEmail(), this.pageable);
        Page<CardResponse> cards = assertDoesNotThrow(() -> this.cardFacade.getAllCardsCreatedByUser(this.pageable));
        verify(this.cardService, times(1)).getAllCardsCreatedByUser(anyString(), any(Pageable.class));
        assertNotNull(cards);
    }

    @Test
    void createCard() {
        doReturn(this.card).when(this.cardService).createCard(this.card);
        CardResponse response = assertDoesNotThrow(() -> this.cardFacade.createCard(this.setCardRequest()));
        verify(this.cardService, times(1)).createCard(any(Card.class));
        assertNotNull(response);
        assertEquals(this.card.getName(), response.getName());
    }

    @Test
    void getCardByIdForNormalUser() {
        doReturn(1L).when(this.auditedUserService).getCurrentUserId();
        doReturn(Optional.of(this.card)).when(this.cardService).getCardByIdAndUserId(1L,1L);
        CardResponse response = assertDoesNotThrow(() -> this.cardFacade.getCardById(1L));
        verify(this.cardService, times(1)).getCardByIdAndUserId(anyLong(),anyLong());
        verify(this.auditedUserService, times(2)).getCurrentUserId();
        assertNotNull(response);
    }

    @Test
    void getCardByIdForAdminUser() {
        doReturn(true).when(this.auditedUserService).checkIfCurrentUserIsAdmin();
        doReturn(Optional.of(this.card)).when(this.cardService).getCardById(1L);
        CardResponse response = assertDoesNotThrow(() -> this.cardFacade.getCardById(1L));
        verify(this.cardService, times(1)).getCardById(anyLong());
        verify(this.auditedUserService, times(1)).getCurrentUserId();
        assertNotNull(response);
    }

    @Test
    void updateCardForNormalUser() {
        doReturn(1L).when(this.auditedUserService).getCurrentUserId();
        doReturn(Optional.of(this.card)).when(this.cardService).getCardByIdAndUserId(1L,1L);
        doReturn(this.card).when(this.cardService).updateCard(this.card);
        CardResponse response = assertDoesNotThrow(() -> this.cardFacade.updateCard(1L, this.cardRequest));
        verify(this.cardService, times(1)).updateCard(any(Card.class));
        verify(this.cardService, times(1)).getCardByIdAndUserId(anyLong(),anyLong());
        verify(this.auditedUserService, times(2)).getCurrentUserId();
        assertNotNull(response);
    }

    @Test
    void updateCardForAdminUser() {
        doReturn(true).when(this.auditedUserService).checkIfCurrentUserIsAdmin();
        doReturn(Optional.of(this.card)).when(this.cardService).getCardById(1L);
        doReturn(this.card).when(this.cardService).updateCard(this.card);
        CardResponse response = assertDoesNotThrow(() -> this.cardFacade.updateCard(1L, this.cardRequest));
        verify(this.cardService, times(1)).updateCard(any(Card.class));
        verify(this.cardService, times(1)).getCardById(anyLong());
        verify(this.auditedUserService, times(1)).getCurrentUserId();
        assertNotNull(response);
    }

    @Test
    void deleteCardAsNormalUser() {
        doReturn(1L).when(this.auditedUserService).getCurrentUserId();
        doReturn(Optional.of(this.card)).when(this.cardService).getCardByIdAndUserId(1L,1L);
        doNothing().when(this.cardService).deleteCard(this.card);
        CardDeletionResponse response = assertDoesNotThrow(()-> this.cardFacade.deleteCard(this.setCardDeleteRequest()));
        verify(this.cardService, times(1)).deleteCard(any(Card.class));
        verify(this.cardService, times(1)).getCardByIdAndUserId(anyLong(),anyLong());
        verify(this.auditedUserService, times(2)).getCurrentUserId();
        assertEquals(String.format(ErrorCode.ERROR_1605.getMessage(), 1L), response.getStatus());
        assertEquals(ErrorCode.ERROR_1605.getCode(), response.getCode());
    }

    @Test
    void deleteCardAsAdminUser() {
        doReturn(true).when(this.auditedUserService).checkIfCurrentUserIsAdmin();
        doReturn(Optional.of(this.card)).when(this.cardService).getCardById(1L);
        doNothing().when(this.cardService).deleteCard(this.card);
        CardDeletionResponse response = assertDoesNotThrow(()-> this.cardFacade.deleteCard(this.setCardDeleteRequest()));
        verify(this.cardService, times(1)).deleteCard(any(Card.class));
        verify(this.cardService, times(1)).getCardById(anyLong());
        verify(this.auditedUserService, times(1)).getCurrentUserId();
        assertEquals(String.format(ErrorCode.ERROR_1605.getMessage(), 1L), response.getStatus());
        assertEquals(ErrorCode.ERROR_1605.getCode(), response.getCode());
    }

    @Test
    void deleteAllCards() {
        doNothing().when(this.cardService).deleteAllCards();
        CardDeletionResponse response = this.cardFacade.deleteAllCards();
        verify(this.cardService, times(1)).deleteAllCards();
        assertEquals(ErrorCode.ERROR_1606.getMessage(), response.getStatus());
        assertEquals(ErrorCode.ERROR_1606.getCode(), response.getCode());
    }

    private Page<Card> setPagedCards(Pageable pageable) {
        List<Card> cards = List.of(this.setCard(),this.setCard());
        return new PageImpl<>(cards, pageable, cards.size());
    }

    private Card setCard() {
        Card card = new Card();
        card.setName("test");
        card.setStatus(CardStatus.TO_DO);
        card.setCreatedBy(this.setUser());
        return card;
    }

    private User setUser() {
        User userEntity = new User();
        userEntity.setId(1L);
        userEntity.setEmail("admin@test.com");
        userEntity.setEnabled(true);
        return userEntity;
    }

    private CardDeleteRequest setCardDeleteRequest() {
        CardDeleteRequest request = new CardDeleteRequest();
        request.setId(1L);
        return request;
    }

    private CardRequest setCardRequest() {
        CardRequest card = new CardRequest();
        card.setName("test");
        card.setStatus("to do");
        return card;
    }
}