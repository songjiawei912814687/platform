package com.stamp.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: young
 * @project_name: platform
 * @description: 刻章信息
 * @date: Created in 2019-04-13  10:55
 * @modified by:
 */
@Entity
@Data
public class PrintInfo implements Serializable {
    private static final long serialVersionUID = -6193338789692746823L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "printInfoGenerator")
    @SequenceGenerator(name = "printInfoGenerator", sequenceName = "printInfo_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 6)
    private Integer id;

    @Column(length = 20)
    private String empNo;

    @Column(length = 20)
    private String empName;

    /**叫号时间*/
    @Column(length = 50)
    private String callTime;

    /**经办人姓名*/
    private String doName;

    /**经办人身份证号码*/
    private String  doIdentity;

    /**经办人手机号码*/
    private String doNumber;

    /**公司名称*/
    private String companyName;

    /**法人代表*/
    private String lealPerson;

    /**法人身份证号码*/
    private String lealIdentity;

    /**统一社会信用代码*/
    private String socialCode;

    /**企业经营地址*/
    private String companyAddress;

    /**服务套餐*/
    @Column(length = 2)
    private String servicePlan;

    /**刻章单位*/
    private Integer stampCompany;

    /**营业执照推送时间*/
    private String companyPushTime;

    /**营业执照推送时长*/
    private BigDecimal companyPushDuration;

    /**刻章单位点击接收时间*/
    @Column(length = 30)
    private String stampReceiveTime;

    /**公章送达时长*/
    private BigDecimal stampDuration;

    /**税务时长*/
    private BigDecimal taxDuration;

    /**银行开户时长*/
    private BigDecimal bankDuration;

    /**总时长*/
    private BigDecimal allDuration;

    private String bak;

    @Transient
    private List<Attachment> attachmentList;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="创建人ID",name="createdUserId")
    private Integer createdUserId;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="创建人name",name="createdUserName")
    private String createdUserName;

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    private Date createdDateTime;

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    private Date lastUpdateDateTime;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="最后更新人name",name="lastUpdateUserName")
    private String lastUpdateUserName;

    @Range(min=0,max = 1,message = "是否删除只能为0或1")
    @Column(nullable =false,length = 6)
    @ApiModelProperty(value="是否删除,0-未删除 1-已删除 默认为0",name="amputated",example = "0")
    private  Integer amputated;


}
