package com.assessment.domian.input;

import com.assessment.model.AppraisalTemplate;

public class AppraisalTemplateInput extends AppraisalTemplate {
    private String objectIdList;

    public String getObjectIdList() {
        return objectIdList;
    }

    public void setObjectIdList(String objectIdList) {
        this.objectIdList = objectIdList;
    }
}
