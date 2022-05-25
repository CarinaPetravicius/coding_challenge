package com.challenge.coding_challenge.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CreditLineDomainTest {

    @Test
    void releaseACreditLineForASmeClient() {
        final CreditLineDomain creditLine = new CreditLineDomain(null, "1234567", FoundingTypeDomain.SME,
                4550.50, 80500.00, 10000.00,
                LocalDateTime.now(), null, null, null, null);

        creditLine.calculateTheCreditLine();
        creditLine.releaseTheCreditLine();

        assertTrue(creditLine.getAccepted());
        assertEquals(creditLine.getRequestedCreditLine(), creditLine.getCreditLineAuthorized());
        assertEquals(16100.00, creditLine.getCreditLineRecommended());
        assertEquals(CreditLineDomain.CREDIT_AUTHORIZED, creditLine.getMessage());
    }

    @Test
    void doNotReleaseACreditLineForASmeClient() {
        final CreditLineDomain creditLine = new CreditLineDomain(null, "1234567", FoundingTypeDomain.SME,
                4550.50, 80500.00, 16100.50,
                LocalDateTime.now(), null, null, null, null);

        creditLine.calculateTheCreditLine();
        creditLine.releaseTheCreditLine();

        assertFalse(creditLine.getAccepted());
        assertEquals(0.0, creditLine.getCreditLineAuthorized());
        assertEquals(16100.00, creditLine.getCreditLineRecommended());
        assertEquals(CreditLineDomain.CREDIT_NOT_AUTHORIZED, creditLine.getMessage());
    }

    @Test
    void releaseACreditLineForASmeClientWithoutBalanceCash() {
        final CreditLineDomain creditLine = new CreditLineDomain(null, "1234568", FoundingTypeDomain.SME,
                0.0, 35355.89, 5500.59,
                LocalDateTime.now(), null, null, null, null);

        creditLine.calculateTheCreditLine();
        creditLine.releaseTheCreditLine();

        assertTrue(creditLine.getAccepted());
        assertEquals(creditLine.getRequestedCreditLine(), creditLine.getCreditLineAuthorized());
        assertEquals(7071.178, creditLine.getCreditLineRecommended());
        assertEquals(CreditLineDomain.CREDIT_AUTHORIZED, creditLine.getMessage());
    }

    @Test
    void releaseACreditLineForAStartupClientWithMonthlyRevenueBiggerThanCashBalance() {
        final CreditLineDomain creditLine = new CreditLineDomain(null, "1234569", FoundingTypeDomain.Startup,
                4550.50, 80500.00, 12500.00,
                LocalDateTime.now(), null, null, null, null);

        creditLine.calculateTheCreditLine();
        creditLine.releaseTheCreditLine();

        assertTrue(creditLine.getAccepted());
        assertEquals(creditLine.getRequestedCreditLine(), creditLine.getCreditLineAuthorized());
        assertEquals(16100.00, creditLine.getCreditLineRecommended());
        assertEquals(CreditLineDomain.CREDIT_AUTHORIZED, creditLine.getMessage());
    }

    @Test
    void releaseACreditLineForAStartupClientWithCashBalanceBiggerThanMonthlyRevenue() {
        final CreditLineDomain creditLine = new CreditLineDomain(null, "1234569", FoundingTypeDomain.Startup,
                21550.55, 19500.00, 4100.00,
                LocalDateTime.now(), null, null, null, null);

        creditLine.calculateTheCreditLine();
        creditLine.releaseTheCreditLine();

        assertTrue(creditLine.getAccepted());
        assertEquals(creditLine.getRequestedCreditLine(), creditLine.getCreditLineAuthorized());
        assertEquals(7183.516666666666, creditLine.getCreditLineRecommended());
        assertEquals(CreditLineDomain.CREDIT_AUTHORIZED, creditLine.getMessage());
    }

    @Test
    void doNotReleaseACreditLineForAStartupClientWithMonthlyRevenueBiggerThanCashBalance() {
        final CreditLineDomain creditLine = new CreditLineDomain(null, "1234569", FoundingTypeDomain.Startup,
                4550.50, 80500.00, 16100.00,
                LocalDateTime.now(), null, null, null, null);

        creditLine.calculateTheCreditLine();
        creditLine.releaseTheCreditLine();

        assertFalse(creditLine.getAccepted());
        assertEquals(0.0, creditLine.getCreditLineAuthorized());
        assertEquals(16100.00, creditLine.getCreditLineRecommended());
        assertEquals(CreditLineDomain.CREDIT_NOT_AUTHORIZED, creditLine.getMessage());
    }

    @Test
    void doNotReleaseACreditLineForAStartupClientWithCashBalanceBiggerThanMonthlyRevenue() {
        final CreditLineDomain creditLine = new CreditLineDomain(null, "1234569", FoundingTypeDomain.Startup,
                21550.55, 19500.00, 7183.517,
                LocalDateTime.now(), null, null, null, null);

        creditLine.calculateTheCreditLine();
        creditLine.releaseTheCreditLine();

        assertFalse(creditLine.getAccepted());
        assertEquals(0.0, creditLine.getCreditLineAuthorized());
        assertEquals(7183.516666666666, creditLine.getCreditLineRecommended());
        assertEquals(CreditLineDomain.CREDIT_NOT_AUTHORIZED, creditLine.getMessage());
    }

    @Test
    void setASalesAgentMessage() {
        final CreditLineDomain creditLine = new CreditLineDomain(null, "1234569", FoundingTypeDomain.Startup,
                21550.55, 19500.00, 7183.517,
                LocalDateTime.now(), null, null, null, null);

        creditLine.setSalesAgentMessage();

        assertEquals(CreditLineDomain.SALES_AGENT_MESSAGE, creditLine.getMessage());
    }

    @Test
    void updateWithPreviousCreditLineAuthorized() {
        final CreditLineDomain previousCreditLineAuthorized = new CreditLineDomain(null, "1234569", FoundingTypeDomain.Startup,
                21550.55, 19500.00, 7183.517,
                LocalDateTime.now(), 8000.00, true, 7183.517, CreditLineDomain.CREDIT_AUTHORIZED);

        final CreditLineDomain creditLine = new CreditLineDomain(null, "1234569", FoundingTypeDomain.Startup,
                21550.55, 19500.00, 7183.517,
                LocalDateTime.now(), null, false, 0.0, null);

        creditLine.updateWithPreviousCreditLineAuthorized(previousCreditLineAuthorized);

        assertEquals(previousCreditLineAuthorized.getAccepted(), creditLine.getAccepted());
        assertEquals(previousCreditLineAuthorized.getCreditLineAuthorized(), creditLine.getCreditLineAuthorized());
        assertEquals(previousCreditLineAuthorized.getMessage(), creditLine.getMessage());
    }

}