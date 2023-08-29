package com.logicea.cardsapi.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class CardDeletionResponse {
    @Schema(description = "The deletion status code", defaultValue = "1600")
    @JsonProperty(value = "code")
    private String code;

    @Schema(defaultValue = "Successfully deleted card, id: 1", description = "The card deletion status")
    @JsonProperty(value = "message")
    private String status;
}
