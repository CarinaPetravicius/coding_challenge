package com.challenge.coding_challenge.service.gateway;

import com.challenge.coding_challenge.domain.gateway.GetAllPreviousCreditLineApprovedInAPeriodForCustomerGateway;
import com.challenge.coding_challenge.domain.model.CreditLineDomain;
import com.challenge.coding_challenge.service.gateway.database.model.CreditLineDB;
import com.challenge.coding_challenge.service.gateway.database.repository.CreditLineRepository;
import com.challenge.coding_challenge.service.gateway.database.translate.CreditLineDBToCreditLineDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class GetAllPreviousCreditLineApprovedInAPeriodForCustomerGatewayImpl implements GetAllPreviousCreditLineApprovedInAPeriodForCustomerGateway {

    private CreditLineRepository creditLineRepository;

    @Override
    public List<CreditLineDomain> execute(String clientId, LocalDateTime period) {
        final List<CreditLineDB> creditLines = creditLineRepository.findAllByClientIdAndAcceptedTrueAndRequestedDateGreaterThan(clientId, period);
        return CreditLineDBToCreditLineDomain.translate(creditLines);
    }

}