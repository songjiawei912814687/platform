package com.api.model;

import com.common.Enum.TakeNumberEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description: 取号数据表
 * @date: Created in 2019-01-11  09:59
 * @modified by:
 */
@Entity
@EqualsAndHashCode
public class StationPeople implements Serializable {


    @Id
    @Column(nullable = false)
    private Integer resourceId;

    private Integer sortIndex;//服务序号

    @Column(nullable = true, length = 20)
    private String fullSequence;//带非数值字符的排队序号

    private Integer sequenceNumber;//排队序号

    @Column( length = 20)
    private String name;//人员名称

    @Column(nullable = false, length = 20)
    private String code;//等候者编码

    @Column(nullable = false, length = 25)
    private String mobile;//手机号

    @Column(nullable = true, length = 50)
    private String priorityCode;//优先规则编号

    @Column( length = 50)
    private String groupCode;//所属队列编号

    @Column(nullable = true, length = 100)
    private String groupName;//所属队列名称

    private String matters;//受理事项名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(nullable = false)
    private Date creationTime;//创建时间

    @Column( length = 50)
    private String scheduleCode;//工作时间段编号

    @Column( length = 50)
    private String deptCode;//所属部门或网点

    @Column(nullable = true, length = 100)
    private String deptName;//所属部门名称

    @Column(nullable = true, length = 200)
    private String source;//来源

    @Column(nullable = false, length = 1)
    private String statusCode;//

    //自定义的状态位,默认为已经取号状态
    @Column(length = 2)
    private Integer status= TakeNumberEnum.IS_TAKE.getCode();

    //取号时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date takeNumberTime;

    //取消时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cancelTime;

    //开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startWorkTime;

    //完成时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date completeTime;

    //过号
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date passTime;

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getFullSequence() {
        return fullSequence;
    }

    public void setFullSequence(String fullSequence) {
        this.fullSequence = fullSequence;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMatters() {
        return matters;
    }

    public void setMatters(String matters) {
        this.matters = matters;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTakeNumberTime() {
        return takeNumberTime;
    }

    public void setTakeNumberTime(Date takeNumberTime) {
        this.takeNumberTime = takeNumberTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Date getStartWorkTime() {
        return startWorkTime;
    }

    public void setStartWorkTime(Date startWorkTime) {
        this.startWorkTime = startWorkTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Date getPassTime() {
        return passTime;
    }

    public void setPassTime(Date passTime) {
        this.passTime = passTime;
    }
}
