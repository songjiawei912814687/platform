package com.wechatsug.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Young
 * @description: 收件箱保存记录用户回复的短信
 * @date: Created in 11:28 2018/10/25
 * @modified by:
 */
@Entity
@Table(name = "SMS_RECEIVE")
public class SMSReceive implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SMSReceiveGenerator")
    @SequenceGenerator(name = "SMSReceiveGenerator", sequenceName = "SMSReceive_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;


    @Range(min=0, max=99, message="业务类型值只能在{min}和{max}之间")
    @ApiModelProperty(value="业务类型",name="type")
    @Column(length = 2,nullable = true)
    private Integer type;

    /**
     * 回复方id
     */
    @Column(length = 10)
    private Integer sendId;

    /**
     * 回复方号码
     */
    @Column(length = 100)
    private String sendTelephone;

    /**
     * 回复时间
     */
    @Column(length = 50)
    private String sendTime;

    /**
     * 回复方姓名
     */
    @Column(length = 255)
    private String sendName;

    /**
     * 回复的内容
     */
    @Column(length = 2000)
    private String description;

    @Column(length = 2)
    private Integer state;

    @Column(length = 6)
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


    public SMSReceive() {
    }

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

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public String getSendTelephone() {
        return sendTelephone;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public void setSendTelephone(String sendTelephone) {
        this.sendTelephone = sendTelephone;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
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
}
