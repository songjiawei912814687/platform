package com.meeting.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Young
 * @description: 组织机构实体类
 * @date: Created in 9:12 2018/8/31
 * @modified by:
 */


public class Organization implements Serializable {
    private Integer id;

    private Integer linkedId;

    private String organizationNo;

    private String organizationCode;

    private String name;

    private Integer parentId;

    private Integer type;

    private Integer assessmentState;

    private String phoneNumber;

    private String officePhone;

    private String fax;

    private String address;

    private String description;

    private Integer leadership;

    private Integer departmentalManager;

    private Integer amputated;

    private String path;

    private Integer createdUserId;//

    private String createdUserName;//

    private Date createdDateTime;//

    private Date lastUpdateDateTime;//

    private Integer lastUpdateUserId;//

    private String lastUpdateUserName;//

    private Integer displayOrder;

    /**0代表不下发，1-代表需要下发*/
    @Column(length = 2)
    @ApiModelProperty(value="是否下发到取号叫号",name="isSyncQueue",required=true)
    private Integer isSyncQueue;


    public Integer getIsSyncQueue() {
        return isSyncQueue;
    }

    public void setIsSyncQueue(Integer isSyncQueue) {
        this.isSyncQueue = isSyncQueue;
    }


    public Integer getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(Integer linkedId) {
        this.linkedId = linkedId;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrganizationNo() {
        return organizationNo;
    }

    public void setOrganizationNo(String organizationNo) {
        this.organizationNo = organizationNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAssessmentState() {
        return assessmentState;
    }

    public void setAssessmentState(Integer assessmentState) {
        this.assessmentState = assessmentState;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLeadership() {
        return leadership;
    }

    public void setLeadership(Integer leadership) {
        this.leadership = leadership;
    }

    public Integer getDepartmentalManager() {
        return departmentalManager;
    }

    public void setDepartmentalManager(Integer departmentalManager) {
        this.departmentalManager = departmentalManager;
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public Integer getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(Integer lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public String getLastUpdateUserName() {
        return lastUpdateUserName;
    }

    public void setLastUpdateUserName(String lastUpdateUserName) {
        this.lastUpdateUserName = lastUpdateUserName;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public Organization() {
    }

    public Organization(Integer id, String organizationNo, String fax, String address, String description, Integer leadership, Integer departmentalManager, Integer amputated, String path, Integer createdUserId, String createdUserName, Date createdDateTime, Date lastUpdateDateTime, Integer lastUpdateUserId, String lastUpdateUserName, Integer displayOrder) {
        this.organizationNo = organizationNo;
        this.name = name;
        this.parentId = parentId;
        this.type = type;
        this.assessmentState = assessmentState;
        this.phoneNumber = phoneNumber;
        this.officePhone = officePhone;
        this.fax = fax;
        this.address = address;
        this.description = description;
        this.leadership = leadership;
        this.departmentalManager = departmentalManager;
        this.amputated = amputated;
        this.path = path;
        this.createdUserId = createdUserId;
        this.createdUserName = createdUserName;
        this.createdDateTime = createdDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.lastUpdateUserId = lastUpdateUserId;
        this.lastUpdateUserName = lastUpdateUserName;
        this.id = id;
        this.displayOrder=displayOrder;
    }

}
