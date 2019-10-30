package com.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description: 迈瑞窗口sqlServer 窗口源数据表
 * @date: Created in 2019-01-14  15:01
 * @modified by:
 */
@Entity
public class Counter implements Serializable {

    @Id
    @Column(length = 6)
    private Integer id;

    /**窗口编号*/
    @Column(length = 20)
    private String code;

    /**窗口名称,同于窗口编号*/
    @Column(length = 50)
    private String name;

    /**所属组织编号*/
    @Column(length = 20)
    private String deptCode;

    @Column(length = 1000)
    private String groupCodes;

    /**是否在线1-在线 0-不在线*/
    @Column(length = 2)
    private Integer onlineFlag;

    /**当前登录的用户编号*/
    @Column(length = 20)
    private String currentEmpCode;

    /**最后登录时间*/
    private Date lastLoginTime;


    public Counter() {
    }

    public String getGroupCodes() {
        return groupCodes;
    }

    public void setGroupCodes(String groupCodes) {
        this.groupCodes = groupCodes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getOnlineFlag() {
        return onlineFlag;
    }

    public void setOnlineFlag(Integer onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    public String getCurrentEmpCode() {
        return currentEmpCode;
    }

    public void setCurrentEmpCode(String currentEmpCode) {
        this.currentEmpCode = currentEmpCode;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
