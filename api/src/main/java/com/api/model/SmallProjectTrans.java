package com.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description:2.3	获取小额工程交易公告接口设计
 * @date: Created in 2018-12-07  15:06
 * @modified by:
 */
@Entity
public class SmallProjectTrans implements Serializable {

    @Id
    private String id;

    /**项目名称*/
    private String projectName;

    /**招标单位*/
    private String tenderUnit;

    /**预算价*/
    private BigDecimal budgtaryPrice;

    /**开标时间*/
    private String bidTime;

    /**投标截止时间*/
    private String publishEndTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    public SmallProjectTrans() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTenderUnit() {
        return tenderUnit;
    }

    public void setTenderUnit(String tenderUnit) {
        this.tenderUnit = tenderUnit;
    }

    public BigDecimal getBudgtaryPrice() {
        return budgtaryPrice;
    }

    public void setBudgtaryPrice(BigDecimal budgtaryPrice) {
        this.budgtaryPrice = budgtaryPrice;
    }

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
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
