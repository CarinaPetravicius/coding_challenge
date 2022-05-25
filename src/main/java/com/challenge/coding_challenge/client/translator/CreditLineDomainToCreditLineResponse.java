package com.challenge.coding_challenge.client.translator;

import com.challenge.coding_challenge.client.model.CreditLineResponse;
import com.challenge.coding_challenge.domain.model.CreditLineDomain;

public class CreditLineDomainToCreditLineResponse {

    public static CreditLineResponse translate(CreditLineDomain creditLine) {
        return new CreditLineResponse(creditLine.getAccepted(), creditLine.getCreditLineAuthorized(),
                creditLine.getMessage(), null);
    }

}