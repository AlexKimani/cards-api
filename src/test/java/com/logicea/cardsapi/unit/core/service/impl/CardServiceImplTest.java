package com.logicea.cardsapi.unit.core.service.impl;

import com.logicea.cardsapi.core.entity.Card;
import com.logicea.cardsapi.core.entity.User;
import com.logicea.cardsapi.core.repository.CardRepository;
import com.logicea.cardsapi.core.repository.UserRepository;
import com.logicea.cardsapi.core.service.impl.CardServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    private Pageable pageable;
    private Card card;
    private User user;

    @BeforeEach
    void setUp() {
        this.pageable = PageRequest.of(1, 5);
        this.card = this.setCard();
        this.user = this.setUser();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName(value = "Given a pageable object should return a paged list of cards")
    void testGivenAPageableObjectShouldReturnAPagedListOfCards() {
        doReturn(this.setPagedCards(this.pageable)).when(this.cardRepository).findAll(this.pageable);
        Page<Card> cards = assertDoesNotThrow(() -> this.cardService.getAllCards(pageable));
        verify(this.cardRepository, times(1)).findAll(any(Pageable.class));
        assertNotNull(cards);
        assertEquals(this.pageable.getPageSize(), cards.getSize());
    }

    @Test
    @DisplayName(value = "Given a Pageable object, should return a sorted paged list of cards")
    void testGivenAPageableObjectShouldReturnASortedPagedListOfCards() {
        doReturn(Optional.of(this.user)).when(this.userRepository).findUserByEmail(this.user.getEmail());
        doReturn(this.setPagedCards(this.pageable)).when(this.cardRepository).findCardsByCreatedBy(this.user, this.pageable);
        Page<Card> cards = assertDoesNotThrow(() -> this.cardService.getAllCardsCreatedByUser(this.user.getEmail(), pageable));
        verify(this.cardRepository, times(1)).findCardsByCreatedBy(any(User.class), any(Pageable.class));
        verify(this.userRepository, times(1)).findUserByEmail(anyString());
        assertNotNull(cards);
        assertEquals(this.pageable.getPageSize(), cards.getSize());
    }

    @Test
    @DisplayName(value = "Given a Card Object, should create a new Card")
    void testGivenACardObjectShouldCreateANewCard() {
        doReturn(this.card).when(this.cardRepository).save(this.card);
        Card createdCard = assertDoesNotThrow(() -> this.cardService.createCard(this.card));
        verify(this.cardRepository, times(1)).save(any(Card.class));
        assertNotNull(createdCard);
    }

    @Test
    @DisplayName(value = "Given a Valid card id, should return a card object")
    void testGivenAValidCardIdShouldReturnACardObject() {
        doReturn((Optional.of(this.card))).when(this.cardRepository).findCardById(1L);
        Optional<Card> foundCard = assertDoesNotThrow(() -> this.cardService.getCardById(1L));
        verify(this.cardRepository, times(1)).findCardById(anyLong());
        assertTrue(foundCard.isPresent());
    }

    @Test
    @DisplayName(value = "Given an invalid card id should return an empty response")
    void testGivenAnInvalidCardIdShouldReturnAnEmptyResponse() {
        Optional<Card> foundCard = assertDoesNotThrow(() -> this.cardService.getCardById(2L));
        verify(this.cardRepository, times(1)).findCardById(anyLong());
        assertTrue(foundCard.isEmpty());
    }

    @Test
    @DisplayName(value = "Given a Valid Card id and user id should return a card object")
    void testGivenAValidCardIdAndUserIdShouldReturnACardObject() {
        doReturn(Optional.of(this.card)).when(this.cardRepository).findCardByIdAndCreatedBy(1L, this.user);
        doReturn(Optional.of(this.user)).when(this.userRepository).findById(1L);
        Optional<Card> foundCard = assertDoesNotThrow(() -> this.cardService.getCardByIdAndUserId(1L, 1L));
        verify(this.cardRepository, times(1)).findCardByIdAndCreatedBy(anyLong(), any(User.class));
        verify(this.userRepository, times(1)).findById(anyLong());
        assertTrue(foundCard.isPresent());
    }

    @Test
    @DisplayName(value = "Given a card object should update card entity")
    void testGivenACardObjectShouldUpdateCardEntity() {
        doReturn(this.card).when(this.cardRepository).save(this.card);
        Card updatedCard = assertDoesNotThrow(() -> this.cardService.createCard(this.card));
        verify(this.cardRepository, times(1)).save(any(Card.class));
        assertNotNull(updatedCard);
    }

    @Test
    @DisplayName(value = "Given a card object, should delete a card item")
    void testGivenACardObjectShouldDeleteACardItem() {
        doNothing().when(this.cardRepository).delete(this.card);

        assertDoesNotThrow(() -> this.cardService.deleteCard(this.card));
    }

    @Test
    void deleteAllCards() {
        doNothing().when(this.cardRepository).deleteAll();
        assertDoesNotThrow(() -> this.cardService.deleteAllCards());
    }

    private Page<Card> setPagedCards(Pageable pageable) {
        List<Card> cards = List.of(this.setCard(),this.setCard());
        return new PageImpl<>(cards, pageable, cards.size());
    }

    private Card setCard() {
        Card card = new Card();
        card.setId(1L);
        card.setName("test");
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
}
