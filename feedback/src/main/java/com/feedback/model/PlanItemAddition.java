package com.feedback.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Young
 * @description: 指标分类实体类
 * @date: Created in 9:12 2018/8/31
 * @modified by:
 */

@DynamicUpdate
@Entity
@Table(name = "planItem_Addition")
@ApiModel(value = "AppraisalIndex指标分类", description = "指标分类")
@Data
public class PlanItemAddition implements Serializable {
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planItemAdditionGenerator")
    @SequenceGenerator(name = "planItemAdditionGenerator", sequenceName = "planItemAddition_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="考核计划明细附件id,后台生成无法修改",name="id")
    private Integer id;

    @NotNull
    @Range(min=0, max=999999, message="考核计划明细id只能在{min}和{max}之间")
    @Column(nullable = false, length = 6)
    @ApiModelProperty(value="考核计划明细id",name="minimumParticleId",required=true)
    private Integer planItemId;

    @ApiModelProperty(value = "附件路径", name = "url", required = true)
    @Column(nullable = false)
    private String url;

    @ApiModelProperty(value = "文件名", name = "fileName", required = true)
    @Column(length =105,nullable = false)
    private String fileName;

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

    /**附件数量*/
    @Transient
    private Integer total;

    public PlanItemAddition() {
    }
}
