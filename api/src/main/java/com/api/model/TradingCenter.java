package com.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Young
 * @description:  产权交易公告
 * @date: Created in 16:45 2018/11/27
 * @modified by:
 */
@Entity
public class TradingCenter implements Serializable {


    private static final long serialVersionUID = -2007103965250442629L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "centerGenerator")
    @SequenceGenerator(name = "centerGenerator", sequenceName = "center_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    /**交易公告*/
    @Column(length = 2000)
    private String announcement;

    /**项目名称*/
    @Column(length = 2000)
    private String projectName;

    /**起拍价格*/
    private BigDecimal price;

    /**开始时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date startTime;

    /**结束时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "创建时间", name = "createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;


















    public TradingCenter() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
