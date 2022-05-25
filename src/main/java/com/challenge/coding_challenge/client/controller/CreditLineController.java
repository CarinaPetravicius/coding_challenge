package com.challenge.coding_challenge.client.controller;

import com.challenge.coding_challenge.client.api.CreditLineApi;
import com.challenge.coding_challenge.client.model.CreditLineRequest;
import com.challenge.coding_challenge.client.model.CreditLineResponse;
import com.challenge.coding_challenge.client.translator.CreditLineDomainToCreditLineResponse;
import com.challenge.coding_challenge.client.translator.CreditLineRequestToCreditLineDomain;
import com.challenge.coding_challenge.domain.model.CreditLineDomain;
import com.challenge.coding_challenge.domain.usecase.CreateCreditLineForACustomerUseCase;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@AllArgsConstructor
public class CreditLineController implements CreditLineApi {

    private static final Logger log = LoggerFactory.getLogger(CreditLineController.class);
    private final CreateCreditLineForACustomerUseCase createCreditLineForACustomer;

    @Override
    public CreditLineResponse createCreditLine(CreditLineRequest creditLineRequest) {
        log.info("The client {} started to request a credit line", creditLineRequest.clientId());
        final CreditLineDomain creditLineDomain = createCreditLineForACustomer.execute(CreditLineRequestToCreditLineDomain.translate(creditLineRequest));
        log.info("The client {} received a response for the credit line", creditLineRequest.clientId());
        return CreditLineDomainToCreditLineResponse.translate(creditLineDomain);
    }

}