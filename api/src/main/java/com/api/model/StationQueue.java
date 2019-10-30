package com.api.model;

import javax.persistence.*;

@Entity
@Table(name="station_queue")
public class StationQueue {

    @Id
    @Column(length = 8)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "station_queueGenerator")
    @SequenceGenerator(name = "station_queueGenerator", sequenceName = "station_queue_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(nullable = false)
    private Integer resourceId;

    @Column(nullable = false, length = 20)
    private String name;//窗口名

    @Column(nullable = false, length = 25)
    private String code;//唯一编号

    @Column(nullable = false, length = 32)
    private String perfix;

    @Column(nullable = false)
    private Integer minimumSequence;

    @Column(nullable = false)
    private Integer maximumSequence;

    @Column(nullable = false)
    private Integer maxWaitingOfRealtime;

    @Column(nullable = false)
    private Integer waitTimeoutMinute;

    @Column(nullable = false)
    private Integer serviceTimeoutMinute;

    @Column(nullable = false)
    private Integer priorityWeights;

    @Column(nullable = false)
    private String deptCode;

    @Column(nullable = false)
    private Integer averageServiceMinute;

    @Column(nullable = false)
    private String description;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPerfix() {
        return perfix;
    }

    public void setPerfix(String perfix) {
        this.perfix = perfix;
    }

    public Integer getMinimumSequence() {
        return minimumSequence;
    }

    public void setMinimumSequence(Integer minimumSequence) {
        this.minimumSequence = minimumSequence;
    }

    public Integer getMaximumSequence() {
        return maximumSequence;
    }

    public void setMaximumSequence(Integer maximumSequence) {
        this.maximumSequence = maximumSequence;
    }

    public Integer getMaxWaitingOfRealtime() {
        return maxWaitingOfRealtime;
    }

    public void setMaxWaitingOfRealtime(Integer maxWaitingOfRealtime) {
        this.maxWaitingOfRealtime = maxWaitingOfRealtime;
    }

    public Integer getWaitTimeoutMinute() {
        return waitTimeoutMinute;
    }

    public void setWaitTimeoutMinute(Integer waitTimeoutMinute) {
        this.waitTimeoutMinute = waitTimeoutMinute;
    }

    public Integer getServiceTimeoutMinute() {
        return serviceTimeoutMinute;
    }

    public void setServiceTimeoutMinute(Integer serviceTimeoutMinute) {
        this.serviceTimeoutMinute = serviceTimeoutMinute;
    }

    public Integer getPriorityWeights() {
        return priorityWeights;
    }

    public void setPriorityWeights(Integer priorityWeights) {
        this.priorityWeights = priorityWeights;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public Integer getAverageServiceMinute() {
        return averageServiceMinute;
    }

    public void setAverageServiceMinute(Integer averageServiceMinute) {
        this.averageServiceMinute = averageServiceMinute;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
