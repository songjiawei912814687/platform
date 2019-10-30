package com.screen.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "window")
@ApiModel(value = "window窗口", description = "窗口")
public class Window implements Serializable {
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "windowGenerator")
    @SequenceGenerator(name = "windowGenerator", sequenceName = "windowNew_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @NotBlank
    @Length(min=1, max=55, message="窗口名称长度只能在{min}和{max}之间")
    @Column(nullable = false,length=55)
    @ApiModelProperty(value="窗口名称,长度最大55",name="name",required = true)
    private String name;

    @NotBlank
    @Length(min=1, max=55, message="窗口编号长度只能在{min}和{max}之间")
    @Column(nullable=false, length=30)
    @ApiModelProperty(value="窗口编号,长度最大30", name="windowNo",required = true)
    private String windowNo;

    @NotNull
    @Range(min=1, max=99999999, message="所属机构ID只能在数字{min}和{max}之间")
    @Column(nullable = false,length = 8)
    @ApiModelProperty(value="所属机构,长度最大8",name="organizationId",required = true)
    private Integer organizationId;

    @Column(length=8)
    @ApiModelProperty(value="窗口负责人,长度最大38",name="userId")
    private Integer userId;


    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)($|(?!\\.$)\\.)){4}$",message = "IP格式不正确")
    @Column(length=15)
    @ApiModelProperty(value="窗口ip,长度最大15",name="windowIp")
    private String windowIp;

    @Column(length=255)
    @ApiModelProperty(value="备注,长度最大255",name="description")
    private String description;












    @Column(nullable = false)
    @ApiModelProperty(value="创建人ID",name="createdUserId")
    private Integer createdUserId;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="创建人name,长度最大55",name="createdUserName")
    private String createdUserName;

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdDateTime;

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    private Date lastUpdateDateTime;

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="最后更新人name,长度最大55",name="lastUpdateUserName")
    private String lastUpdateUserName;

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

    public String getWindowNo() {
        return windowNo;
    }

    public void setWindowNo(String windowNo) {
        this.windowNo = windowNo;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getWindowIp() {
        return windowIp;
    }

    public void setWindowIp(String windowIp) {
        this.windowIp = windowIp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
