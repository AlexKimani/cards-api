package com.logicea.cardsapi.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDeleteRequest {
    @Schema(defaultValue = "1", description = "Card id in database")
    @NotNull(message = "The id cannot be null")
    @JsonProperty(value = "id")
    private long id;
}
