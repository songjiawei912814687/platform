package com.screen.domain.output;


import com.screen.model.StationPeople;

public class StationPeopleOutput extends StationPeople {

    private Integer takeCount;

    private Integer compCount;

    private String orgName;

    public Integer getTakeCount() {
        return takeCount;
    }

    public void setTakeCount(Integer takeCount) {
        this.takeCount = takeCount;
    }

    public Integer getCompCount() {
        return compCount;
    }

    public void setCompCount(Integer compCount) {
        this.compCount = compCount;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
