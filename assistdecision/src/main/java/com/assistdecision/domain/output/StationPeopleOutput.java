package com.assistdecision.domain.output;

import com.assistdecision.model.StationPeople;

/**
 * @author: young
 * @project_name: assist-decision
 * @description:
 * @date: Created in 2019-02-27  18:17
 * @modified by:
 */
public class StationPeopleOutput extends StationPeople {

    private Integer averageMinute;

    private Integer takeNumber;

    private Integer compNumber;

    private String organizationNo;

    private String matters;

    /**组织名称 */
    private String organizationName;

    @Override
    public String getMatters() {
        return matters;
    }

    @Override
    public void setMatters(String matters) {
        this.matters = matters;
    }

    public Integer getCompNumber() {
        return compNumber;
    }

    public void setCompNumber(Integer compNumber) {
        this.compNumber = compNumber;
    }

    public String getOrganizationNo() {
        return organizationNo;
    }

    public void setOrganizationNo(String organizationNo) {
        this.organizationNo = organizationNo;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getTakeNumber() {
        return takeNumber;
    }

    public void setTakeNumber(Integer takeNumber) {
        this.takeNumber = takeNumber;
    }

    public Integer getAverageMinute() {
        return averageMinute;
    }

    public void setAverageMinute(Integer averageMinute) {
        this.averageMinute = averageMinute;
    }

}
