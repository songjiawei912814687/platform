package com.assessment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@DynamicUpdate
@Table(name = "attendance_statistics")
@Data
public class AttendanceStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statisticsGenerator")
    @SequenceGenerator(name = "statisticsGenerator", sequenceName = "statisticsNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 6)
    private Integer id;

    @Column(length = 55)
    private  String employeeName;

    private String employeeNo;

    private Integer employeeId;

    @Column(length = 6)
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

    private  Date punchTime;

    //迟到  是/否
    private String beLate;

    //早退  是/否
    private String leaveEarly;


    //打卡  是/否
    private String punch ="否";

    //状态  正常/异常
    private String statusName;

    @Column(length = 2)
    @ApiModelProperty(value="缺勤时间，以小时为单位",name="absenceHours")
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
}