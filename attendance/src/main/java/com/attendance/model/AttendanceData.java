package com.attendance.model;

import com.common.Enum.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "attendance_data")
public class AttendanceData implements Serializable {

    @Id
    @Column(length = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendanceDataGenerator")
    @SequenceGenerator(name = "attendanceDataGenerator", sequenceName = "attendanceDataNew_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;


    @Range(min=1, max=999999, message="员工ID只能在{min}和{max}之间")
    @Column(length=6)
    @ApiModelProperty(value="员工ID",name="employeeId")
    private Integer employeeId;



    @Range(min=1, max=999999, message="组织机构ID只能在{min}和{max}之间")
    @Column(length = 6)
    @ApiModelProperty(value="组织机构ID",name="organizationId")
    private Integer organizationId;




    @Column(length = 6)
    @ApiModelProperty(value="职务ID",name="jobsId")
    private Integer jobsId;

    @Column(length=255)
    @ApiModelProperty(value="考勤机名称",name="attendanceDeviceName")
    private String attendanceDeviceName;

    @Column(length = 50)
    private String  authentication;

    @Column(length = 2)
    @ApiModelProperty(value="状态",name="state")
    private Integer state= StatusEnum.USE.getCode();

    @ApiModelProperty(value="打卡时间",name="punchTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date punchTime;

    @Column(length=255)
    @ApiModelProperty(value="描述",name="description")
    private String description;













    @Column(nullable = false)
    @ApiModelProperty(value="创建人ID",name="createdUserId")
    private Integer createdUserId;

    @Column(nullable = false,length = 80)
    @ApiModelProperty(value="创建人name",name="createdUserName")
    private String createdUserName;

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(nullable = false,length = 80)
    @ApiModelProperty(value="最后更新人name",name="lastUpdateUserName")
    private String lastUpdateUserName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getJobsId() {
        return jobsId;
    }

    public void setJobsId(Integer jobsId) {
        this.jobsId = jobsId;
    }

    public String getAttendanceDeviceName() {
        return attendanceDeviceName;
    }

    public void setAttendanceDeviceName(String attendanceDeviceName) {
        this.attendanceDeviceName = attendanceDeviceName;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getPunchTime() {
        return punchTime;
    }

    public void setPunchTime(Date punchTime) {
        this.punchTime = punchTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


}
