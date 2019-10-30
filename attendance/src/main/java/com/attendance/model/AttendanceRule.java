package com.attendance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@DynamicUpdate
@Table(name = "attendance_rule")
@Data
public class AttendanceRule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendanceRuleGenerator")
    @SequenceGenerator(name = "attendanceRuleGenerator", sequenceName = "attendanceRuleNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 6)
    private Integer id;

    @NotBlank(message = "名字不能为空")
    @Length(min=1, max=255, message="名称长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 255)
    @ApiModelProperty(value="名称,长度最长255",name="name",required=true)
    private String name;

    @NotBlank(message = "规则值不能为空")
    @Length(min=1, max=255, message="规则值名称长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 255)
    @ApiModelProperty(value="规则值，长度最长255",name="value",required=true)
    private String value;

    @Column(length = 30)
    @ApiModelProperty(value="类型",name="type")
    private Integer type;

    @Length(min=1, max=255, message="描述名称长度只能在{min}和{max}之间")
    @Column(length = 255)
    @ApiModelProperty(value="描述",name="description")
    private String description;

    @NotNull
    @Range(message = "状态的只能为1或3或者5，默认为1启用状态")
    @Column(nullable = false,length = 3)
    @ApiModelProperty(value="状态， 1启用 3停用 ",name="state",example = "1",required=true)
    private Integer state;

    @Range(min=1,max = 999,message = "排序只能在{min}和{max}之间")
    @Column(length = 3)
    @ApiModelProperty(value="排序,长度不超过3",name="displayOrder",required=true)
    private Integer displayOrder;









    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="创建人ID",name="createdUserId")
    private Integer createdUserId;//

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="创建人name",name="createdUserName")
    private String createdUserName;//

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;//

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;//

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;//

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="最后更新人name",name="lastUpdateUserName")
    private String lastUpdateUserName;//

    @Column(nullable =false,length = 6)
    @ApiModelProperty(value="是否删除",name="amputated")
    private  int amputated;

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

    public int getAmputated() {
        return amputated;
    }

    public void setAmputated(int amputated) {
        this.amputated = amputated;
    }
}
