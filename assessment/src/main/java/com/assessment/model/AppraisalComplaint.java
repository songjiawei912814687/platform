package com.assessment.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "appraisal_complaint")
@ApiModel(value = "考核申诉AppraisalComplaint", description = "考核申诉")
public class AppraisalComplaint implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AppraisalComplaintGenerator")
    @SequenceGenerator(name = "AppraisalComplaintGenerator", sequenceName = "AppraisalComplaint_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 8)
    private Integer id;



    @ApiModelProperty(value = "模板ID",name = "templateId")
    private Integer templateId;


    @ApiModelProperty(value = "模板类型",name = "templateType")
    private Integer templateType;


    @ApiModelProperty(value = "计划ID",name = "planId")
    private Integer planId;


    @Column(length = 255)
    @ApiModelProperty(value="计划名称，标题,长度最大255",name="name")
    private String name;



    @Column(length = 4)
    @ApiModelProperty(value="年",name="year")
    private Integer year;


    @Column(length = 2)
    @ApiModelProperty(value="月",name="year")
    private Integer month;


    @Column(length = 6)
    @ApiModelProperty(value="部门id",name="organizationId")
    private Integer organizationId;


    @Column(length = 6)
    @ApiModelProperty(value="员工id",name="employeeId")
    private Integer employeeId;


    @ApiModelProperty(value="基准值",name="datumValue")
    private BigDecimal datumValue;


    @ApiModelProperty(value="得分",name="score")
    private BigDecimal score;

    @Length(min=1, max=255, message="描述长度只能在{min}和{max}之间")
    @Column(length = 255)
    @ApiModelProperty(value="描述申述原因,长度最大255",name="description")
    private String description;


    @Range(min=0, max=99, message="状态只能在{min}和{max}之间")
    @Column(length = 2)
    @ApiModelProperty(value="状态",name="state")
    private Integer state;

    @Range(min=0, max=999999, message="排序只能在{min}和{max}之间")
    @Column(length = 6)
    @ApiModelProperty(value="排序",name="displayOrder")
    private Integer displayOrder;

    @Column(length = 6)
    @ApiModelProperty(value="创建人ID,长度最大6",name="createdUserId")
    private Integer createdUserId;

    @Column(length = 55)
    @ApiModelProperty(value="创建人name,长度最大55",name="createdUserName")
    private String createdUserName;


    @ApiModelProperty(value="创建日期时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Column(length = 6)
    @ApiModelProperty(value="最后更新人Id,长度最大6",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(length = 55)
    @ApiModelProperty(value="最后更新人name,长度最大55",name="lastUpdateUserName")
    private String lastUpdateUserName;


    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column()
    @ApiModelProperty(value="审核人Id",name="approvalUserId")
    private Integer approvalUserId;

    @Column(length = 80)
    @ApiModelProperty(value="审核人name",name="approvalUserName")
    private String approvalUserName;

    @ApiModelProperty(value="审核时间",name="approvalDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approvalDateTime;


    @Column( length = 2)
    @ApiModelProperty(value="审核状态",name="approvalState")
    private Integer approvalState;

    @Column(length = 1)
    @ApiModelProperty(value="是否删除,长度最大1，默认0",name="amputated")
    private Integer amputated;


    @Transient
    private List<Attachment> attachmentList;



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

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }



    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
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

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
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

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public Integer getApprovalUserId() {
        return approvalUserId;
    }

    public void setApprovalUserId(Integer approvalUserId) {
        this.approvalUserId = approvalUserId;
    }

    public String getApprovalUserName() {
        return approvalUserName;
    }

    public void setApprovalUserName(String approvalUserName) {
        this.approvalUserName = approvalUserName;
    }

    public Date getApprovalDateTime() {
        return approvalDateTime;
    }

    public void setApprovalDateTime(Date approvalDateTime) {
        this.approvalDateTime = approvalDateTime;
    }

    public Integer getApprovalState() {
        return approvalState;
    }

    public void setApprovalState(Integer approvalState) {
        this.approvalState = approvalState;
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }

    public AppraisalComplaint() {
    }


    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }
}

