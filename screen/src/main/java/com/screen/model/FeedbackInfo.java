package com.screen.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Young
 * @description: 反馈信息实体类
 * @date: Created in 14:03 2018/11/5
 * @modified by:
 */
@Entity
@Table(name = "feedback_info")
public class FeedbackInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FeedbackInfoGenerator")
    @SequenceGenerator(name = "FeedbackInfoGenerator", sequenceName = "feedbackInfo_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    /**群众姓名**/
    @Column(length = 20)
    private String personName;

    /**群众电话**/
    @Column(length = 20,nullable = false)
    private String phone;

    /**受理部门**/
    @Column(length = 8)
    private Integer organizationId;

    /**受理部门名字**/
    @Column(length = 55)
    private String organizationName;

    /**窗口号**/
    @Column(length = 8)
    private String windowNo;

    /**员工姓名**/
    @Column(length = 55)
    private String employeesName;

    /**员工工号**/
    @Column(length = 20)
    private String employeesNo;

    /**办理事项**/
    @Column(length = 500)
    private String matters;

    /**反馈时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date feedbackTime;

    /**评价时间**/
    @Column(length = 50)
    private String appraiseTime;

    /**发送状态0-未发送，1-已发送**/
    @Column(length = 2)
    private Integer sendState;

    /**截止时间**/
    @Column(length = 50)
    private String deadline;

    /**评价状态0-未评价，1-已评价**/
    @Column(length = 2)
    private Integer appraiseState;

    /**满意度 0-满意，1-不满意**/
    @Column(length = 2)
    private Integer satisfaction;

    /**实现率 1-跑一次， 2-跑多次**/
    @Column(length = 2)
    private Integer completeRate;

    /**对应取号数据中的resourceId*/
    private Integer resourceId;

    /**第一次回复 不满意的详情*/
    @Column(length = 2000)
    private String oneDetail;

    /**第二次回复 不满意详情*/
    @Column(length = 2000)
    private String twoDetail;

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getSendState() {
        return sendState;
    }

    public void setSendState(Integer sendState) {
        this.sendState = sendState;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Column(length = 6)
    @ApiModelProperty(value="创建人ID",name="createdUserId")
    private Integer createdUserId;

    @Column(length = 80)
    @ApiModelProperty(value="创建人name",name="createdUserName")
    private String createdUserName;

    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(length = 6)
    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(length = 80)
    @ApiModelProperty(value="最后更新人name",name="lastUpdateUserName")
    private String lastUpdateUserName;

    @Column(length = 2)
    @ApiModelProperty(value="是否删除",name="amputated")
    private  Integer amputated;

    public String getOneDetail() {
        return oneDetail;
    }

    public void setOneDetail(String oneDetail) {
        this.oneDetail = oneDetail;
    }

    public String getTwoDetail() {
        return twoDetail;
    }

    public void setTwoDetail(String twoDetail) {
        this.twoDetail = twoDetail;
    }

    public FeedbackInfo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppraiseState() {
        return appraiseState;
    }

    public void setAppraiseState(Integer appraiseState) {
        this.appraiseState = appraiseState;
    }

    public String getAppraiseTime() {
        return appraiseTime;
    }

    public void setAppraiseTime(String appraiseTime) {
        this.appraiseTime = appraiseTime;
    }

    public String getEmployeesName() {
        return employeesName;
    }

    public void setEmployeesName(String employeesName) {
        this.employeesName = employeesName;
    }

    public String getEmployeesNo() {
        return employeesNo;
    }

    public void setEmployeesNo(String employeesNo) {
        this.employeesNo = employeesNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getWindowNo() {
        return windowNo;
    }

    public void setWindowNo(String windowNo) {
        this.windowNo = windowNo;
    }

    public String getMatters() {
        return matters;
    }

    public void setMatters(String matters) {
        this.matters = matters;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public Integer getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Integer satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Integer getCompleteRate() {
        return completeRate;
    }

    public void setCompleteRate(Integer completeRate) {
        this.completeRate = completeRate;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public Integer getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(Integer lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public String getLastUpdateUserName() {
        return lastUpdateUserName;
    }

    public void setLastUpdateUserName(String lastUpdateUserName) {
        this.lastUpdateUserName = lastUpdateUserName;
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }
}
