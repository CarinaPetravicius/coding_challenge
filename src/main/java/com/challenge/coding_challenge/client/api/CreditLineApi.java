package com.challenge.coding_challenge.client.api;

import com.challenge.coding_challenge.client.model.CreditLineRequest;
import com.challenge.coding_challenge.client.model.CreditLineResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

@RequestMapping("/v1")
@Tag(name = "Credit Line")
public interface CreditLineApi {

    @Operation(summary = "Create credit line", description = "create a credit line for a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = CreditLineResponse.class))),
            @ApiResponse(responseCode = "429", description = "Too Many Requests", content = @Content(schema = @Schema(implementation = CreditLineResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CreditLineResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = CreditLineResponse.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/credit_line")
    CreditLineResponse createCreditLine(@RequestBody @Valid CreditLineRequest creditLineRequest);

}