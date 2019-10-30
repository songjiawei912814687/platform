package com.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Young
 * @description: 交易成果公告
 * @date: Created in 16:45 2018/11/27
 * @modified by:
 */
@Entity
public class TradingResult implements Serializable {

    private static final long serialVersionUID = 446661410510288557L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resultGenerator")
    @SequenceGenerator(name = "resultGenerator", sequenceName = "resultGenerator",allocationSize = 1,initialValue = 1)
    private Integer id;

    /**成果公告*/
    @Column(length = 2000)
    private String announcement;

    /**产权所有人*/
    @Column(length = 2000)
    private String owner;

    /**中标价格*/
    private BigDecimal price;

    /**关联产权公告id*/
    private Integer tradingCenterId;

    /**开始时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date startTime;

    /**结束时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "创建时间", name = "createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    public TradingResult() {
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getTradingCenterId() {
        return tradingCenterId;
    }

    public void setTradingCenterId(Integer tradingCenterId) {
        this.tradingCenterId = tradingCenterId;
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
