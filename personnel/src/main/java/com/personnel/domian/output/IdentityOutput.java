package com.personnel.domian.output;

import java.math.BigDecimal;

/**
 * @author: young
 * @project_name: svn
 * @description: 获取餐盘身份参数
 * @date: Created in 2019-01-06  01:39
 * @modified by:
 */
public class IdentityOutput {

    private BigDecimal clsNum;//身份编号

    private String clsName;//身份名称

    private String remark;//备注

    public BigDecimal getClsNum() {
        return clsNum;
    }

    public void setClsNum(BigDecimal clsNum) {
        this.clsNum = clsNum;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
