package com.personnel.domian.output;



import com.common.Enum.IsSyncQueueNameEnum;
import com.personnel.model.Organization;

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

    public OrganizationOutput(Organization organization) {
        this.setId(organization.getId());
        this.key = organization.getId();
        this.setOrganizationNo( organization.getOrganizationNo());
        this.setDisplayOrder(organization.getDisplayOrder());
        this.setName(organization.getName());
        this.setParentId(organization.getParentId());
        this.setOfficePhone(organization.getOfficePhone());
        this.setPhoneNumber(organization.getPhoneNumber());
        this.setAddress(organization.getAddress());
        this.setChildren(null);
        this.setIsSyncQueue(organization.getIsSyncQueue());
        this.setCopyOrganNo(organization.getCopyOrganNo());
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

    private String isSyncQueueName;

    public String getIsSyncQueueName() {
        if(getIsSyncQueue()!=null){
            return IsSyncQueueNameEnum.getDesc(getIsSyncQueue());
        }
        return isSyncQueueName;
    }

    public void setIsSyncQueueName(String isSyncQueueName) {
        this.isSyncQueueName = isSyncQueueName;
    }

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
    private String ruleName;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public OrganizationOutput(Organization organization,String parentName,String leadershipName,String departmentalManagerName,String ruleName ) {
        this.setOrganizationNo(organization.getOrganizationNo()) ;
        this.setOrganizationCode(organization.getOrganizationCode());
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
        this.setAttendanceRuleConfigId(organization.getAttendanceRuleConfigId());
        this.setDisplayOrder(organization.getDisplayOrder());
        this.setFirstLetter(organization.getFirstLetter());
        this.setLinkedId(organization.getLinkedId());
        this.setIsSyncQueue(organization.getIsSyncQueue());
        this.setRuleName(ruleName);
        this.setConfigApprove(organization.getConfigApprove());
        this.setApproveRule(organization.getApproveRule());
        this.setOuorgcode(organization.getOuorgcode());
        this.setCopyOrganNo(organization.getCopyOrganNo());
    }
}
