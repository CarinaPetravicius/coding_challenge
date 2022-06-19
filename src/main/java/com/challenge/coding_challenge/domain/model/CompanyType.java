package com.challenge.coding_challenge.domain.model;

public abstract class CompanyType {

    public abstract Double calculateTheCreditLine(Double monthlyRevenue, Double cashBalance);

    public Double calculateMonthlyRevenueRule(Double monthlyRevenue) {
        return monthlyRevenue / 5;
    }

    public Double calculateCashBalanceRule(Double cashBalance) {
        return cashBalance / 3;
    }

}