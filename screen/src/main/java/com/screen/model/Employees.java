package com.screen.model;

import java.io.Serializable;
import java.util.Date;

public class Employees implements Serializable {

    private Integer id;

    private String employeeNo;

    private String name;

    private String icon;

    private Integer sex;

    private String citizenCards;

    private String bankCardNumber;

    private Integer userCompile;

    private String phoneNumber;

    private String officePhone;

    private String email;

    private Integer jobsId;

    private Integer maritalStatus;

    private Date dateBirth;

    private String idCard;

    private Integer recordOfFormalSchooling;

    private Integer activationId;

    private String national;

    private String office;

    private String plateNo;

    private Date inductionDateTime;

    private Date departureDateTime;

    private Integer workingState;

    private Integer attendanceState;

    private Integer windowState;

    private Integer reserveCadresState;

    private Integer partyMemberState;

    private Integer createdUserId;

    private String createdUserName;

    private Date createdDateTime;

    private Date lastUpdateDateTime;

    private Integer lastUpdateUserId;

    private String lastUpdateUserName;

    private  Integer amputated;

    private Integer organizationId;

    private Integer windowId;

    private Integer partyBranch;

    private Date joinPartyDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCitizenCards() {
        return citizenCards;
    }

    public void setCitizenCards(String citizenCards) {
        this.citizenCards = citizenCards;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getUserCompile() {
        return userCompile;
    }

    public void setUserCompile(Integer userCompile) {
        this.userCompile = userCompile;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getJobsId() {
        return jobsId;
    }

    public void setJobsId(Integer jobsId) {
        this.jobsId = jobsId;
    }

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getReserveCadresState() {
        return reserveCadresState;
    }

    public void setReserveCadresState(Integer reserveCadresState) {
        this.reserveCadresState = reserveCadresState;
    }

    public Integer getActivationId() {
        return activationId;
    }

    public void setActivationId(Integer activationId) {
        this.activationId = activationId;
    }

    public Integer getWorkingState() {
        return workingState;
    }

    public void setWorkingState(Integer workingState) {
        this.workingState = workingState;
    }

    public Integer getAttendanceState() {
        return attendanceState;
    }

    public void setAttendanceState(Integer attendanceState) {
        this.attendanceState = attendanceState;
    }

    public Integer getPartyMemberState() {
        return partyMemberState;
    }

    public void setPartyMemberState(Integer partyMemberState) {
        this.partyMemberState = partyMemberState;
    }

    public Integer getWindowState() {
        return windowState;
    }

    public void setWindowState(Integer windowState) {
        this.windowState = windowState;
    }

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public Integer getWindowId() {
        return windowId;
    }

    public void setWindowId(Integer windowId) {
        this.windowId = windowId;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public Date getInductionDateTime() {
        return inductionDateTime;
    }

    public void setInductionDateTime(Date inductionDateTime) {
        this.inductionDateTime = inductionDateTime;
    }

    public Date getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(Date departureDateTime) {
        this.departureDateTime = departureDateTime;
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

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }

    public Integer getRecordOfFormalSchooling() {
        return recordOfFormalSchooling;
    }

    public void setRecordOfFormalSchooling(Integer recordOfFormalSchooling) {
        this.recordOfFormalSchooling = recordOfFormalSchooling;
    }

    public Integer getPartyBranch() {
        return partyBranch;
    }

    public void setPartyBranch(Integer partyBranch) {
        this.partyBranch = partyBranch;
    }

    public Date getJoinPartyDate() {
        return joinPartyDate;
    }

    public void setJoinPartyDate(Date joinPartyDate) {
        this.joinPartyDate = joinPartyDate;
    }
}
