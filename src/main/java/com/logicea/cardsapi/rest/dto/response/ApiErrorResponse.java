package com.logicea.cardsapi.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.logicea.cardsapi.rest.dto.response.error.ApiSubError;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@Builder
public class ApiErrorResponse {
    private Long timestamp;
    private String status;
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value="sub_errors")
    private List<ApiSubError> subErrors;
}
