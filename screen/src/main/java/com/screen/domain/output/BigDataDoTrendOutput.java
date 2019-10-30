package com.screen.domain.output;

import java.io.Serializable;

/**
 * @author: Young
 * @description: 办件趋势
 * @date: Created in 10:51 2018/10/31
 * @modified by:
 */
public class BigDataDoTrendOutput implements Serializable {

    private static final long serialVersionUID = -7519355321909275654L;
    private Integer organizationId;
    private Integer totalAmount;
    private String yearAndMonth;
    private String organizationName;

    public Integer getOrganizationId() {
        return organizationId;
    }

    public String getOrganizationName() {

        if(getOrganizationId()!=null){
            switch (organizationId){
                case 161:
                    organizationName="富阳行政中心";
                    break;
                case 162:
                    organizationName="场口分中心窗口";
                    break;
                case 163:
                    organizationName="新登分中心窗口";
                    break;
            }
        }

        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getYearAndMonth() {
        return yearAndMonth;
    }

    public void setYearAndMonth(String yearAndMonth) {
        this.yearAndMonth = yearAndMonth;
    }

    public BigDataDoTrendOutput() {
    }
}
