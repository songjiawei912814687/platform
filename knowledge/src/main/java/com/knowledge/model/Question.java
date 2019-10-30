package com.knowledge.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionGenerator")
    @SequenceGenerator(name = "questionGenerator", sequenceName = "questionNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 8)
    private Integer id;

    @Length(min=1, max=255, message="问题标题长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 255)
    @ApiModelProperty(value="问题长度最大255",name="title",required = true)
    private String title;

    @Column(length = 255)
    @ApiModelProperty(value="内容,长度最大255",name="description",required = true)
    private String description;

    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="是否公开,长度最大1，默认0",name="isOpen",required = true)
    private Integer isOpen;

    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="是否置顶,长度最大1，默认0",name="isTop",required = true)
    private Integer isTop;

    @Range(min=0,max = 1,message = "是否删除只能为0或1")
    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="是否删除,长度最大1，默认0",name="amputated",required = true)
    private Integer amputated;

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

    @Column(nullable = false,length = 6)
    private Integer organizationId;

    @Column(nullable = false)
    @ApiModelProperty(value="类型")
    private Integer type;

    private Date releaseTime;

    @Column(nullable = false)
    @ApiModelProperty(value="发布状态")
    private Integer state;

    private Integer answerState;

//    @Length(min=1, max=55, message="用户名长度只能在{min}和{max}之间")
//    @Column(nullable = false,length = 55)
//    @ApiModelProperty(value="提问人姓名，长度不大于55",name="name",required=true)
    private String askName;

//    @Pattern(regexp = "^1\\d{10}$",message = "手机号码格式不合法")
//    @Column(nullable = false,length = 12)
//    @ApiModelProperty(value="提问人手机号码",name="phoneNumber",required=true)
    private String askTel;


    //资源
    @Transient
    private List<Attachment> attachmentList;

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
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

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAnswerState() {
        return answerState;
    }

    public void setAnswerState(Integer answerState) {
        this.answerState = answerState;
    }

    public String getAskName() {
        return askName;
    }

    public void setAskName(String askName) {
        this.askName = askName;
    }

    public String getAskTel() {
        return askTel;
    }

    public void setAskTel(String askTel) {
        this.askTel = askTel;
    }
}
