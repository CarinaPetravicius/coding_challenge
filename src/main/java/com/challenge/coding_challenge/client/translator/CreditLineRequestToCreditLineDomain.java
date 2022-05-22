package com.challenge.coding_challenge.client.translator;

import com.challenge.coding_challenge.client.model.CreditLineRequest;
import com.challenge.coding_challenge.domain.model.CreditLineDomain;
import com.challenge.coding_challenge.domain.model.FoundingTypeDomain;

import java.time.LocalDateTime;

public class CreditLineRequestToCreditLineDomain {

    public static CreditLineDomain translate(CreditLineRequest request) {
        return new CreditLineDomain(null, request.clientId(), FoundingTypeDomain.valueOf(request.foundingType().name()),
                request.cashBalance(), request.monthlyRevenue(), request.requestedCreditLine(),
                LocalDateTime.now(), null, null, null, null);
    }

}