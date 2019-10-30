package com.attendance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**考勤报表实体类*/
@Entity
public class AttendanceStatisticsNew implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tisGenerator")
    @SequenceGenerator(name = "tisGenerator", sequenceName = "tis_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(length = 55)
    private String employeeName;

    private String employeeNo;

    private Integer employeeId;

    @ApiModelProperty(value="组织机构ID",name="organizationId")
    private Integer organizationId;

    private String organizationName;

    private String jobsName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date signInTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date signOutTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date attendanceDate;

    /**第一个核销假时间*/
    private Date verificationTimeOne;

    /**第二个核销假时间*/
    private Date verificationTimeTwo;

    //迟到  是/否
    private String beLate;

    //是否请假  是/否
    private String isLeave;

    //早退  是/否
    private String leaveEarly;

    //是否调休
    private String isOffTime;

    //打卡  是/否
    private String punch ;

    //状态  正常/异常
    private String statusName;

    @Column(length = 2)
    @ApiModelProperty(value="缺勤时间，以小时为单位",name="absenceHours")
    private BigDecimal absenceHours;

    //状态  正常/异常
    private String verification;

    public AttendanceStatisticsNew() {
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getIsOffTime() {
        return isOffTime;
    }

    public void setIsOffTime(String isOffTime) {
        this.isOffTime = isOffTime;
    }

    public Date getVerificationTimeOne() {
        return verificationTimeOne;
    }

    public void setVerificationTimeOne(Date verificationTimeOne) {
        this.verificationTimeOne = verificationTimeOne;
    }

    public Date getVerificationTimeTwo() {
        return verificationTimeTwo;
    }

    public void setVerificationTimeTwo(Date verificationTimeTwo) {
        this.verificationTimeTwo = verificationTimeTwo;
    }

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

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
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

    public BigDecimal getAbsenceHours() {
        return absenceHours;
    }

    public void setAbsenceHours(BigDecimal absenceHours) {
        this.absenceHours = absenceHours;
    }

    public String getIsLeave() {
        return isLeave;
    }

    public void setIsLeave(String isLeave) {
        this.isLeave = isLeave;
    }

}
