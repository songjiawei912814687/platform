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
 * @description: 考核模板指标实体类
 * @date: Created in 9:12 2018/8/31
 * @modified by:
 */

@DynamicUpdate
@Entity
@Table(name = "appraisal_plan_item")
@ApiModel(value = "AppraisalPlanItem考核计划明细", description = "考核计划明细")
public class AppraisalPlanItem implements Serializable {

    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planItemGenerator")
    @SequenceGenerator(name = "planItemGenerator", sequenceName = "planItem_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="考核计划明细id,后台生成无法修改",name="id")
    private Integer id;

    @Column(nullable = false, length = 6)
    @ApiModelProperty(value="计划id",name="planId",required=true)
    private Integer planId;

    @Column(nullable = false, length = 6)
    @ApiModelProperty(value="指标id",name="indexId",required=true)
    private Integer indexId;

    @Column(nullable = false, length = 255)
    @ApiModelProperty(value="指标名称",name="indexName",required=true)
    private String indexName;

    @Column(nullable = false)
    @ApiModelProperty(value="基准值",name="datumValue",required=true)
    private BigDecimal datumValue;

    @Column
    @ApiModelProperty(value="累计最高加分",name="maxBonus")
    private BigDecimal maxBonus;

    @Range(min=0, max=99, message="对象类型只能在{min}和{max}之间")
    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="对象类型",name="objectType",required=true)
    private Integer objectType;

    @Column
    @ApiModelProperty(value="指标得分",name="maxBonus")
    private BigDecimal indexScore;

    @Column( length = 6)
    @ApiModelProperty(value="考核规则id",name="ruleId",required=true)
    private Integer ruleId;

    @Column(length = 255)
    @ApiModelProperty(value="考核规则名称",name="ruleName",required=true)
    private String ruleName;

    @Length(min=1, max=255, message="计分标准项长度只能在{min}和{max}之间")
    @Column(length = 255)
    @ApiModelProperty(value="计分标准项,长度最大255",name="description")
    private String description;

    @Range(min=0, max=99, message="计分公式只能在{min}和{max}之间")
    @Column( length = 2)
    @ApiModelProperty(value="计分公式",name="scoreType",required=true)
    private Integer scoreType;

    @Range(min=0, max=99, message="数据接口设置只能在{min}和{max}之间")
    @Column(length = 2)
    @ApiModelProperty(value="数据接口",name="dataType",required=true)
    private Integer dataType;

    @Range(min=0, max=99, message="计分设置只能在{min}和{max}之间")
    @Column(length = 2)
    @ApiModelProperty(value="计分设置",name="scoreource",required=true)
    private Integer scoreSource;

    @Column
    @ApiModelProperty(value="累计限额",name="cumulativeLimit")
    private BigDecimal cumulativeLimit;

    @Column
    @ApiModelProperty(value="数量",name="quantity")
    private BigDecimal quantity;

    @Column
    @ApiModelProperty(value="分值",name="score")
    private BigDecimal score;

    @Column
    @ApiModelProperty(value="打分",name="ruleScore")
    private BigDecimal ruleScore;


    @Column(nullable = false)
    @ApiModelProperty(value="默认打分",name="defaultScore",required=true)
    private BigDecimal defaultScore;

    @Column(length = 255)
    @ApiModelProperty(value="评分说明",name="ratingDescription",required=true)
    private String ratingDescription;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
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

    public BigDecimal getIndexScore() {
        return indexScore;
    }

    public void setIndexScore(BigDecimal indexScore) {
        this.indexScore = indexScore;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getScoreType() {
        return scoreType;
    }

    public void setScoreType(Integer scoreType) {
        this.scoreType = scoreType;
    }

    public Integer getScoreSource() {
        return scoreSource;
    }

    public void setScoreSource(Integer scoreSource) {
        this.scoreSource = scoreSource;
    }

    public BigDecimal getCumulativeLimit() {
        return cumulativeLimit;
    }

    public void setCumulativeLimit(BigDecimal cumulativeLimit) {
        this.cumulativeLimit = cumulativeLimit;
    }


    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getRuleScore() {
        return ruleScore;
    }

    public void setRuleScore(BigDecimal ruleScore) {
        this.ruleScore = ruleScore;
    }

    public BigDecimal getDefaultScore() {
        return defaultScore;
    }

    public void setDefaultScore(BigDecimal defaultScore) {
        this.defaultScore = defaultScore;
    }

    public String getRatingDescription() {
        return ratingDescription;
    }

    public void setRatingDescription(String ratingDescription) {
        this.ratingDescription = ratingDescription;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }
}
