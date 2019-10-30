package com.personnel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-05  09:34
 * @modified by:
 */
@Entity
@Table(name = "QUEUE_EMPLOYEES")
public class QueueEmployees implements Serializable {
    private static final long serialVersionUID = 315674769730239616L;

    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "queeGenerator")
    @SequenceGenerator(name = "queeGenerator", sequenceName = "quee_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(length = 6,nullable = false)
    private Integer employeesId;

    /**员工公号*/
    @Column(length = 20,nullable = false)
    private String code;

    /**员工公号*/
    @Column(length = 20,nullable = false)
    private String userName;

    /**员工姓名*/
    @Column(length = 20,nullable = false)
    private String name;

    @Column(length = 50)
    private String password;

    /**部门编码*/
    @Column(length = 20)
    private String deptCode;

    /**星级默认给100*/
    private Integer rated;

    /**头像地址*/
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "AVATAR", columnDefinition = "CLOB", nullable = true)
    private String avatar;

    /**员工职务名称*/
    @Column(length = 20)
    private String jobTitle;

    /**介绍内容*/
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "BIOS", columnDefinition = "CLOB", nullable = true)
    private String bios;

    /**员工状态 true在职。。false-离职*/
    @Column(length = 2,nullable = false)
    private Integer enabled;

    /**下发状态，0-未下发，1-已经下发**/
    @Column(length = 2,nullable = false)
    private Integer queueState;

    @ApiModelProperty(value="创建人ID",name="createdUserId")
    private Integer createdUserId;

    @Column(length = 55)
    @ApiModelProperty(value="创建人name,长度最大55",name="createdUserName")
    private String createdUserName;


    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;


    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;


    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(length = 55)
    @ApiModelProperty(value="最后更新人name,长度最大55",name="lastUpdateUserName")
    private String lastUpdateUserName;

    public QueueEmployees() {
    }

    public Integer getQueueState() {
        return queueState;
    }

    public void setQueueState(Integer queueState) {
        this.queueState = queueState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeesId() {
        return employeesId;
    }

    public void setEmployeesId(Integer employeesId) {
        this.employeesId = employeesId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public Integer getRated() {
        return rated;
    }

    public void setRated(Integer rated) {
        this.rated = rated;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getBios() {
        return bios;
    }

    public void setBios(String bios) {
        this.bios = bios;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
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
}
