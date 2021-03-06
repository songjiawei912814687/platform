package com.screenData.domain.output;


import com.screenData.model.HchenStatistical;

/**
 * @author: Young
 * @description:
 * @date: Created in 19:09 2018/10/16
 * @modified by:
 */
public class HchenStatisticalOutput extends HchenStatistical {

    private Integer count;

    private Integer organizationId;

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
