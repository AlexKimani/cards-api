package com.logicea.cardsapi.rest.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class PagedResponse {
    private Object content;
    private int size;
    private int page;
    private boolean sorted;
    private long totalNumberOfElements;
    private int totalPages;
}
