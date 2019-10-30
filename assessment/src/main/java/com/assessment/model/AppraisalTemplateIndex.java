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
@Table(name = "appraisal_template_index")
@ApiModel(value = "AppraisalTemplateIndex考核模板指标", description = "考核模板指标")
public class AppraisalTemplateIndex implements Serializable {
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "templateIndexGenerator")
    @SequenceGenerator(name = "templateIndexGenerator", sequenceName = "templateIndex_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="考核模板指标id,后台生成无法修改",name="id")
    private Integer id;

    @Column(nullable = false, length = 6)
    @ApiModelProperty(value="模板id",name="templateId",required=true)
    private Integer templateId;

    @Column(nullable = false, length = 6)
    @ApiModelProperty(value="指标id",name="indexId",required=true)
    private Integer indexId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }
}
