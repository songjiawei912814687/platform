package com.assessment.domian.output;

public class AppraisalOrganizationOutput {
    private Integer organizationId;

    private Integer parentId;

    private String organizationName;

    private String parentName;

    private String orgaNo;

    private String path;

    private String copyOrganNo;


    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getOrgaNo() {
        return orgaNo;
    }

    public void setOrgaNo(String orgaNo) {
        this.orgaNo = orgaNo;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCopyOrganNo() {
        return copyOrganNo;
    }

    public void setCopyOrganNo(String copyOrganNo) {
        this.copyOrganNo = copyOrganNo;
    }
}
