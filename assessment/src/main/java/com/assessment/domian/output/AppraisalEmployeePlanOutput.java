package com.assessment.domian.output;

import java.math.BigDecimal;

public class AppraisalEmployeePlanOutput {
    private String OrganizationName;
    private String name;
    private String bankCardNumber;
    private Integer effectiveMonth;
    private BigDecimal score;

    public String getOrganizationName() {
        return OrganizationName;
    }

    public void setOrganizationName(String organizationName) {
        OrganizationName = organizationName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public Integer getEffectiveMonth() {
        return effectiveMonth;
    }

    public void setEffectiveMonth(Integer effectiveMonth) {
        this.effectiveMonth = effectiveMonth;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
