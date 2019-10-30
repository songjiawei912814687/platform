package com.personnel.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "address_book")
public class AddressBook {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addressBookGenerator")
    @SequenceGenerator(name = "addressBookGenerator", sequenceName = "addressBookNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 8)
    private Integer id;

    @NotBlank(message = "名字不能为空")
    @Length(min=1, max=55, message="用户名长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="名字，长度不大于55",name="name",required=true)
    private String name;

    @NotNull
    @Pattern(regexp = "^1\\d{10}$",message = "手机号码格式不合法")
    @Column(nullable = false,length = 12)
    @ApiModelProperty(value="手机号码",name="phoneNumber",required=true)
    private String phoneNumber;

    @Column(length = 255)
    @ApiModelProperty(value="车牌号，长度不大于255",name="plateNo")
    private String plateNo;

    @NotNull
    @Range(min=0,max = 1,message = "性别的只能为0或1")
    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="性别，（男-0；女-1）",name="sex",example = "0",required=true)
    private Integer sex;


    @ApiModelProperty(value = "逻辑删除", name = "amputated")
    @Column(length = 1)
    private Integer amputated;

    @Column(nullable = true, length = 6)
    @ApiModelProperty(value = "创建人ID", name = "createdUserId")
    private Integer createdUserId;

    @Column(nullable = true, length = 55)
    @ApiModelProperty(value = "创建人name", name = "createdUserName")
    private String createdUserName;

    @Column(nullable = true)
    @ApiModelProperty(value = "创建时间", name = "createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Column(nullable = true)
    @ApiModelProperty(value = "最后更新时间", name = "lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(nullable = true, length = 6)
    @ApiModelProperty(value = "最后更新人Id", name = "lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(nullable = true, length = 55)
    @ApiModelProperty(value = "最后更新人name", name = "lastUpdateUserName")
    private String lastUpdateUserName;

    @Transient
    private List<String> plateNoList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public List<String> getPlateNoList() {
        return plateNoList;
    }

    public void setPlateNoList(List<String> plateNoList) {
        this.plateNoList = plateNoList;
    }
}
