package com.logicea.cardsapi.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.logicea.cardsapi.core.enums.CardStatus;
import com.logicea.cardsapi.rest.validations.ColorValidator;
import com.logicea.cardsapi.rest.validations.EnumValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardRequest implements Serializable {
    @Schema(defaultValue = "Card Test", description = "Card name")
    @NotBlank(message = "The name cannot be blank")
    @JsonProperty(value = "name")
    private String name;

    @Schema(defaultValue = "This is a test description", description = " The description of the card.")
    @JsonProperty(value = "description")
    private String description;

    @Schema(defaultValue = "#FF8403", description = " The alphanumeric color code")
    @ColorValidator(message = "The color must be of color code format: eg: #FF3637")
    @JsonProperty(value = "color")
    private String color;

    @Schema(defaultValue = "To Do", description = " The status of the card")
    @EnumValidator(message = "Valid values allowed are: To Do, In Progress and Done", targetClassType = CardStatus.class)
    @JsonProperty(value = "status")
    private String status;
}
