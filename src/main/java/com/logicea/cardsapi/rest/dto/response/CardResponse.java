package com.logicea.cardsapi.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Getter
@Setter
@Builder
@ApiResponse(description = "The card response object", responseCode= "200")
public class CardResponse implements Serializable {
    @Schema(description = "The card id in database.", defaultValue = "1")
    @JsonProperty(value = "id")
    private long id;

    @Schema(description = "The name of the card", defaultValue = "Test Card 1")
    @JsonProperty(value = "name")
    private String name;

    @Schema(description = "The description of the card", defaultValue = "test description")
    @JsonProperty(value = "description")
    private String description;

    @Schema(description = "The card color", defaultValue = "#FF6753")
    @JsonProperty(value = "color")
    private String color;

    @Schema(description = "The status of the card", defaultValue = "To Do")
    @JsonProperty(value = "status")
    private String status;

    @Schema(description = "The date car was created", defaultValue = "2023-08-27")
    @JsonProperty(value = "date_created")
    private Timestamp dateCreated;
}
