package com.challenge.coding_challenge.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreditLineResponse(
        Boolean accepted,
        Double creditLineAuthorized,
        String message,
        List<String> errors
) {
}