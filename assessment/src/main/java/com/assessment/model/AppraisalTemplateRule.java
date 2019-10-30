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
 * @description: 考核模板规则实体类
 * @date: Created in 9:12 2018/8/31
 * @modified by:
 */

@DynamicUpdate
@Entity
@Table(name = "appraisal_template_rule")
@ApiModel(value = "AppraisalTemplateRule考核模板规则", description = "考核模板规则")
public class AppraisalTemplateRule implements Serializable {
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "templateRuleGenerator")
    @SequenceGenerator(name = "templateRuleGenerator", sequenceName = "templateRule_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="考核模板规则id,后台生成无法修改",name="id")
    private Integer id;

    @Column(nullable = false, length = 6)
    @ApiModelProperty(value="考核模板id",name="templateId",required=true)
    private Integer templateId;

    @Column(nullable = false, length = 6)
    @ApiModelProperty(value="考核模板指标id",name="templateId",required=true)
    private Integer templateIndexId;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="规则id",name="targetId")
    private Integer ruleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTemplateIndexId() {
        return templateIndexId;
    }

    public void setTemplateIndexId(Integer templateIndexId) {
        this.templateIndexId = templateIndexId;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }
}
