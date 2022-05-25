package com.challenge.coding_challenge.domain.usecase;

import com.challenge.coding_challenge.domain.exception.TooManyRequestsException;
import com.challenge.coding_challenge.domain.gateway.GetAllPreviousCreditLineApprovedInAPeriodForCustomerGateway;
import com.challenge.coding_challenge.domain.gateway.GetAllPreviousCreditLineForCustomerGateway;
import com.challenge.coding_challenge.domain.gateway.SaveCreditLineForCustomerGateway;
import com.challenge.coding_challenge.domain.model.CreditLineDomain;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@AllArgsConstructor
public class CreateCreditLineForACustomerUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateCreditLineForACustomerUseCase.class);
    private GetAllPreviousCreditLineForCustomerGateway getAllPreviousCreditLineForCustomer;

    private GetAllPreviousCreditLineApprovedInAPeriodForCustomerGateway getAllPreviousCreditLineApprovedInAPeriodForCustomer;
    private SaveCreditLineForCustomerGateway saveCreditLineForCustomer;

    public CreditLineDomain execute(CreditLineDomain creditLineRequested) {
        creditLineRequested.calculateTheCreditLine();
        creditLineRequested.releaseTheCreditLine();

        final List<CreditLineDomain> creditLines = getAllPreviousCreditLineForCustomer.execute(creditLineRequested.getClientId());

        if (creditLines.isEmpty()) {
            log.info("Creating the first credit line for the client");
            saveCreditLineForCustomer.execute(creditLineRequested);
            return creditLineRequested;
        } else {
            final CreditLineDomain recentCreditSaved = creditLines.get(0);

            if (recentCreditSaved.getAccepted()) {
                log.info("The client already had a credit line approved");
                return createAndReturnAcceptedCredit(creditLineRequested, recentCreditSaved);
            } else {
                log.info("The client already had a credit line not approved");
                return createOrReturnRecentNotAcceptedCredit(creditLineRequested, recentCreditSaved, creditLines);
            }
        }
    }

    private CreditLineDomain createAndReturnAcceptedCredit(CreditLineDomain creditRequested, CreditLineDomain recentCreditSaved) {
        creditRequested.updateWithPreviousCreditLineAuthorized(recentCreditSaved);
        saveCreditLineForCustomer.execute(creditRequested);

        verifyTooManyRequestForApprovedCreditLine(creditRequested);

        return creditRequested;
    }

    private void verifyTooManyRequestForApprovedCreditLine(CreditLineDomain creditRequested) {
        final LocalDateTime periodForManyRequest = creditRequested.getRequestedDate().minusMinutes(
                CreditLineDomain.MINUTES_PERIOD_FOR_TOO_MANY_REQUEST_FOR_APPROVED_CREDIT);

        final List<CreditLineDomain> creditLines = getAllPreviousCreditLineApprovedInAPeriodForCustomer
                .execute(creditRequested.getClientId(), periodForManyRequest);

        if (creditLines.size() >= CreditLineDomain.REQUEST_LIMIT_FOR_TOO_MANY_REQUEST) {
            throw new TooManyRequestsException();
        }
    }

    private CreditLineDomain createOrReturnRecentNotAcceptedCredit(CreditLineDomain creditRequested, CreditLineDomain recentCreditSaved,
                                                                   List<CreditLineDomain> creditLines) {
        verifyTooManyRequestForNotApprovedCreditLine(creditRequested, recentCreditSaved);

        if (recentCreditSaved.getMessage().equals(CreditLineDomain.SALES_AGENT_MESSAGE)) {
            return recentCreditSaved;
        } else {
            changeToSalesMessageIfGetLimitOfNotAccepted(creditLines, creditRequested);

            saveCreditLineForCustomer.execute(creditRequested);
            return creditRequested;
        }
    }

    private void verifyTooManyRequestForNotApprovedCreditLine(CreditLineDomain creditRequested, CreditLineDomain recentCreditSaved) {
        final long secondsDifference = recentCreditSaved.getRequestedDate().until(creditRequested.getRequestedDate(), ChronoUnit.SECONDS);

        if (secondsDifference <= CreditLineDomain.SECONDS_PERIOD_FOR_TOO_MANY_REQUEST_FOR_NOT_APPROVED_CREDIT) {
            throw new TooManyRequestsException();
        }
    }

    private void changeToSalesMessageIfGetLimitOfNotAccepted(List<CreditLineDomain> creditLines, CreditLineDomain creditRequested) {
        if (!creditRequested.getAccepted() && creditLines.size() >= CreditLineDomain.LIMIT_OF_NOT_APPROVED - 1) {
            log.info("The sales agent will contact the client");
            creditRequested.setSalesAgentMessage();
        }
    }

}