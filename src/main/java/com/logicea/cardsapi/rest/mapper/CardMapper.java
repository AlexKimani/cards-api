package com.logicea.cardsapi.rest.mapper;

import com.logicea.cardsapi.core.entity.Card;
import com.logicea.cardsapi.core.enums.CardStatus;
import com.logicea.cardsapi.core.enums.ErrorCode;
import com.logicea.cardsapi.rest.dto.request.CardRequest;
import com.logicea.cardsapi.rest.dto.response.CardDeletionResponse;
import com.logicea.cardsapi.rest.dto.response.CardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Card mapper.
 */
@Slf4j
public class CardMapper {
    private CardMapper() {}
    /**
     * Create card entity card.
     *
     * @param request the request
     * @return the card
     */
    public static Card createCardEntity(CardRequest request) {
        Card card = new Card();
        card.setName(request.getName());
        card.setDescription(request.getDescription() != null ? request.getDescription() : "");
        card.setColor(request.getColor() != null ? request.getColor() : "");
        card.setStatus(request.getStatus() != null ? CardStatus.fromString(request.getStatus()) : CardStatus.TO_DO);
        return card;
    }

    /**
     * Gets card response.
     *
     * @param card the card
     * @return the card response
     */
    public static CardResponse getCardResponse(Card card) {
        return CardResponse.builder()
                .id(card.getId())
                .name(card.getName())
                .description(card.getDescription())
                .color(card.getColor())
                .status(card.getStatus().getValue())
                .dateCreated(card.getCreatedAt())
                .build();
    }

    /**
     * Gets paged card response.
     *
     * @param cardPage the card page
     * @return the paged card response
     */
    public static Page<CardResponse> getPagedCardResponse(Page<Card> cardPage) {
        List<CardResponse> cardResponses = cardPage.getContent()
                .stream()
                .map(CardMapper::getCardResponse)
                .collect(Collectors.toList());
        Pageable responsePage = PageRequest.of(cardPage.getNumber(), cardPage.getSize(), cardPage.getSort());
        return new PageImpl<>(cardResponses, responsePage, cardResponses.size());
    }

    /**
     * Update card details.
     *
     * @param request      the request
     * @param existingCard the existing card
     */
    public static void updateCardDetails(CardRequest request, Card existingCard) {
        if (request.getName() != null && !request.getName().isBlank()) {
            existingCard.setName(request.getName());
        }
        existingCard.setDescription(request.getDescription() != null ? request.getDescription() : "");
        existingCard.setColor(request.getColor() != null ? request.getColor() : "");
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            existingCard.setStatus(CardStatus.fromString(request.getStatus()));
        }
    }

    /**
     * Sets card deletion response.
     *
     * @param id the id
     * @return the card deletion response
     */
    public static CardDeletionResponse setCardDeletionResponse(long id) {
        return CardDeletionResponse.builder()
                .code(ErrorCode.ERROR_1605.getCode())
                .status(String.format(ErrorCode.ERROR_1605.getMessage(), id))
                .build();
    }

    /**
     * Sets delete all cards response.
     *
     * @return the delete all cards response
     */
    public static CardDeletionResponse setDeleteAllCardsResponse() {
        return CardDeletionResponse.builder()
                .code(ErrorCode.ERROR_1606.getCode())
                .status(ErrorCode.ERROR_1606.getMessage())
                .build();
    }
}
