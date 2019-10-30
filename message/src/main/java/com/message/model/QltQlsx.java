package com.message.model;


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
@Getter
@Setter
@EqualsAndHashCode
public class QltQlsx  implements Serializable {

    @Id
    @Column(length = 50)
    private String QL_INNER_CODE;//权力内部编码

    @Column(length = 50)
    private String QL_Full_ID;//权力编码

    private String QL_STATE;// 在用	1  暂停	2 取消	3

    @Column(length = 50)
    private String QL_MAINITEM_ID; //主项编码

    @Column(length = 50)
    private String QL_SUBITEM_ID; //子项编码

    @Column(length = 255)
    private String QL_NAME;//权力名称

    @Column(length = 2)
    private String BJTYPE;//办件类型

    @Column(nullable = true)
    @Lob
    private String Content_involve;//涉及内容

    @Column(nullable = true)
    @Lob
    private String Applicable_object;//适用对象

    @Column(length = 2)
    private String QL_KIND;//权力事项类型

    @Column(length = 2)
    private String ITEMSOURCE;//权力来源

    private String WEBAPPLYURL;//网上办理地址

    @Column(length = 100)
    private String Acp_institution;//受理机构

    @Column(length = 100)
    private String Dec_institution;//决定机构

    @Column(length = 50)
    private String LEAD_DEPT;//责任科室

    @Column(length = 5)
    private String SHIXIANGSCtype;//事项审查类型

    @Column(length = 20)
    private String Apply_type;//申请方式

    @Column(length = 100)
    private String Apply_type_tel;//联系电话

    @Column(nullable = true)
    @Lob
    private String ACCEPT_ADDRESS_INFO;//受理地点信息

    @Column(length = 400)
    private String LINK_TEL;//咨询电话

    @Column(length = 80)
    private String SUPERVISE_TEL;//监督投诉电话

    @Column(length = 100)
    private String BANJIAN_FINISHFILES;//审批结果


    @Column(length = 11)
    private Integer PROMISE_DAY;//承诺期限

    @Column(length = 11)
    private Integer ANTICIPATE_DAY;//法定期限


    private String ANTICIPATE_TYPE;//法定期限单位


    @Column(length = 30)
    private String Service_mode;//送达方式

    @Column(length = 10)
    private String Service_day;//送达时限

    @Column(length = 11)
    private Integer APPLYERMIN_COUNT;//办事者到办事地点最少次数

    @Column(nullable = true)
    @Lob
    private String APPLY_CONDITION;//审批条件

    @Column(length = 2)
    private String Count_limit;//有无数量限制

    @Column(nullable = true)
    @Lob
    private String Ban_requirement;//禁止性要求

    @Column(nullable = true)
    @Lob
    private String RELATED;//相关附件信息


    @Column(nullable = true)
    @Lob
    private String OUT_FLOW_DESC;//办理程序

    @Column(length = 255)
    private String IM_FLOW_url;//内部流程图

    @Column(length = 1)
    private String CHARGE_FLAG;//是否收费

    @Column(nullable = true)
    @Lob
    private String CHARGE_BASIS;//收费依据

    @Lob
    private String CHARGEITEM_INFO;//收费标准

    @Column(nullable = true)
    @Lob
    private String LAWBASIS;//法定依据

    @Column(nullable = true)
    @Lob
    private String XINGZHENXDRXY;//事项者权利和义务


    private String XINGZHENXDRXZ;//行政相对人性质（适用对象)


    @Column(nullable = true)
    @Lob
    private String QA_INFO;//常见问题解答

    @Column(nullable = true)
    @Lob
    private String MATERIAL_INFO;//业务申报材料

    /**行业主题**/
    @Column(length = 2)
    private String hangYeClassType;

    private Date UPDATE_DATE;

    /**部门组织机构代码*/
    private String OUORGCODE;

    /**统一组织编码正式使用这个字段*/
    private String OUGUID;

    private Long tongID;

    private String ROWGUID;

    private String business_regulate;//业务审查规范

    public QltQlsx() {
    }
}
