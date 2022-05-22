package com.challenge.coding_challenge.domain.gateway;

import com.challenge.coding_challenge.domain.model.CreditLineDomain;

public interface SaveCreditLineForCustomerGateway {

    void execute(CreditLineDomain creditLine);

}