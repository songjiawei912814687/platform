package com.screenData.domain.output;




import com.screenData.model.Organization;

import java.util.List;

public class OrganizationOutput extends Organization {
    private List<OrganizationOutput> children = null;

    public List<OrganizationOutput> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizationOutput> children) {
        this.children = children;
    }

    private  Integer key;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public OrganizationOutput() {
    }

    public OrganizationOutput(Organization organization) {
        this.setId(organization.getId());
        this.key = organization.getId();
        this.setOrganizationNo( organization.getOrganizationNo());
        this.setName(organization.getName());
        this.setParentId(organization.getParentId());
        this.setOfficePhone(organization.getOfficePhone());
        this.setPhoneNumber(organization.getPhoneNumber());
        this.setAddress(organization.getAddress());
        this.setChildren(null);
    }

    private  String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    private  String leadershipName;

    private  String departmentalManagerName;

    public String getLeadershipName() {
        return leadershipName;
    }

    public void setLeadershipName(String leadershipName) {
        this.leadershipName = leadershipName;
    }

    public String getDepartmentalManagerName() {
        return departmentalManagerName;
    }

    public void setDepartmentalManagerName(String departmentalManagerName) {
        this.departmentalManagerName = departmentalManagerName;
    }

    public OrganizationOutput(Organization organization, String parentName, String leadershipName, String departmentalManagerName ) {
        this.setOrganizationNo(organization.getOrganizationNo()) ;
        this.setName(organization.getName());
        this.setParentId(organization.getParentId());
        this.setType(organization.getType());
        this.setAssessmentState(organization.getAssessmentState());
        this.setPhoneNumber(organization.getPhoneNumber());
        this.setOfficePhone(organization.getOfficePhone());
        this.setFax(organization.getFax());
        this.setAddress(organization.getAddress());
        this.setDescription(organization.getDescription());
        this.setDepartmentalManager(organization.getDepartmentalManager());
        this.setLeadership(organization.getLeadership());
        this.setCreatedUserName(organization.getCreatedUserName());
        this.setCreatedUserId(organization.getCreatedUserId());
        this.setCreatedDateTime(organization.getCreatedDateTime());
        this.setLastUpdateDateTime(organization.getLastUpdateDateTime());
        this.setLastUpdateUserId(organization.getLastUpdateUserId());
        this.setLastUpdateUserName(organization.getLastUpdateUserName());
        this.setId(organization.getId());
        this.setParentName(parentName);
        this.setDepartmentalManagerName(departmentalManagerName);
        this.setLeadershipName(leadershipName);
        this.setDisplayOrder(organization.getDisplayOrder());
    }
}
