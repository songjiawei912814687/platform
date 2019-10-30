package com.assessment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Young
 * @description: 指标分类实体类
 * @date: Created in 9:12 2018/8/31
 * @modified by:
 */

@DynamicUpdate
@Entity
@Table(name = "appraisal_index")
@ApiModel(value = "AppraisalIndex指标分类", description = "指标分类")
public class AppraisalIndex implements Serializable {
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appraisalIndexGenerator")
    @SequenceGenerator(name = "appraisalIndexGenerator", sequenceName = "appraisalIndex_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="指标id,后台生成无法修改",name="id")
    private Integer id;

    @Range(min=0, max=99, message="指标类型值只能在{min}和{max}之间")
    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="指标类型",name="type")
    private Integer type;

    @NotBlank
    @Length(min=1, max=255, message="指标名称长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 255)
    @ApiModelProperty(value="指标名称名称,长度最大255",name="name",required=true)
    private String name;

    @NotNull
    @Column(nullable = false)
    @ApiModelProperty(value="基准值",name="datumValue",required=true)
    private BigDecimal datumValue;

    @Column
    @ApiModelProperty(value="累计最高加分",name="maxBonus")
    private BigDecimal maxBonus;

    @NotNull
    @Range(min=0, max=99, message="对象类型只能在{min}和{max}之间")
    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="对象类型",name="objectType",required=true)
    private Integer objectType;

    @Length(min=1, max=255, message="描述长度只能在{min}和{max}之间")
    @Column(length = 255)
    @ApiModelProperty(value="描述,长度最大255",name="description")
    private String description;

    @NotNull
    @Range(min=0, max=99, message="状态只能在{min}和{max}之间")
    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="状态",name="state")
    private Integer state;

    @Range(min=1, max=999999, message="排序只能在{min}和{max}之间")
    @Column(length = 6)
    @ApiModelProperty(value="排序",name="displayOrder")
    private Integer displayOrder;

    @Range(min=0,max = 1,message = "是否删除只能为0或1")
    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="是否删除,长度最大1，默认0",name="amputated",required = true)
    private Integer amputated;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="创建人ID,长度最大6",name="createdUserId")
    private Integer createdUserId;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="创建人name,长度最大55",name="createdUserName")
    private String createdUserName;

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="最后更新人Id,长度最大6",name="lastUpdateUserId")
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getDatumValue() {
        return datumValue;
    }

    public void setDatumValue(BigDecimal datumValue) {
        this.datumValue = datumValue;
    }

    public BigDecimal getMaxBonus() {
        return maxBonus;
    }

    public void setMaxBonus(BigDecimal maxBonus) {
        this.maxBonus = maxBonus;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
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
}
