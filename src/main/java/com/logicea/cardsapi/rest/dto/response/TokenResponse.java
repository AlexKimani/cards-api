package com.logicea.cardsapi.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
@Builder
@ApiResponse(description = "The login Password token", responseCode= "200")
public class TokenResponse implements Serializable {
    @Schema(description = "The user access token")
    @JsonProperty(value = "token")
    private String token;
}
