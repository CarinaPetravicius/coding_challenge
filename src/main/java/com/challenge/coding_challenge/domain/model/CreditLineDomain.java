package com.challenge.coding_challenge.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CreditLineDomain {

    public static final String CREDIT_AUTHORIZED = "The credit line was authorized";
    public static final String CREDIT_NOT_AUTHORIZED = "The credit line was not authorized";
    public static final String SALES_AGENT_MESSAGE = "A sales agent will contact you";
    public static final Integer REQUEST_LIMIT_FOR_TOO_MANY_REQUEST = 3;
    public static final Integer MINUTES_PERIOD_FOR_TOO_MANY_REQUEST_FOR_APPROVED_CREDIT = 2;
    public static final Integer SECONDS_PERIOD_FOR_TOO_MANY_REQUEST_FOR_NOT_APPROVED_CREDIT = 30;
    public static final Integer LIMIT_OF_NOT_APPROVED = 3;

    private Long id;
    private String clientId;
    private FoundingTypeDomain foundingType;
    private Double cashBalance;
    private Double monthlyRevenue;
    private Double requestedCreditLine;
    private LocalDateTime requestedDate;
    private Double creditLineRecommended;
    private Boolean accepted;
    private Double creditLineAuthorized;
    private String message;

    public void calculateTheCreditLine() {
        if (foundingType.equals(FoundingTypeDomain.SME)) {
            creditLineRecommended = calculateMonthlyRevenueRule();
        } else {
            creditLineRecommended = Math.max(calculateMonthlyRevenueRule(), calculateCashBalanceRule());
        }
    }

    private Double calculateMonthlyRevenueRule() {
        return monthlyRevenue / 5;
    }

    private Double calculateCashBalanceRule() {
        return cashBalance / 3;
    }

    public void releaseTheCreditLine() {
        accepted = creditLineRecommended > requestedCreditLine;
        if (accepted) {
            creditLineAuthorized = requestedCreditLine;
            message = CREDIT_AUTHORIZED;
        } else {
            creditLineAuthorized = 0.0;
            message = CREDIT_NOT_AUTHORIZED;
        }
    }

    public void setSalesAgentMessage() {
        message = SALES_AGENT_MESSAGE;
    }

    public void updateWithPreviousCreditLineAuthorized(CreditLineDomain previousCreditLine) {
        accepted = previousCreditLine.getAccepted();
        creditLineAuthorized = previousCreditLine.getCreditLineAuthorized();
        message = previousCreditLine.getMessage();
    }

}