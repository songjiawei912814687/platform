package com.api.domain.output;

/**
 * @author: young
 * @project_name: svn
 * @description: 输入参数M返回出的集合
 * @date: Created in 2018-12-20  16:16
 * @modified by:
 */
public class QueryDeptOutput {

    //部门编号
    private String code;

    //部门名称
    private String name;

    //排队人数
    private Integer count;

    //等候人数
    private Integer waitingCount;

    //受理人数
    private Integer calledCount;

    //平均等待时长
    private Integer averageWaitingMinute;

    //平均服务时长
    private Integer averageServiceMinute;

    //预约未到人数
    private Integer appointedCount;

    //预约已到人数
    private Integer checkinedCount;

    //该部门下的窗口数量
    private Integer counterCount;

    //窗口开通数量
    private Integer onlineCounterCount;

    //受理事项数
    private Integer transCount;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getWaitingCount() {
        return waitingCount;
    }

    public void setWaitingCount(Integer waitingCount) {
        this.waitingCount = waitingCount;
    }

    public Integer getCalledCount() {
        return calledCount;
    }

    public void setCalledCount(Integer calledCount) {
        this.calledCount = calledCount;
    }

    public Integer getAverageWaitingMinute() {
        return averageWaitingMinute;
    }

    public void setAverageWaitingMinute(Integer averageWaitingMinute) {
        this.averageWaitingMinute = averageWaitingMinute;
    }

    public Integer getAverageServiceMinute() {
        return averageServiceMinute;
    }

    public void setAverageServiceMinute(Integer averageServiceMinute) {
        this.averageServiceMinute = averageServiceMinute;
    }

    public Integer getAppointedCount() {
        return appointedCount;
    }

    public void setAppointedCount(Integer appointedCount) {
        this.appointedCount = appointedCount;
    }

    public Integer getCheckinedCount() {
        return checkinedCount;
    }

    public void setCheckinedCount(Integer checkinedCount) {
        this.checkinedCount = checkinedCount;
    }

    public Integer getCounterCount() {
        return counterCount;
    }

    public void setCounterCount(Integer counterCount) {
        this.counterCount = counterCount;
    }

    public Integer getOnlineCounterCount() {
        return onlineCounterCount;
    }

    public void setOnlineCounterCount(Integer onlineCounterCount) {
        this.onlineCounterCount = onlineCounterCount;
    }

    public Integer getTransCount() {
        return transCount;
    }

    public void setTransCount(Integer transCount) {
        this.transCount = transCount;
    }
}
