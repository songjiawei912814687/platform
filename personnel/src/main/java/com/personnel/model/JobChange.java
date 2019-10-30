package com.personnel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="job_change")
public class JobChange implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobChangeGenerator")
    @SequenceGenerator(name = "jobChangeGenerator", sequenceName = "jobChangeNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 6)
    private Integer id;

    @NotNull
    @Range(min=1, max=99999999, message="员工id只能在{min}和{max}之间")
    @Column(nullable = false,length = 8)
    @ApiModelProperty(value="员工id,最大长度8",name="employeeId",required=true)
    private Integer employeeId;

    @Length(min=1, max=55, message="员工工号长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="员工工号,最大长度5",name="employeeNo",required=true)
    private String employeeNo;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="员工姓名",name="employeeName")
    private String employeeName;

    @NotNull
    @Range(min=1, max=999999, message="现部门id只能在{min}和{max}之间")
    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="现部门id,最大长度6",name="nowOrganizationId",required=true)
    private Integer nowOrganizationId;

    @Length(min=1, max=105, message="现部门名称长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 105)
    @ApiModelProperty(value="现部门名称,最大长度105",name="nowOrganizationName",required=true)
    private String nowOrganizationName;

    @NotNull
    @Range(min=1, max=999999, message="原有部门id只能在{min}和{max}之间")
    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="原有部门id,最大长度6",name="originalOrganizationId",required=true)
    private Integer originalOrganizationId;

    @Length(min=1, max=105, message="原用部门名称长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 105)
    @ApiModelProperty(value="原用部门名称,最大长度105",name="originalOrganizationName",required=true)
    private String originalOrganizationName;

    @Length(min=1, max=55, message="原职务名称长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="原职务,最大长度55",name="originalJobName",required=true)
    private String originalJobName;

    @NotNull
    @Range(min=1, max=999999, message="现职务id只能在{min}和{max}之间")
    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="现职务id,最大长度6",name="nowJobId",required=true)
    private Integer nowJobId;

    @NotNull
    @Range(min=1, max=999999, message="原职务id只能在{min}和{max}之间")
    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="原职务id,最大长度6",name="originalJobId",required=true)
    private Integer originalJobId;

    @Length(min=1, max=55, message="现职务名称长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="现职务,最大长度55",name="nowJobName",required=true)
    private String nowJobName;

    @Length(min=1, max=255, message="调用原因名称长度只能在{min}和{max}之间")
    @Column(length = 255)
    @ApiModelProperty(value="调用原因,最大长度255",name="description",required=false)
    private String description;

    @Column(nullable = false,length = 19)
    @ApiModelProperty(value="创建人ID,最大长度19",name="createdUserId")
    private Integer createdUserId;//

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="创建人name,最大长度55",name="createdUserName")
    private String createdUserName;//

    @Column(nullable = false,length=7)
    @ApiModelProperty(value="创建时间,最大长度7",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdDateTime;//

    @Column(nullable = false,length=7)
    @ApiModelProperty(value="最后更新时间,最大长度7",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;//

    @Column(nullable = false,length = 19)
    @ApiModelProperty(value="最后更新人Id,最大长度19",name="lastUpdateUserId")
    private Integer lastUpdateUserId;//

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="最后更新人name,最大长度55",name="lastUpdateUserName")
    private String lastUpdateUserName;//


    @Column(nullable = false, length = 1)
    @ApiModelProperty(value="是否已审核,长度最大1,状态：0-未审核，1-已审核",name="auditState")
    private Integer state;;

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

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public Integer getNowOrganizationId() {
        return nowOrganizationId;
    }

    public void setNowOrganizationId(Integer nowOrganizationId) {
        this.nowOrganizationId = nowOrganizationId;
    }

    public String getNowOrganizationName() {
        return nowOrganizationName;
    }

    public void setNowOrganizationName(String nowOrganizationName) {
        this.nowOrganizationName = nowOrganizationName;
    }

    public Integer getOriginalOrganizationId() {
        return originalOrganizationId;
    }

    public void setOriginalOrganizationId(Integer originalOrganizationId) {
        this.originalOrganizationId = originalOrganizationId;
    }

    public String getOriginalOrganizationName() {
        return originalOrganizationName;
    }

    public void setOriginalOrganizationName(String originalOrganizationName) {
        this.originalOrganizationName = originalOrganizationName;
    }

    public String getOriginalJobName() {
        return originalJobName;
    }

    public void setOriginalJobName(String originalJobName) {
        this.originalJobName = originalJobName;
    }

    public String getNowJobName() {
        return nowJobName;
    }

    public void setNowJobName(String nowJobName) {
        this.nowJobName = nowJobName;
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

    public Integer getNowJobId() {
        return nowJobId;
    }

    public void setNowJobId(Integer nowJobId) {
        this.nowJobId = nowJobId;
    }

    public Integer getOriginalJobId() {
        return originalJobId;
    }

    public void setOriginalJobId(Integer originalJobId) {
        this.originalJobId = originalJobId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
