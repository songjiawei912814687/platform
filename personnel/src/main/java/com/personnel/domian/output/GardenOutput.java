package com.personnel.domian.output;

import java.math.BigDecimal;

/**
 * @author: young
 * @project_name: svn
 * @description: 餐盘 系统获取园区参数
 * @date: Created in 2019-01-06  01:55
 * @modified by:
 */
public class GardenOutput {

    private BigDecimal areaNum;//校区编号

    private String areaName;//校区名称

    private String remark;//备注

    public BigDecimal getAreaNum() {
        return areaNum;
    }

    public void setAreaNum(BigDecimal areaNum) {
        this.areaNum = areaNum;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
