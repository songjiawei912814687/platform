package com.assessment.domian.output;

import com.assessment.model.AppraisalEmployeeRecord;

public class AppraisalEmployeeRecordOutput extends AppraisalEmployeeRecord {
    private String ruleName;
    private String indexName;
    private String employeeName;
    private String organizationName;
    private String url;
    private String scoreTypeName;
    private String scoreSourceName;
    private String stateName;


    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStateName() {
        switch (getState()){
            case 0:
                stateName="待审核";
                break;
            case 1:
                stateName="已审核";
                break;
            case 2:
                stateName="审核不通过";
                break;
            default:
                stateName="";
                break;
        }
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

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

}
