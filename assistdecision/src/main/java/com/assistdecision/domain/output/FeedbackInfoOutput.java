package com.assistdecision.domain.output;

import com.assistdecision.model.FeedbackInfo;

import java.math.BigDecimal;


/**
 * @author: Young
 * @description:
 * @date: Created in 14:22 2018/11/5
 * @modified by:
 */
public class FeedbackInfoOutput extends FeedbackInfo {

    private BigDecimal point;

    private String date;

    private Integer doCount;

    private Integer unSatisCount;

    private BigDecimal satisPoint;

    private BigDecimal completePoint;

    private String hotTime;

    //满意跑多次件数
    private Integer two;

    //不满意跑一次件数
    private Integer three;

    //不满意跑多次件数
    private Integer four;

    public Integer getTwo() {
        return two;
    }

    public void setTwo(Integer two) {
        this.two = two;
    }

    public Integer getThree() {
        return three;
    }

    public void setThree(Integer three) {
        this.three = three;
    }

    public Integer getFour() {
        return four;
    }

    public void setFour(Integer four) {
        this.four = four;
    }

    public Integer getUnSatisCount() {
        return unSatisCount;
    }

    public void setUnSatisCount(Integer unSatisCount) {
        this.unSatisCount = unSatisCount;
    }

    public String getHotTime() {
        return hotTime;
    }

    public void setHotTime(String hotTime) {
        this.hotTime = hotTime;
    }

    public BigDecimal getSatisPoint() {
        return satisPoint;
    }

    public void setSatisPoint(BigDecimal satisPoint) {
        this.satisPoint = satisPoint;
    }

    public BigDecimal getCompletePoint() {
        return completePoint;
    }

    public void setCompletePoint(BigDecimal completePoint) {
        this.completePoint = completePoint;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getDoCount() {
        return doCount;
    }

    public void setDoCount(Integer doCount) {
        this.doCount = doCount;
    }
}
