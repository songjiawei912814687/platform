package com.personnel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description: 排队叫号窗口对象
 * @date: Created in 2018-12-05  09:23
 * @modified by:
 */
@Entity
@Table(name = "QUEUE_WINDOW")
public class QueueWindow implements Serializable {

    private static final long serialVersionUID = -1185469107077615224L;
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quewGenerator")
    @SequenceGenerator(name = "quewGenerator", sequenceName = "quew_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(length = 6)
    private Integer windowId;

    /**编码等同于我们的窗口号*/
    @Column(length = 20,nullable = false)
    private String code;

    /**窗口名称*/
    @Column(length = 20,nullable = false)
    private String name;

    /**部门编码*/
    @Column(length = 20)
    private String deptCode;

    /**设备接入到网络中的IP地址，如：192.168.1.100*/
    @Column(length = 100)
    private String evaluatorAddress;

    /**设备的首页URL*/
    @Column(length = 200)
    private String homeUrl;

    /**下发状态默认为0-未下发。。。1-已经下发*/
    @Column(length = 2,nullable = false)
    private Integer state;

    /**是否可用，0-可用，1-不可用*/
    private Integer enabled;

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


    public QueueWindow() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWindowId() {
        return windowId;
    }

    public void setWindowId(Integer windowId) {
        this.windowId = windowId;
    }

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

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getEvaluatorAddress() {
        return evaluatorAddress;
    }

    public void setEvaluatorAddress(String evaluatorAddress) {
        this.evaluatorAddress = evaluatorAddress;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
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
