package com.challenge.coding_challenge.domain.model;

public class BigType extends CompanyType {

    @Override
    public Double calculateTheCreditLine(Double monthlyRevenue, Double cashBalance) {
        return calculateCashBalanceRule(cashBalance);
    }

}