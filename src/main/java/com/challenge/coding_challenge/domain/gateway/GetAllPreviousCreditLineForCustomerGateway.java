package com.challenge.coding_challenge.domain.gateway;

import com.challenge.coding_challenge.domain.model.CreditLineDomain;

import java.util.List;

public interface GetAllPreviousCreditLineForCustomerGateway {

    List<CreditLineDomain> execute(String clientId);

}