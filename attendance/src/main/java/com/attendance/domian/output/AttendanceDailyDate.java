package com.attendance.domian.output;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AttendanceDailyDate {
    private  String employeeName;

    private String employeeNo;

    private Integer employeeId;

    private String organizationName;

    private Integer organizationId;

    private String jobsName;

    private String verification;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date signInTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date signOutTime;

    private  Date punchTime;

    private Integer ruleConfigId;

    //迟到  是/否
    private String beLate;

    //迟到次数
    private Integer beLateTimes;

    //早退  是/否
    private String leaveEarly;

    //早退次数
    private Integer leaveEarlyTimes;

    //打卡  是/否
    private String punch ="否";

    //未打卡次数
    private Integer punchTimes;

    //状态  正常/异常
    private String statusName;

    private  String isLeave;

    private Integer hours;

    private String phoneNumber;


    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date attendanceDate;

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public Integer getRuleConfigId() {
        return ruleConfigId;
    }

    public void setRuleConfigId(Integer ruleConfigId) {
        this.ruleConfigId = ruleConfigId;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }


    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getJobsName() {
        return jobsName;
    }

    public void setJobsName(String jobsName) {
        this.jobsName = jobsName;
    }

    public Date getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(Date signInTime) {
        this.signInTime = signInTime;
    }

    public Date getSignOutTime() {
        return signOutTime;
    }

    public void setSignOutTime(Date signOutTime) {
        this.signOutTime = signOutTime;
    }

    public String getBeLate() {
        return beLate;
    }

    public void setBeLate(String beLate) {
        this.beLate = beLate;
    }

    public String getLeaveEarly() {
        return leaveEarly;
    }

    public void setLeaveEarly(String leaveEarly) {
        this.leaveEarly = leaveEarly;
    }

    public String getPunch() {
        return punch;
    }

    public void setPunch(String punch) {
        this.punch = punch;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Date getPunchTime() {
        return punchTime;
    }

    public void setPunchTime(Date punchTime) {
        this.punchTime = punchTime;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getIsLeave() {
        return isLeave;
    }

    public void setIsLeave(String isLeave) {
        this.isLeave = isLeave;
    }

    public Integer getBeLateTimes() {
        return beLateTimes;
    }

    public void setBeLateTimes(Integer beLateTimes) {
        this.beLateTimes = beLateTimes;
    }

    public Integer getLeaveEarlyTimes() {
        return leaveEarlyTimes;
    }

    public void setLeaveEarlyTimes(Integer leaveEarlyTimes) {
        this.leaveEarlyTimes = leaveEarlyTimes;
    }

    public Integer getPunchTimes() {
        return punchTimes;
    }

    public void setPunchTimes(Integer punchTimes) {
        this.punchTimes = punchTimes;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }
}
