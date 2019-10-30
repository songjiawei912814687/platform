package com.feedback.model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: Young
 * @description: 考核模板指标实体类
 * @date: Created in 9:12 2018/8/31
 * @modified by:
 */

@Entity
@Table(name = "appraisal_plan_item")
@ApiModel(value = "AppraisalPlanItem考核计划明细", description = "考核计划明细")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
