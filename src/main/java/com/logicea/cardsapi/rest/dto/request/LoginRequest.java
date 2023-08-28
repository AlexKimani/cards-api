package com.logicea.cardsapi.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest implements Serializable {
    @Schema(defaultValue = "admin@test.com", description = " Email Address")
    @NotNull(message = "The email address cannot be null")
    @JsonProperty(value = "email_address")
    @Email(message = "Invalid email address provided.", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String emailAddress;

    @Schema(defaultValue = "password", description = "The user password")
    @NotNull(message = "The password value cannot be null")
    @JsonProperty(value = "password")
    private String password;
}
