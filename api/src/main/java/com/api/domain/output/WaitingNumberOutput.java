package com.api.domain.output;

import java.io.Serializable;

public class WaitingNumberOutput  implements Serializable{

    //部门名称
    private String organizationName;

    //部门取号数
    private Integer organizationFetchNumber;

    //部门叫号数
    private Integer organizationCallNumber;

    //部门未叫号数
    private Integer organizationNoCallNumber;

    //人数
    private Integer number;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getOrganizationFetchNumber() {
        return organizationFetchNumber;
    }

    public void setOrganizationFetchNumber(Integer organizationFetchNumber) {
        this.organizationFetchNumber = organizationFetchNumber;
    }

    public Integer getOrganizationCallNumber() {
        return organizationCallNumber;
    }

    public void setOrganizationCallNumber(Integer organizationCallNumber) {
        this.organizationCallNumber = organizationCallNumber;
    }

    public Integer getOrganizationNoCallNumber() {
        return organizationNoCallNumber;
    }

    public void setOrganizationNoCallNumber(Integer organizationNoCallNumber) {
        this.organizationNoCallNumber = organizationNoCallNumber;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
