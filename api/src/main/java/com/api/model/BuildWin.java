package com.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: young
 * @project_name:
 * @description: 建筑中标公告
 * @date: Created in 2018-11-30  15:20
 * @modified by:
 */
@Entity
public class BuildWin implements Serializable {

    private static final long serialVersionUID = -5544903827626062115L;
    @Id
    private String id;

    /**项目名称*/
    private String tenderName;

    /**项目负责人*/
    private String personnels;

    /**公示开始时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String publishStartTime;

    /**公示截止时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String publishEndTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    /**中标价*/
    private BigDecimal winPrice;

    /**中标单位*/
    private String enterpriseName;


    public BuildWin() {
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

    public String getPersonnels() {
        return personnels;
    }

    public void setPersonnels(String personnels) {
        this.personnels = personnels;
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

    public BigDecimal getWinPrice() {
        return winPrice;
    }

    public void setWinPrice(BigDecimal winPrice) {
        this.winPrice = winPrice;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

}
