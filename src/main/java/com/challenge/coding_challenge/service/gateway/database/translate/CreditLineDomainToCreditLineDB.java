package com.challenge.coding_challenge.service.gateway.database.translate;

import com.challenge.coding_challenge.domain.model.CreditLineDomain;
import com.challenge.coding_challenge.service.gateway.database.model.CreditLineDB;
import com.challenge.coding_challenge.service.gateway.database.model.FoundingTypeDB;

public class CreditLineDomainToCreditLineDB {

    public static CreditLineDB translate(CreditLineDomain creditLine) {
        return new CreditLineDB(creditLine.getId(), creditLine.getClientId(),
                FoundingTypeDB.valueOf(creditLine.getFoundingType().name()), creditLine.getCashBalance(),
                creditLine.getMonthlyRevenue(), creditLine.getRequestedCreditLine(), creditLine.getRequestedDate(),
                creditLine.getCreditLineRecommended(), creditLine.getAccepted(), creditLine.getCreditLineAuthorized(),
                creditLine.getMessage());
    }

}