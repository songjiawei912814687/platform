package com.assistdecision.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "appraisal_plan")
@ApiModel(value = "AppraisalPlan考核计划", description = "考核计划")
public class AppraisalPlan implements Serializable {
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appraisalPlanGenerator")
    @SequenceGenerator(name = "appraisalPlanGenerator", sequenceName = "appraisalPlan_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="考核计划id,后台生成无法修改",name="id")
    private Integer id;

    @Range(min=1, max=999999, message="考核模板Id只能在{min}和{max}之间")
    @Column(nullable = false, length = 6)
    @ApiModelProperty(value="考核模板Id",name="templateId")
    private Integer templateId;

    @NotBlank
    @Length(min=1, max=255, message="考核计划名称长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 255)
    @ApiModelProperty(value="考核计划名称,长度最大255",name="name",required=true)
    private String name;

    @Range(min=1, max=9999, message="年只能在{min}和{max}之间")
    @Column(nullable = false, length = 4)
    @ApiModelProperty(value="年",name="year")
    private Integer year;

    @Range(min=1, max=12, message="月份只能在{min}和{max}之间")
    @Column(length = 2)
    @ApiModelProperty(value="月",name="year")
    private Integer month;

    @NotNull
    @Range(min=0, max=99, message="模板类型只能在{min}和{max}之间")
    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="模板类型",name="type",required=true)
    private Integer type;

    @NotNull
    @Range(min=0, max=99, message="模板类型只能在{min}和{max}之间")
    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="模板对象",name="objectType",required=true)
    private Integer objectType;

    @Range(min=0, max=999999, message="部门id只能在{min}和{max}之间")
    @Column(length = 6)
    @ApiModelProperty(value="部门id",name="organizationId")
    private Integer organizationId;

    @Range(min=0, max=999999, message="员工id只能在{min}和{max}之间")
    @Column(length = 6)
    @ApiModelProperty(value="员工id",name="employeeId")
    private Integer employeeId;

    @Column(length = 2)
    @ApiModelProperty(value="基准值",name="datumValue")
    private BigDecimal datumValue;

    @ApiModelProperty(value="最终得分",name="finalScore",required=true)
    private BigDecimal finalScore;


    @Column(length = 255)
    @ApiModelProperty(value="描述,长度最大255",name="description")
    private String description;

    @NotNull
    @Range(min=0, max=99, message="状态只能在{min}和{max}之间")
    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="状态",name="state")
    private Integer state;

    @Column(length = 6)
    @ApiModelProperty(value="排序",name="displayOrder")
    private Integer displayOrder;


    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="是否删除,长度最大1，默认0",name="amputated",required = true)
    private Integer amputated;

    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="是否服务明星,长度最大1，默认0",name="isStar")
    private Integer isStar;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="创建人ID,长度最大6",name="createdUserId")
    private Integer createdUserId;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="创建人name,长度最大55",name="createdUserName")
    private String createdUserName;

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="最后更新人Id,长度最大6",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="最后更新人name,长度最大55",name="lastUpdateUserName")
    private String lastUpdateUserName;


    @ApiModelProperty(value="审核时间",name="approvaDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approvaDateTime;

    @ApiModelProperty(value="审核人Id",name="approvaUserId")
    private Integer approvaUserId;

    @ApiModelProperty(value="审核人name",name="approvaUserName")
    private String approvaUserName;

    @ApiModelProperty(value="是否有效",name="isEnabled")
    private Integer isEnabled;

    @ApiModelProperty(value="缺勤天数",name="absenceDays")
    private Double absenceDays;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public BigDecimal getDatumValue() {
        return datumValue;
    }

    public void setDatumValue(BigDecimal datumValue) {
        this.datumValue = datumValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
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

    public Date getApprovaDateTime() {
        return approvaDateTime;
    }

    public void setApprovaDateTime(Date approvaDateTime) {
        this.approvaDateTime = approvaDateTime;
    }

    public Integer getApprovaUserId() {
        return approvaUserId;
    }

    public void setApprovaUserId(Integer approvaUserId) {
        this.approvaUserId = approvaUserId;
    }

    public String getApprovaUserName() {
        return approvaUserName;
    }

    public void setApprovaUserName(String approvaUserName) {
        this.approvaUserName = approvaUserName;
    }

    public BigDecimal getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(BigDecimal finalScore) {
        this.finalScore = finalScore;
    }

    public Integer getIsStar() {
        return isStar;
    }

    public void setIsStar(Integer isStar) {
        this.isStar = isStar;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Double getAbsenceDays() {
        return absenceDays;
    }

    public void setAbsenceDays(Double absenceDays) {
        this.absenceDays = absenceDays;
    }
}
