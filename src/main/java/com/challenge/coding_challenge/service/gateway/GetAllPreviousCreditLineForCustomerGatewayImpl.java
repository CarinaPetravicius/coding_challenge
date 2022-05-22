package com.challenge.coding_challenge.service.gateway;

import com.challenge.coding_challenge.domain.gateway.GetAllPreviousCreditLineForCustomerGateway;
import com.challenge.coding_challenge.domain.model.CreditLineDomain;
import com.challenge.coding_challenge.service.gateway.database.model.CreditLineDB;
import com.challenge.coding_challenge.service.gateway.database.repository.CreditLineRepository;
import com.challenge.coding_challenge.service.gateway.database.translate.CreditLineDBToCreditLineDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GetAllPreviousCreditLineForCustomerGatewayImpl implements GetAllPreviousCreditLineForCustomerGateway {

    private CreditLineRepository creditLineRepository;

    @Override
    public List<CreditLineDomain> execute(String clientId) {
        final List<CreditLineDB> creditLines = creditLineRepository.findAllByClientIdOrderByRequestedDateDesc(clientId);
        return CreditLineDBToCreditLineDomain.translate(creditLines);
    }

}