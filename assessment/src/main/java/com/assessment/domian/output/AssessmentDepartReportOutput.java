package com.assessment.domian.output;

import com.assessment.model.AssessmentDepartReport;

/**
 * @author: Young
 * @description:
 * @date: Created in 20:51 2018/10/11
 * @modified by:
 */
public class AssessmentDepartReportOutput extends AssessmentDepartReport {
    private  String  organizationCode;

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }
}
