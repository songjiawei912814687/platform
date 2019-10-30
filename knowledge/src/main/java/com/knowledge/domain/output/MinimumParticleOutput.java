package com.knowledge.domain.output;

import com.knowledge.model.MinimumParticle;

import java.util.List;

public class MinimumParticleOutput extends MinimumParticle {
    private String qlName;

    private String contentInvolve;

    private List<MinimumParticleOutput> children = null;

    private String parentType;

    //受理机构
    private String acpInstitution;

    private String userId;

    private String employeeId;

    private String ouoCode;

    private String organizationId;

    public String getOrganizationId() {
        if(getOrgId()!=null){
            organizationId = getOrgId().toString();
        }
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOuoCode() {
        return ouoCode;
    }

    public void setOuoCode(String ouoCode) {
        this.ouoCode = ouoCode;
    }

    public String getQlName() {
        return qlName;
    }

    public String getAcpInstitution() {
        return acpInstitution;
    }

    public void setAcpInstitution(String acpInstitution) {
        this.acpInstitution = acpInstitution;
    }

    public void setQlName(String qlName) {
        this.qlName = qlName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getContentInvolve() {
        return contentInvolve;
    }

    public void setContentInvolve(String contentInvolve) {
        this.contentInvolve = contentInvolve;
    }

    public List<MinimumParticleOutput> getChildren() {
        return children;
    }

    public void setChildren(List<MinimumParticleOutput> children) {
        this.children = children;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }
}
