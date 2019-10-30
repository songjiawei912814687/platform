package com.feedback.domain.output;



import com.feedback.model.Organization;

public class OrganizationOutput extends Organization {

    private Integer linkId;

    private String linkName;

    private String linkNo;

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkNo() {
        return linkNo;
    }

    public void setLinkNo(String linkNo) {
        this.linkNo = linkNo;
    }
}
