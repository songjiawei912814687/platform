package com.stamp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "sms_Send")
public class SMSSend {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "smsSendGenerator")
    @SequenceGenerator(name = "smsSendGenerator", sequenceName = "smsSend_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @ApiModelProperty(value="业务类型",name="type")
    @Column(length = 2,nullable = true)
    private Integer type;


    @Column(length = 100,nullable = false)
    @ApiModelProperty(value="接收方号码,长度最大100",name="receiveTelephone",required=true)
    private String receiveTelephone;

    @ApiModelProperty(value="接收方名称,长度最大255",name="receiveEmployeeName",required=true)
    @Column(length = 255,nullable = false)
    private String receiveEmployeeName;

    @NotBlank
    @Length(min=1, max=255, message="短信内容长度只能在{min}和{max}之间")
    @ApiModelProperty(value="短信内容,长度最大255",name="description",required=true)
    @Column(length = 255,nullable = false)
    private String description;

    @NotNull
    @Range(min=0, max=1, message="是否定时值只能在{min}和{max}之间")
    @ApiModelProperty(value="是否定时",name="type")
    @Column(length = 1,nullable = false)
    private Integer isTiming;

    //定时时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(nullable = true)
    private Date timingTime;

    @Column(nullable = false)
    private Integer state;

    @Column(length = 255)
    private Integer displayOrder;

    @ApiModelProperty(value = "逻辑删除", name = "amputated")
    @Column(length = 1)
    private Integer amputated;

    @Column(nullable = true, length = 6)
    @ApiModelProperty(value = "创建人ID", name = "createdUserId")
    private Integer createdUserId;

    @Column(nullable = true, length = 55)
    @ApiModelProperty(value = "创建人name", name = "createdUserName")
    private String createdUserName;

    @Column(nullable = true)
    @ApiModelProperty(value = "创建时间", name = "createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Column(nullable = true)
    @ApiModelProperty(value = "最后更新时间", name = "lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(nullable = true, length = 6)
    @ApiModelProperty(value = "最后更新人Id", name = "lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(nullable = true, length = 55)
    @ApiModelProperty(value = "最后更新人name", name = "lastUpdateUserName")
    private String lastUpdateUserName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getReceiveTelephone() {
        return receiveTelephone;
    }

    public void setReceiveTelephone(String receiveTelephone) {
        this.receiveTelephone = receiveTelephone;
    }

    public String getReceiveEmployeeName() {
        return receiveEmployeeName;
    }

    public void setReceiveEmployeeName(String receiveEmployeeName) {
        this.receiveEmployeeName = receiveEmployeeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsTiming() {
        return isTiming;
    }

    public void setIsTiming(Integer isTiming) {
        this.isTiming = isTiming;
    }

    public Date getTimingTime() {
        return timingTime;
    }

    public void setTimingTime(Date timingTime) {
        this.timingTime = timingTime;
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

    @ApiModelProperty(value = "", name = "isReply")
    @Column(length = 1)
    private Integer isReply;

    @ApiModelProperty(value = "回复截止时间", name = "replyLimitDate")
    @Column
    private Date replyLimitDate;

    public Integer getIsReply() {
        return isReply;
    }

    public void setIsReply(Integer isReply) {
        this.isReply = isReply;
    }

    public Date getReplyLimitDate() {
        return replyLimitDate;
    }

    public void setReplyLimitDate(Date replyLimitDate) {
        this.replyLimitDate = replyLimitDate;
    }
}
