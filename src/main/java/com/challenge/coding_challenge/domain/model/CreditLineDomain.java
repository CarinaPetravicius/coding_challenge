package com.challenge.coding_challenge.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CreditLineDomain {

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
        } else {
            creditLineAuthorized = 0.0;
        }
    }

}