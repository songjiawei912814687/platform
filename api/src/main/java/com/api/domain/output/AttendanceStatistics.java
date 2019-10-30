package com.api.domain.output;


import java.util.Date;

public class AttendanceStatistics {

    private Integer id;

    private  String employeeName;

    private String employeeNo;

    private Integer employeeId;

    private Integer organizationId;

    private String organizationName;

    private String jobsName;

    private Date signInTime;

    private Date signOutTime;

    private Date attendanceDate;

    private  Date punchTime;

    //迟到  是/否
    private String beLate;

    //早退  是/否
    private String leaveEarly;


    //打卡  是/否
    private String punch ="否";

    //状态  正常/异常
    private String statusName;

    private String isLeave;

    private Integer absenceHours;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
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

    public Date getPunchTime() {
        return punchTime;
    }

    public void setPunchTime(Date punchTime) {
        this.punchTime = punchTime;
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

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Integer getAbsenceHours() {
        return absenceHours;
    }

    public void setAbsenceHours(Integer absenceHours) {
        this.absenceHours = absenceHours;
    }

    public String getIsLeave() {
        return isLeave;
    }

    public void setIsLeave(String isLeave) {
        this.isLeave = isLeave;
    }
}