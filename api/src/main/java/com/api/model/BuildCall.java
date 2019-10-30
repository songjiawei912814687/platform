package com.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: young
 * @PROJECT_NAME:
 * @description: 建筑招标公告
 * @date: Created in 2018-11-30  15:07
 * @modified by:
 */
@Entity
public class BuildCall implements Serializable {

    @Id
    private String id;

    /**项目名称*/
    private String tenderName;

    /**建设单位*/
    private String tendererName;

    /**预算价格*/
    private BigDecimal thisBudget;

    /**开标时间*/
    private String openBidTime;

    /**投标截止时间*/
    private String publishEndTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    public BuildCall() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenderName() {
        return tenderName;
    }

    public void setTenderName(String tenderName) {
        this.tenderName = tenderName;
    }

    public String getTendererName() {
        return tendererName;
    }

    public void setTendererName(String tendererName) {
        this.tendererName = tendererName;
    }

    public BigDecimal getThisBudget() {
        return thisBudget;
    }

    public void setThisBudget(BigDecimal thisBudget) {
        this.thisBudget = thisBudget;
    }

    public String getOpenBidTime() {
        return openBidTime;
    }

    public void setOpenBidTime(String openBidTime) {
        this.openBidTime = openBidTime;
    }

    public String getPublishEndTime() {
        return publishEndTime;
    }

    public void setPublishEndTime(String publishEndTime) {
        this.publishEndTime = publishEndTime;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
