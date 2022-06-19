package com.challenge.coding_challenge.domain.model;

public enum FoundingTypeDomain {
    Startup(new StartupType()),
    SME(new SmeType()),
    Big(new BigType());

    final CompanyType companyType;

    FoundingTypeDomain(CompanyType companyType) {
        this.companyType = companyType;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

}