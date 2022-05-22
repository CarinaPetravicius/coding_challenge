package com.challenge.coding_challenge.service.gateway;

import com.challenge.coding_challenge.domain.gateway.SaveCreditLineForCustomerGateway;
import com.challenge.coding_challenge.domain.model.CreditLineDomain;
import com.challenge.coding_challenge.service.gateway.database.repository.CreditLineRepository;
import com.challenge.coding_challenge.service.gateway.database.translate.CreditLineDomainToCreditLineDB;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SaveCreditLineForCustomerGatewayImpl implements SaveCreditLineForCustomerGateway {

    private CreditLineRepository creditLineRepository;

    @Override
    public void execute(CreditLineDomain creditLine) {
        creditLineRepository.save(CreditLineDomainToCreditLineDB.translate(creditLine));
    }

}