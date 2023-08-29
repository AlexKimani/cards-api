package com.logicea.cardsapi.rest.controller;

import com.logicea.cardsapi.rest.dto.request.CardDeleteRequest;
import com.logicea.cardsapi.rest.dto.request.CardRequest;
import com.logicea.cardsapi.rest.dto.response.CardDeletionResponse;
import com.logicea.cardsapi.rest.dto.response.CardResponse;
import com.logicea.cardsapi.rest.facade.CardFacade;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/card")
@Tag(name = "Cards", description = "API to handle CRUD services for Cards service.")
public class CardController {
    private final CardFacade cardFacade;

    @PostMapping(path = "/create")
    @Operation(summary = "Create a new Card",
            description = "Create a new card in database.",
            tags = {"Create Card", "POST"})
    public ResponseEntity<CardResponse> createCard(
            @Valid
            @Schema(name = "Card Request", implementation = CardRequest.class)
            @RequestBody CardRequest cardRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.cardFacade.createCard(cardRequest));
    }

    @GetMapping(path = "/admin")
    @Operation(summary = "Admin only Get a paged list of all cards with page, size, and offset",
            description = "Get a paged list of all cards with page, size, and offset for users with ROLE ADMIN",
            tags = {"Get All Cards", "GET"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<CardResponse>> getAllCards(@ApiParam(name = "page", value = "The page number.", defaultValue = "0")
                                                       @RequestParam(defaultValue = "0") int page,
                                              @ApiParam(name = "size", value = "The total number of elements per page.", defaultValue = "5")
                                                       @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(this.cardFacade.getAllCards(pageable));
    }

    @GetMapping(path = "/all")
    @Operation(summary = "Get a paged list of all cards with page, size, and offset",
            description = "Get a paged list of all cards with page, size, and offset for user",
            tags = {"Get Cards by User", "GET"})
    public ResponseEntity<Page<CardResponse>> getAllCardsForUser(@ApiParam(name = "page", value = "The page number.", defaultValue = "0")
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @ApiParam(name = "size", value = "The total number of elements per page.", defaultValue = "5")
                                                          @RequestParam(defaultValue = "5") int size,
                                                                 @ApiParam(name = "sort_field", value = "Available sort fields: id | name | color | status | date_created", defaultValue = "id")
                                                                 @RequestParam(defaultValue = "id") String sortField) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(this.cardFacade.getAllCardsCreatedByUser(sortField, pageable));
    }

    @GetMapping(path = "/id/{id}")
    @Operation(summary = "Get a card using the id field",
            description  = "Get a card using the id field",
            tags = {"Get Card by Id", "GET"})
    public ResponseEntity<CardResponse> getCardById(@ApiParam(name = "id", value = "The unique card identifier.")
                                                              @PathVariable long id) {
        log.info("Getting Card Object for id: {}", id);
        return ResponseEntity.ok(this.cardFacade.getCardById(id));
    }

    @PutMapping(path = "/update/{id}")
    @Operation(summary = "Update a card using the id field as search field",
            description  = "Update a card using the id field as search field",
            tags = {"Update Card by Id", "PUT"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MEMBER')")
    public ResponseEntity<CardResponse> updateCardById(@ApiParam(name = "id", value = "The unique card identifier.")
                                                           @PathVariable long id,
                                                       @Valid
                                                       @Schema(name = "Card Request", implementation = CardRequest.class)
                                                       @RequestBody CardRequest cardRequest) {
        return ResponseEntity.ok(this.cardFacade.updateCard(id, cardRequest));
    }

    @DeleteMapping(path = "/delete")
    @Operation(summary = "Delete a card using the id field as search field in request",
            description  = "Update a card using the id field as search field in request",
            tags = {"Delete Card by Id", "DELETE"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MEMBER')")
    public ResponseEntity<CardDeletionResponse> deleteCardById(
            @Valid
            @Schema(name = "Card Deletion Request", implementation = CardDeleteRequest.class)
            @RequestBody CardDeleteRequest cardRequest) {
        return ResponseEntity.ok(this.cardFacade.deleteCard(cardRequest));
    }

    @DeleteMapping(path = "/admin/delete/all")
    @Operation(summary = "Delete all cards using Admin role.",
            description  = "Delete all cards using Admin role.",
            tags = {"Delete All Cards", "DELETE"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CardDeletionResponse> deleteAllCards() {
        return ResponseEntity.ok(this.cardFacade.deleteAllCards());
    }
}
