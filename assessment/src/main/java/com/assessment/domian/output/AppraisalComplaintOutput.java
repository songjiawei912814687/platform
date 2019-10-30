package com.assessment.domian.output;

import com.assessment.core.util.AppConsts;
import com.assessment.model.AppraisalComplaint;

public class AppraisalComplaintOutput extends AppraisalComplaint {


    private String organizationName;
    //考核人员名称
    private String inspectionpersonnelName;

    //模板名称
    private String typeName;

    private String url;

    private String stateName;


    public String getTypeName() {
        if (getTemplateType() != null) {
            if (getTemplateType()== AppConsts.Annual_Assessment) {
                typeName = "年度考核";
            } else if (getTemplateType() == AppConsts.Monthly_Assessment) {
                typeName = "月度考核";
            }
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getInspectionpersonnelName() {
        return inspectionpersonnelName;
    }

    public void setInspectionpersonnelName(String inspectionpersonnelName) {
        this.inspectionpersonnelName = inspectionpersonnelName;
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
                stateName="审核通过";
                break;
            case 2:
                stateName="审核未通过";
            default:
                stateName="";
                break;
        }
        return stateName;
    }
}
