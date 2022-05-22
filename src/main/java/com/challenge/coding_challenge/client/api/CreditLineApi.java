package com.challenge.coding_challenge.client.api;

import com.challenge.coding_challenge.client.model.CreditLineRequest;
import com.challenge.coding_challenge.client.model.CreditLineResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

@RequestMapping("/v1")
public interface CreditLineApi {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/credit_line")
    CreditLineResponse createCreditLine(@RequestBody @Valid CreditLineRequest creditLineRequest);

}