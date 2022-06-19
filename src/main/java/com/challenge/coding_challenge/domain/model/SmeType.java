package com.challenge.coding_challenge.domain.model;

public class SmeType extends CompanyType {

    @Override
    public Double calculateTheCreditLine(Double monthlyRevenue, Double cashBalance) {
        return calculateMonthlyRevenueRule(monthlyRevenue);
    }

}