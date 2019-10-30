package com.attendance.model;

import com.common.Enum.CheckStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Young
 * @description: 调休表
 * @date: Created in 20:33 2018/9/11
 * @modified by:
 */
@Entity
@Table(name = "off_application")
public class OffApplication implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "OffApplicationGenerator")
    @SequenceGenerator(name = "OffApplicationGenerator", sequenceName = "OffApplication_sequence",allocationSize = 1,initialValue = 1)
    @Column(nullable = false,length = 8)
    private Integer id;


    @NotNull
    @Range(min=1,max = 999999,message = "上级组织机构Id只能为0-999999之间的数字")
    @Column(nullable = false,length = 6)
    @ApiModelProperty(value = "组织Id", name = "organizationId", required = true)
    private Integer organizationId;



    @NotNull
    @Range(min=1,max = 99999999,message = "员工只能为0-999999之间的数字")
    @ApiModelProperty(value = "员工Id", name = "employeesId", required = true)
    @Column(nullable = false,length = 8)
    private Integer employeesId;


    @Column(nullable = false,length = 6)
    private Integer overTimeId;

    @Column(nullable = false)
    @ApiModelProperty(value = "加班日期",name = "overTimeDate" ,required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date overTimeDate;

    @Column(nullable = false)
    @ApiModelProperty(value = "调休日期",name = "restTime" ,required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date restTime;

    @Column(length = 255)
    @ApiModelProperty(name = "description",value = "描述")
    private String description;

    @Column(nullable = false)
    @ApiModelProperty(name = "status",value = "状态")
    private Integer status= CheckStatusEnum.UN_CHECK.getCode();

    @Column(length = 5)
    @ApiModelProperty(name = "orderNumber",value = "排序")
    private Integer oderNumber;

    @Column(length = 6,nullable = false)
    @ApiModelProperty(value="创建人ID",name="createdUserId")
    private Integer createdUserId;//

    @Column(length = 80,nullable = false)
    @ApiModelProperty(value="创建人name",name="createdUserName")
    private String createdUserName;//

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    private Date createdDateTime;//

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    private Date lastUpdateDateTime;//

    @Column(length = 6,nullable = false)
    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;//

    @Column(length = 80,nullable = false)
    @ApiModelProperty(value="最后更新人name",name="lastUpdateUserName")
    private String lastUpdateUserName;//

    @Column(length = 1,nullable = false)
    @ApiModelProperty(value="是否删除",name="amputated")
    private  Integer amputated;


    public Integer getOverTimeId() {
        return overTimeId;
    }

    public void setOverTimeId(Integer overTimeId) {
        this.overTimeId = overTimeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getEmployeesId() {
        return employeesId;
    }

    public void setEmployeesId(Integer employeesId) {
        this.employeesId = employeesId;
    }

    public Date getOverTimeDate() {
        return overTimeDate;
    }

    public void setOverTimeDate(Date overTimeDate) {
        this.overTimeDate = overTimeDate;
    }

    public Date getRestTime() {
        return restTime;
    }

    public void setRestTime(Date restTime) {
        this.restTime = restTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOderNumber() {
        return oderNumber;
    }

    public void setOderNumber(Integer oderNumber) {
        this.oderNumber = oderNumber;
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
}
