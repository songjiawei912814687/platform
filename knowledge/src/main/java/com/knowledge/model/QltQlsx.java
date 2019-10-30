package com.knowledge.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Young
 * @description: 权力事项
 * @date: Created in 18:43 2018/10/23
 * @modified by:
 */
@Entity
@Table(name = "QLT_QLSX")
@Getter
@Setter
@EqualsAndHashCode
public class QltQlsx implements Serializable {

    @Id
    private String qlInnerCode;

    private String qlFullId;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="acceptAddressInfo", columnDefinition="CLOB")
    private String acceptAddressInfo;

    private Integer anticipateDay;

    /**行业主题**/
    @Column(length = 2)
    private String hangYeClassType;

    private Integer applyerminCount;

    private Long tongid;

    private String outFlowUrl;

    private String applyCondition;

    private String acpInstitution;

    private String applicableObject;

    private String applyType;

    private String applyTypeTel;

    private String banjianFinishfiles;

    private String bjtype;

    private String banRequirement;

    private String chargeBasis;

    private String chargeFlag;

    private String contentInvolve;

    private String countLimit;

    private String decInstitution;

    private String imFlowUrl;

    private String itemsource;

    private String lawbasis;

    private String leadDept;

    private String linkTel;

    private String materialInfo;

    private String outFlowDesc;

    private Integer promiseDay;

    private String qaInfo;

    private String qlKind;

    private String qlName;

    private String related;

    private String shixiangsctype;

    private String superviseTel;

    private String serviceDay;

    private String serviceMode;

    private String xingzhenxdrxy;

    private Integer hot=0;

    private Integer particles=0;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    @Column(length = 200)
    private String windowApplication;

    @Column(length = 200)
    private String onlineApplication;

    /**部门组织机构编码（废弃）*/
    private String ouorgcode;

    /**正式使用部门编码*/
    private String ouguid;

    /**部门主项编码*/
    private String qlMainitemId;

    /**部门子项编码*/
    private String qlSubitemId;

    /**网上办理地址*/
    private String webapplyurl;

    public QltQlsx() {
    }

}
