package com.assessment.model;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

public class FeedBackInfo {
    private Integer id;

    private Integer amputated;

    private Integer appraiseState;

    private String appraiseTime;

    private Integer completeRate;

    private Date createdDateTime;

    private Integer createdUserId;

    private String createdUserName;

    private String deadline;

    private String employeesName;

    private String employeesNo;

    private Date feedbackTime;

    private Date lastUpdateDateTime;

    private Integer lastUpdateUserId;

    private String lastUpdateUserName;

    private String matters;

    private Integer organizationId;

    private String organizationName;

    private String personName;

    private String phone;

    private Integer satisfaction;

    private Integer sendState;

    private String windowNo;

    private Integer suggestId;

    private String oneDetail;

    private String twoDetail;

    private Integer resourceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }

    public Integer getAppraiseState() {
        return appraiseState;
    }

    public void setAppraiseState(Integer appraiseState) {
        this.appraiseState = appraiseState;
    }

    public String getAppraiseTime() {
        return appraiseTime;
    }

    public void setAppraiseTime(String appraiseTime) {
        this.appraiseTime = appraiseTime == null ? null : appraiseTime.trim();
    }

    public Integer getCompleteRate() {
        return completeRate;
    }

    public void setCompleteRate(Integer completeRate) {
        this.completeRate = completeRate;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
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
        this.createdUserName = createdUserName == null ? null : createdUserName.trim();
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline == null ? null : deadline.trim();
    }

    public String getEmployeesName() {
        return employeesName;
    }

    public void setEmployeesName(String employeesName) {
        this.employeesName = employeesName == null ? null : employeesName.trim();
    }

    public String getEmployeesNo() {
        return employeesNo;
    }

    public void setEmployeesNo(String employeesNo) {
        this.employeesNo = employeesNo == null ? null : employeesNo.trim();
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
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
        this.lastUpdateUserName = lastUpdateUserName == null ? null : lastUpdateUserName.trim();
    }

    public String getMatters() {
        return matters;
    }

    public void setMatters(String matters) {
        this.matters = matters == null ? null : matters.trim();
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName == null ? null : organizationName.trim();
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName == null ? null : personName.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Integer satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Integer getSendState() {
        return sendState;
    }

    public void setSendState(Integer sendState) {
        this.sendState = sendState;
    }

    public String getWindowNo() {
        return windowNo;
    }

    public void setWindowNo(String windowNo) {
        this.windowNo = windowNo == null ? null : windowNo.trim();
    }

    public Integer getSuggestId() {
        return suggestId;
    }

    public void setSuggestId(Integer suggestId) {
        this.suggestId = suggestId;
    }

    public String getOneDetail() {
        return oneDetail;
    }

    public void setOneDetail(String oneDetail) {
        this.oneDetail = oneDetail == null ? null : oneDetail.trim();
    }

    public String getTwoDetail() {
        return twoDetail;
    }

    public void setTwoDetail(String twoDetail) {
        this.twoDetail = twoDetail == null ? null : twoDetail.trim();
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    @Transient
    private Integer sizeVal;

    public Integer getSizeVal() {
        return sizeVal;
    }

    public void setSizeVal(Integer sizeVal) {
        this.sizeVal = sizeVal;
    }
}
