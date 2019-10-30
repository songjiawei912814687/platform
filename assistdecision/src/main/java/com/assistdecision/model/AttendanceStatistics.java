package com.assistdecision.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "attendance_statistics")

public class AttendanceStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statisticsGenerator")
    @SequenceGenerator(name = "statisticsGenerator", sequenceName = "statisticsNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 6)
    private Integer id;

    @Column(length = 55, columnDefinition="varchar(55) COMMENT '员工姓名' ")
    private  String employeeName;

    @Column(length = 55, columnDefinition="varchar(55) COMMENT '员工工号' ")
    private String employeeNo;

    @Column( columnDefinition="int(10) COMMENT '员工ID' ")
    private Integer employeeId;

    @Column( columnDefinition="int(10) COMMENT '组织机构ID' ")
    @ApiModelProperty(value="组织机构ID",name="organizationId")
    private Integer organizationId;

    @Column( columnDefinition="varchar(55) COMMENT '组织机构名称' ")
    private String organizationName;

    @Column( columnDefinition="varchar(55) COMMENT '职务名' ")
    private String jobsName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column( columnDefinition="COMMENT '签到时间' ")
    private Date signInTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column( columnDefinition=" COMMENT '签退时间' ")
    private Date signOutTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column( columnDefinition="COMMENT '考勤时间' ")
    private Date attendanceDate;

    private  Date punchTime;

    //迟到  是/否
    @Column( columnDefinition="varchar(20) COMMENT '是否迟到' ")
    private String beLate;

    //是否请假  是/否
    @Column( columnDefinition="varchar(20) COMMENT '是否请假' ")
    private String isLeave;

    //早退  是/否
    @Column( columnDefinition="varchar(20) COMMENT '是否早退' ")
    private String leaveEarly;


    //打卡  是/否
    @Column( columnDefinition="varchar(20) COMMENT '是否打卡' ")
    private String punch ="否";

    //状态  正常/异常
    @Column( columnDefinition="varchar(20) COMMENT '考勤状态' ")
    private String statusName;

    @Column( columnDefinition="int(2) COMMENT '缺勤时间，以小时为单位' ")
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

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
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
