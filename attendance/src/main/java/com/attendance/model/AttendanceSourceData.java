package com.attendance.model;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;


/**
 * @author: Young
 * @description: 考勤机拉取考勤源数据
 * @date: Created in 16:41 2018/10/9
 * @modified by:
 */
@Entity
public class AttendanceSourceData {

    @Id
    @Column(length = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendanceSourceDataGenerator")
    @SequenceGenerator(name = "attendanceSourceDataGenerator", sequenceName = "SourceData_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    //是否删除字段
    private Integer amputated;

    //门禁点名称
    @Column(length = 50)
    private String doorName;

    //门禁点UUID
    @Column(length = 50)
    private String doorUuid;

    //门禁事件UUID
    @Column(length = 50)
    private String eventUuid;

    //门禁事件类型
    @Column(length =8)
    private Integer eventType;

    //发生时间毫秒
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date eventTime;

    //事件名称
    @Column(length = 50)
    private String eventName;

    //读卡器类型
    @Column(length = 16)
    private Integer deviceType;

    //卡号，可以对应人员编号
    @Column(length = 100)
    private String cardNo;

    //海康系统自动生成ID
    @Column(length = 6)
    private Integer personId;

    //由我们系统下发，人员姓名
    @Column(length = 100)
    private String personName;

    //部门UUID
    @Column(length = 16)
    private String deptUuid;

    //部门名称
    @Column(length = 50)
    private String deptName;

    //联动图片URL
    @Column(length = 255)
    private String picUrl;

    //联动录像URL
    @Column(length = 255)
    private String videoUrl;


    public AttendanceSourceData() {
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    public String getDoorUuid() {
        return doorUuid;
    }

    public void setDoorUuid(String doorUuid) {
        this.doorUuid = doorUuid;
    }

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventName() {
        return eventName;
    }


    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getDeptUuid() {
        return deptUuid;
    }

    public void setDeptUuid(String deptUuid) {
        this.deptUuid = deptUuid;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
