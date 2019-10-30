package com.attendance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description: 考勤规则新表
 * @date: Created in 2019-03-13  08:44
 * @modified by:
 */

@Entity
public class AttendanceRuleNew implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attRuleGenerator")
    @SequenceGenerator(name = "attRuleGenerator", sequenceName = "attRule_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 6)
    private Integer id;

    /**考勤规则配置表的主键id*/

    private Integer attendanceRuleConfigId;


    @ApiModelProperty(value="规则名称,长度最长255",name="name")
    private String name;

    @ApiModelProperty(value="规则值，长度最长255",name="value")
    private String value;

    @ApiModelProperty(value="类型",name="type")
    private Integer type;


    @ApiModelProperty(value="描述",name="description")
    private String description;


    @ApiModelProperty(value="状态， 1启用 3停用 ",name="state",example = "1")
    private Integer state;

    @ApiModelProperty(value="排序,长度不超过3",name="displayOrder",required=true)
    private Integer displayOrder;


    @ApiModelProperty(value="创建人ID",name="createdUserId")
    private Integer createdUserId;

    @ApiModelProperty(value="创建人name",name="createdUserName")
    private String createdUserName;


    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;


    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;


    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;


    @ApiModelProperty(value="最后更新人name",name="lastUpdateUserName")
    private String lastUpdateUserName;

    @ApiModelProperty(value="是否删除",name="amputated")
    private  Integer amputated;

    public Integer getId() {
        return id;
    }

    public Integer getAttendanceRuleConfigId() {
        return attendanceRuleConfigId;
    }

    public void setAttendanceRuleConfigId(Integer attendanceRuleConfigId) {
        this.attendanceRuleConfigId = attendanceRuleConfigId;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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
