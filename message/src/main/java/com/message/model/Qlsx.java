package com.message.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @description:
 * @date: Created in 2019-07-24  08:49
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "QLSX")
public class Qlsx implements Serializable {

    @Id
    private Integer tongId;

    private String rowguid;//权力唯一标识

    private Date updateDate;//更新时间

    private String updateType;//更新类型

    private String qlKind;//事项类型

    private String qlFullId;//权力编码

    /**部门主项编码*/
    private String qlMainitemId;

    /**部门子项编码*/
    private String qlSubitemId;

    /**正式使用部门编码*/
    private String ouguid;

    private String itemsource;//权力来源

    private String qlName;//权力名称

    private String qlState;//权力状态

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="lawbasis", columnDefinition="CLOB")
    private String lawbasis;//法定依据

    private Integer anticipateDay;//法定期限

    private String anticipateType;//法定期限单位

    private Integer promiseDay;//承若期限

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="feeBasis", columnDefinition="CLOB")
    private String feeBasis;//征收标准

    private Integer applyerminCount;//办事者到达办事地点最少的次数

    private String qlDep;//实施机关

    private String acpInstitution;//受理机关

    private String decInstitution; //决定机构

    private String bjtype;//办件类型

    private String leadDept;//实施或牵头的处（科）室名称

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="contentInvolve", columnDefinition="CLOB")
    private String contentInvolve;//涉及内容

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="applicableObject", columnDefinition="CLOB")
    private String applicableObject;//试用对象说明

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="xingzhenxdrxy", columnDefinition="CLOB")
    private String xingzhenxdrxy;//行政相对人权利和义务

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="applyCondition", columnDefinition="CLOB")
    private String applyCondition;//审批条件

    private String countLimit;//有无数量限制

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="countNote", columnDefinition="CLOB")
    private String countNote;//数量限制情况说明

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="banRequirement", columnDefinition="CLOB")
    private String banRequirement;//禁止性要求


    private String shixiangsctype;//事项审查说明

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="shixiangsclx", columnDefinition="CLOB")
    private String shixiangsclx;//事项审查类型说明

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="banjianFinishfiles", columnDefinition="CLOB")
    private String banjianFinishfiles;//审批结果

    private String linkTel;//咨询电话

    private String superviseTel;//监督投诉电话

    private String applyType;//申请方式

    private String applyTypeTel;//联系电话

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="applyTypeMail", columnDefinition="CLOB")
    private String applyTypeMail;//邮箱

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="applyTypeFax", columnDefinition="CLOB")
    private String applyTypeFax;//传真

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="handleType", columnDefinition="CLOB")
    private String handleType;//办理方式

    /**网上办理地址*/
    private String webapplyurl;

    private String webconsulturl;//网上咨询地址

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="chargeBasis", columnDefinition="CLOB")
    private String chargeBasis;//收费依据

    private String chargeFlag;//是否收费

    /**行业主题**/
    @Column(length = 2)
    private String hangYeClassType;

    private String qlSubKind;//其他行政权力子类型

    private String imFlowUrl;//内部流程图

    private String outFlowUrl;//外部流程图

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="materialInfo", columnDefinition="CLOB")
    private String materialInfo;//业务申请材料

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="chargeitemInfo", columnDefinition="CLOB")
    private String chargeitemInfo;//收费项目

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="qaInfo", columnDefinition="CLOB")
    private String qaInfo;//常见问题解答

    @Lob
    @Basic(fetch= FetchType.LAZY)
    @Column(name="acceptAddressInfo", columnDefinition="CLOB")
    private String acceptAddressInfo;//受理地点信息

    private Date syncDate;//同步时间

    private String syncErrorDesc;//同步过程中错误信息描述

    private String farenurl;//电脑端法人认证地址

    private String geRenFlag;//个人标识\

    private String appwebapplyurl;//移动端网上办理地址

    private String appappointmenturl;//电脑端网上预约地址

    private String appointmenturl;//电脑端网上预约地址

    private String isWebappointment;//是否网上预约

    private String webappointmentperiod;//网上预约时段

    private String isExpress;//是否提供快递送达

    private String ispyc;//是否列入最多跑一次事项清单

    @Lob
    private String mbfarenadd;//手机端法人认证地址

    @Lob
    private String mbgerenflag;//手机端个人认证标识

    @Lob
    private String bak6;//是否为民生事项 （2018年8月27日新增字段）

    @Lob
    private String bak7;//是否应一证通办（2018年8月27日新增字段）

    @Lob
    private String bak8;//全省是否已实现一证通办（2018年8月27日新增字段）

    private String serviceDay;//送达时限

    private String serviceMode;//送达方式

    private String outFlowDesc;//办理程序



}
