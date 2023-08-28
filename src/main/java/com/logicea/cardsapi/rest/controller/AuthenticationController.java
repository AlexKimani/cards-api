package com.logicea.cardsapi.rest.controller;

import com.logicea.cardsapi.rest.dto.request.LoginRequest;
import com.logicea.cardsapi.rest.dto.response.TokenResponse;
import com.logicea.cardsapi.rest.facade.AuthenticationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/v1/")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API to Authenticate User into Cards service.")
public class AuthenticationController {
    private final AuthenticationFacade authenticationFacade;

    @Operation(summary = "Authenticate User",
            description = "Verify user authentication details.",
            tags = {"Authenticate", "POST"})
    @PostMapping(path = "/authenticate")
    public ResponseEntity<TokenResponse> authenticateUser(
            @Valid
            @Schema(name = "Authentication Request", implementation = LoginRequest.class)
            @RequestBody LoginRequest request) {
        log.info("About to login user: {}", request.getEmailAddress());
        return ResponseEntity.ok(this.authenticationFacade.createAuthentication(request));
    }
}
