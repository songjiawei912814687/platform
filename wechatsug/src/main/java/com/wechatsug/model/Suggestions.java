package com.wechatsug.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "suggestions")
@ApiModel(value = "Suggestions投诉建议", description = "投诉建议")
public class Suggestions implements Serializable {
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suggestionsGenerator")
    @SequenceGenerator(name = "suggestionsGenerator", sequenceName = "suggestions_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="投诉建议id,后台生成无法修改",name="id")
    private Integer id;

    @Column(length = 10)
    @ApiModelProperty(value="反馈信息id",name="feedbackId")
    private Integer feedbackId;

    /**父id*/
    @Column(length = 10)
    private Integer parentId;

    /**投诉内容*/
    @Column(length = 2000)
    private String content;


    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="来源",name="resource",required=true)
    private Integer dateResource;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="投诉人name,长度最大55",name="suggestionName")
    private String suggestionName;

    @Column(nullable = false,length = 12)
    @ApiModelProperty(value="投诉人手机号,长度最大12",name="suggestionPhoneNumber")
    private String suggestionPhoneNumber;

    @Column(nullable = true,length = 105)
    @ApiModelProperty(value="上级组织机构名称,长度最大105",name="upperOrganizationName")
    private String upperOrganizationName;


    @Column(length = 6)
    @ApiModelProperty(value="上级组织机构id,长度最大6",name="upperOrganizationId")
    private Integer upperOrganizationId;


    @Column(nullable = false,length = 105)
    @ApiModelProperty(value="组织机构名称,长度最大105",name="organizationName",required=true)
    private String organizationName;


    @Column(length = 6)
    @ApiModelProperty(value="组织机构id,长度最大6",name="organizationId")
    private Integer organizationId;

    @Column(length = 105)
    @ApiModelProperty(value="窗口名称,长度最大105",name="windowName")
    private String windowName;

    @Column(length = 6)
    @ApiModelProperty(value="窗口id,长度最大6",name="windowId")
    private Integer windowId;

    @Column(length = 105)
    @ApiModelProperty(value="人员名称,长度最大105",name="employeeName",required=true)
    private String employeeName;


    @Column(length = 6)
    @ApiModelProperty(value="人员id,长度最大6",name="employeeId")
    private Integer employeeId;

    @Column(length = 55)
    @ApiModelProperty(value="人员工号,长度最大55",name="employeeNo")
    private String employeeNo;

    @Column(nullable = false)
    @ApiModelProperty(value="投诉时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Range(min=0, max=99, message="发布状态只能在{min}和{max}之间")
    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="发布,0表示待发布，1表示已发布，默认为待发布",name="publish")
    private Integer publish;

    @ApiModelProperty(value="逾期时间",name="outDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date outDateTime;

    @Range(min=0, max=99, message="逾期状态只能在{min}和{max}之间")
    @Column(length = 2)
    @ApiModelProperty(value="逾期状态,0表示未逾期，1表示已逾期，默认为未逾期",name="outOfDate")
    private Integer outOfDate;

    @Column(nullable = true,length = 6)
    @ApiModelProperty(value="指定回复人ID,长度最大6",name="replyUserId")
    private Integer replyUserId;

    @Column(length = 255)
    @ApiModelProperty(value="处理结果,长度最大255",name="dealResult")
    private String dealResult;

    @ApiModelProperty(value="处理时间",name="dealTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dealTime;

    @Range(min=0, max=99, message="处理状态只能在{min}和{max}之间")
    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="处理状态,0表示未处理，1表示已处理，默认为未处理",name="dealState",required=true)
    private Integer dealState;

    /**添加投诉内容状态0-未添加。。。1-已经添加*/
    @Column(length = 10)
    private Integer replyType;

    /**附件列表*/
    @Transient
    private List<Attachment> attachmentList;

    @Range(min=0,max = 1,message = "是否删除只能为0或1")
    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="是否删除,长度最大1，默认0",name="amputated",required = true)
    private Integer amputated;

    @Column(length = 6)
    @ApiModelProperty(value="创建人ID,长度最大6",name="createdUserId")
    private Integer createdUserId;

    @Column(length = 55)
    @ApiModelProperty(value="创建人name,长度最大55",name="createdUserName")
    private String createdUserName;

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(length = 6)
    @ApiModelProperty(value="最后更新人Id,长度最大6",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(length = 55)
    @ApiModelProperty(value="最后更新人name,长度最大55",name="lastUpdateUserName")
    private String lastUpdateUserName;

    public Integer getReplyType() {
        return replyType;
    }

    public void setReplyType(Integer replyType) {
        this.replyType = replyType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDateResource() {
        return dateResource;
    }

    public void setDateResource(Integer dateResource) {
        this.dateResource = dateResource;
    }

    public String getSuggestionName() {
        return suggestionName;
    }

    public void setSuggestionName(String suggestionName) {
        this.suggestionName = suggestionName;
    }

    public String getSuggestionPhoneNumber() {
        return suggestionPhoneNumber;
    }

    public void setSuggestionPhoneNumber(String suggestionPhoneNumber) {
        this.suggestionPhoneNumber = suggestionPhoneNumber;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getWindowName() {
        return windowName;
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }

    public Integer getWindowId() {
        return windowId;
    }

    public void setWindowId(Integer windowId) {
        this.windowId = windowId;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Integer getPublish() {
        return publish;
    }

    public void setPublish(Integer publish) {
        this.publish = publish;
    }

    public Date getOutDateTime() {
        return outDateTime;
    }

    public void setOutDateTime(Date outDateTime) {
        this.outDateTime = outDateTime;
    }

    public Integer getOutOfDate() {
        return outOfDate;
    }

    public void setOutOfDate(Integer outOfDate) {
        this.outOfDate = outOfDate;
    }

    public Integer getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(Integer replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getDealResult() {
        return dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public Integer getDealState() {
        return dealState;
    }

    public void setDealState(Integer dealState) {
        this.dealState = dealState;
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

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public String getUpperOrganizationName() {
        return upperOrganizationName;
    }

    public void setUpperOrganizationName(String upperOrganizationName) {
        this.upperOrganizationName = upperOrganizationName;
    }

    public Integer getUpperOrganizationId() {
        return upperOrganizationId;
    }

    public void setUpperOrganizationId(Integer upperOrganizationId) {
        this.upperOrganizationId = upperOrganizationId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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


}
