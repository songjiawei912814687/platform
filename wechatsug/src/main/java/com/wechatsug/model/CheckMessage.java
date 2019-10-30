package com.wechatsug.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description: 短信验证实体类
 * @date: Created in 2019-01-03  15:30
 * @modified by:
 */

@Entity
public class CheckMessage implements Serializable {

    private static final long serialVersionUID = 2586067093441938671L;

    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checkMessageGenerator")
    @SequenceGenerator(name = "checkMessageGenerator", sequenceName = "checkMessage_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    /**手机号码*/
    @Column(length = 20,nullable = false)
    private String phone;

    /**验证码*/
    @Column(length = 20,nullable = false)
    private Integer authCode;

    /**过期时间*/
    @Column(length = 20,nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String expireDate;

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Column(length = 1)
    @ApiModelProperty(value="是否删除,0是未删除，1是删除",name="amputated")
    private Integer amputated=0;

    public CheckMessage() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAuthCode() {
        return authCode;
    }

    public void setAuthCode(Integer authCode) {
        this.authCode = authCode;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }
}
