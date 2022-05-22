package com.challenge.coding_challenge.client.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record CreditLineRequest(
        @NotBlank(message = "The clientId must be informed")
        @Size(max = 100, message = "The clientId has exceeded 100 characters")
        String clientId,

        @NotNull(message = "The foundingType must be informed")
        FoundingTypeRequest foundingType,

        @NotNull(message = "The cashBalance must be informed")
        Double cashBalance,

        @NotNull(message = "The monthlyRevenue must be informed")
        Double monthlyRevenue,

        @NotNull(message = "The requestedCreditLine must be informed")
        Double requestedCreditLine
) {
}