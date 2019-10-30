package com.assessment.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: Young
 * @description: 考核模板对象实体类
 * @date: Created in 9:12 2018/8/31
 * @modified by:
 */

@DynamicUpdate
@Entity
@Table(name = "appraisal_template_target")
@ApiModel(value = "AppraisalTemplateTarget考核模板对象", description = "考核模板对象")
public class AppraisalTemplateTarget implements Serializable {
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "templateTargetGenerator")
    @SequenceGenerator(name = "templateTargetGenerator", sequenceName = "templateTarget_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="考核模板对象id,后台生成无法修改",name="id")
    private Integer id;

    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="对象类型",name="objectType",required=true)
    private Integer objectType;

    @Column(nullable = false, length = 6)
    @ApiModelProperty(value="模板id",name="templateId",required=true)
    private Integer templateId;

    @Column(length = 6)
    @ApiModelProperty(value="部门id",name="organizationId")
    private Integer organizationId;

    @Column(length = 6)
    @ApiModelProperty(value="角色id",name="roleId")
    private Integer roleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
