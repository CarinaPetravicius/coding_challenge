package com.challenge.coding_challenge.domain.model;

public class StartupType extends CompanyType {

    @Override
    public Double calculateTheCreditLine(Double monthlyRevenue, Double cashBalance) {
        return Math.max(calculateMonthlyRevenueRule(monthlyRevenue), calculateCashBalanceRule(cashBalance));
    }

}