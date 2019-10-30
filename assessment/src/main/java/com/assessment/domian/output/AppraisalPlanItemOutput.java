package com.assessment.domian.output;

import com.assessment.model.AppraisalPlanItem;

import java.math.BigDecimal;

public class AppraisalPlanItemOutput extends AppraisalPlanItem{
    private String scoreTypeName;
    private String scoreSourceName;

    //计分公式   直接加分：0   直接减分：1
    //打分设置  系统默认值：0  手动调整 1  自动计算 3

    public String getScoreTypeName() {
        switch (getScoreType()){
            case 0:
                scoreTypeName="直接加分";
                break;
            case 1:
                scoreTypeName="直接减分";
                break;
            default:
                scoreTypeName="";
                break;
        }
        return scoreTypeName;
    }

    public void setScoreTypeName(String scoreTypeName) {
        this.scoreTypeName = scoreTypeName;
    }

    public String getScoreSourceName() {
        switch (getScoreSource()){
            case 0:
                scoreSourceName="系统默认值";
                break;
            case 1:
                scoreSourceName="手动调整";
                break;
            case 3:
                scoreSourceName="自动计算";
                break;
            default:
                scoreSourceName="";
                break;
        }
        return scoreSourceName;
    }

    public void setScoreSourceName(String scoreSourceName) {
        this.scoreSourceName = scoreSourceName;
    }

    private  Integer organizationId;

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    private BigDecimal finalScore;

    public BigDecimal getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(BigDecimal finalScore) {
        this.finalScore = finalScore;
    }

    private Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    private String organizationName;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
