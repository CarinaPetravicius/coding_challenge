package com.challenge.coding_challenge.domain.gateway;

import com.challenge.coding_challenge.domain.model.CreditLineDomain;

import java.time.LocalDateTime;
import java.util.List;

public interface GetAllPreviousCreditLineApprovedInAPeriodForCustomerGateway {

    List<CreditLineDomain> execute(String clientId, LocalDateTime period);

}