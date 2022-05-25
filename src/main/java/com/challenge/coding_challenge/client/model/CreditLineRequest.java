package com.challenge.coding_challenge.client.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record CreditLineRequest(
        @Schema(required = true)
        @NotBlank(message = "The clientId must be informed")
        @Size(max = 100, message = "The clientId has exceeded 100 characters")
        String clientId,

        @Schema(required = true)
        @NotNull(message = "The foundingType must be informed")
        FoundingTypeRequest foundingType,

        @Schema(required = true)
        @NotNull(message = "The cashBalance must be informed")
        Double cashBalance,

        @Schema(required = true)
        @NotNull(message = "The monthlyRevenue must be informed")
        Double monthlyRevenue,

        @Schema(required = true)
        @NotNull(message = "The requestedCreditLine must be informed")
        Double requestedCreditLine
) {
}