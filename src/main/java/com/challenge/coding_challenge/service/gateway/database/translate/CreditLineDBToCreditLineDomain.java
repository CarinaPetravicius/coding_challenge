package com.challenge.coding_challenge.service.gateway.database.translate;

import com.challenge.coding_challenge.domain.model.CreditLineDomain;
import com.challenge.coding_challenge.domain.model.FoundingTypeDomain;
import com.challenge.coding_challenge.service.gateway.database.model.CreditLineDB;

import java.util.ArrayList;
import java.util.List;

public class CreditLineDBToCreditLineDomain {

    public static List<CreditLineDomain> translate(List<CreditLineDB> creditLines) {
        return creditLines.isEmpty() ? new ArrayList<>() :
                creditLines.stream().map(CreditLineDBToCreditLineDomain::translate).toList();
    }

    public static CreditLineDomain translate(CreditLineDB creditLine) {
        return new CreditLineDomain(creditLine.getId(), creditLine.getClientId(),
                FoundingTypeDomain.valueOf(creditLine.getFoundingType().name()), creditLine.getCashBalance(),
                creditLine.getMonthlyRevenue(), creditLine.getRequestedCreditLine(), creditLine.getRequestedDate(),
                creditLine.getCreditLineRecommended(), creditLine.getAccepted(), creditLine.getCreditLineAuthorized(),
                creditLine.getMessage());
    }

}