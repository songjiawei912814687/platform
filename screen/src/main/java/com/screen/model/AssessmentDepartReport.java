package com.screen.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Young
 * @description: 部门考核报表
 * @date: Created in 13:44 2018/10/11
 * @modified by:
 */
@Entity
@Data
public class AssessmentDepartReport implements Serializable {

    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DepartReport")
    @SequenceGenerator(name = "DepartReport", sequenceName = "DepartReport_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="指标id,后台生成无法修改",name="id")
    private Integer id;

    @Column(length = 8)
    private Integer templateId;

    @Column(length = 8)
    private Integer planId;

    //单位id该月加入考核计划的所有组织机构名称
    @Column(length = 8)
    private Integer organizationId;

    @Column(length = 105)
    private String organizationName;

    //人均办件量从取号叫号系统中取出
    @Column(length = 8)
    private BigDecimal doThings;

    //一窗受理率
    @Column(length = 8)
    private BigDecimal windowAccept;

    //网上申报率取号叫号
    @Column(length = 8)
    private BigDecimal onlineReporting;

    //事项联办率
    @Column(length = 8)
    private BigDecimal matterSeec;

    //资料精简鸿查
    @Column(length = 8)
    private BigDecimal dataCompaction;

    //中介服务效率
    @Column(length = 8)
    private BigDecimal intermediaryService;

    //群众满意率取号叫号系统
    @Column(length = 8)
    private BigDecimal peopleSatisfaction;

    //工作记录
    @Column(length = 8)
    private BigDecimal jobLogging;

    //办理规范
    @Column(length = 8)
    private BigDecimal transactNorm;

    //创新工作
    @Column(length = 8)
    private BigDecimal innovationWork;

    //当月指标之前10大效能指标总和
    @Column(length = 8)
    private BigDecimal mouthIndex;

    //年
    @Column(length = 8)
    private Integer year;

    //月
    @Column(length = 8)
    private Integer month;

    @Column(length = 6)
    @ApiModelProperty(value="排序",name="displayOrder")
    private Integer displayOrder;


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
}
