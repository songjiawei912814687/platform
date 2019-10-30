package com.api.model;

import com.common.Enum.EvaluationResultsEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description: 评价结果表
 * @date: Created in 2019-01-11  10:09
 * @modified by:
 */
@Entity
public class EvaluationResults implements Serializable {

    @Id
    @Column(length = 6)
    private Integer resourceId;

    @Column( length = 32)
    private String employeeCode;//被评价的员工编号

    @Column(length = 32)
    private String counterCode;//被评价的窗口或诊室编号

    @Column( length = 32)
    private String evaluateCode;//评价结果分值，请参考管理后台的【评价项目设置】功能中的设置

    @Column( length = 10)
    private Integer evaluateVale;//评价结果分值，请参考管理后台的【评价项目设置】功能中的设置

    private String appraiserMobile;//评价人手机号码或联系电话

    private String evalDescription;//评价描述

    /**对应statisonPeople的resourceId**/
    private Integer queueingListId;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date creationTime;//评价时间

    @Column(nullable = false, length = 32)
    private String deptCode;//所属部门或网点

    /**评价状态 0-成功，1-不成功*/
    private Integer status = EvaluationResultsEnum.SUCCESS.getCode();


    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getCounterCode() {
        return counterCode;
    }

    public void setCounterCode(String counterCode) {
        this.counterCode = counterCode;
    }

    public String getEvaluateCode() {
        return evaluateCode;
    }

    public void setEvaluateCode(String evaluateCode) {
        this.evaluateCode = evaluateCode;
    }

    public Integer getEvaluateVale() {
        return evaluateVale;
    }

    public void setEvaluateVale(Integer evaluateVale) {
        this.evaluateVale = evaluateVale;
    }

    public String getAppraiserMobile() {
        return appraiserMobile;
    }

    public void setAppraiserMobile(String appraiserMobile) {
        this.appraiserMobile = appraiserMobile;
    }

    public String getEvalDescription() {
        return evalDescription;
    }

    public void setEvalDescription(String evalDescription) {
        this.evalDescription = evalDescription;
    }

    public Integer getQueueingListId() {
        return queueingListId;
    }

    public void setQueueingListId(Integer queueingListId) {
        this.queueingListId = queueingListId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
