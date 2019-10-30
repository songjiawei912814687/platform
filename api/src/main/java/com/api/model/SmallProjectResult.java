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
 * @description: 2.4	获取小额工程成交结果公示接口设计
 * @date: Created in 2018-12-07  15:12
 * @modified by:
 */
@Entity
public class SmallProjectResult implements Serializable {

    private static final long serialVersionUID = 2598190198930974633L;
    @Id
    private String id;

    /**项目名称*/
    private String projectName;

    /**中标单位*/
    private String getUnit;

    /**中标价*/
    private BigDecimal bidCost;

    /**公示开始时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String publishStartTime;

    /**公示结束时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String publishEndTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    public SmallProjectResult() {
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

    public String getGetUnit() {
        return getUnit;
    }

    public void setGetUnit(String getUnit) {
        this.getUnit = getUnit;
    }

    public BigDecimal getBidCost() {
        return bidCost;
    }

    public void setBidCost(BigDecimal bidCost) {
        this.bidCost = bidCost;
    }

    public String getPublishStartTime() {
        return publishStartTime;
    }

    public void setPublishStartTime(String publishStartTime) {
        this.publishStartTime = publishStartTime;
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
