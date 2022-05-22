package com.challenge.coding_challenge.domain.usecase;

import com.challenge.coding_challenge.domain.gateway.GetAllPreviousCreditLineForCustomerGateway;
import com.challenge.coding_challenge.domain.gateway.SaveCreditLineForCustomerGateway;
import com.challenge.coding_challenge.domain.model.CreditLineDomain;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CreateCreditLineForACustomerUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateCreditLineForACustomerUseCase.class);
    private GetAllPreviousCreditLineForCustomerGateway getAllPreviousCreditLineForCustomer;
    private SaveCreditLineForCustomerGateway saveCreditLineForCustomer;

    public void execute(CreditLineDomain creditLine) {
        creditLine.calculateTheCreditLine();
        creditLine.releaseTheCreditLine();

        final List<CreditLineDomain> creditLines = getAllPreviousCreditLineForCustomer.execute(creditLine.getClientId());
        // TODO develop some validations before save.

        saveCreditLineForCustomer.execute(creditLine);
    }

}