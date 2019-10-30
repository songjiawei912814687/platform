package com.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description: 预约取号实体类
 * @date: Created in 2018-12-29  08:38
 * @modified by:
 */
@Entity
@Table(name = "APPOINTMENT_NUMBER")
@Getter
@Setter
public class AppointmentNumber implements Serializable {

    private static final long serialVersionUID = -8752321954804474829L;

    @Id
    private Integer id;

    /**预约的排队序号填写0*/
    @Column(length = 2,nullable = false,columnDefinition = "number(10) COMMENT '预约排队序号'")
    private Integer sequence;

    /**等候着唯一标识，填写身份证**/
    @Column(length = 50,nullable = false,columnDefinition = "varchar(100) COMMENT '等候着唯一标识，填写身份证号码'")
    private String code;

    /**等候姓名*/
    @Column(length = 20,nullable = false,columnDefinition = "varchar(100) COMMENT '等候着姓名'")
    private String name;

    /**等候着手机号码*/
    @Column(length = 20,nullable = false,columnDefinition = "varchar(100) COMMENT '等候着手机号码'")
    private String mobile;

    /**优先规则编号*/
    @Column(length = 20,nullable = false,columnDefinition = "varchar(100) COMMENT '有限规则编号'")
    private String priorityCode;

    /**窗口编号*/
    @Column(columnDefinition = "varchar(100) comment '窗口编号'")
    private String groupCode;

    /**排队编号填写窗口名称*/
    @Column(columnDefinition = "varchar(100) comment '队列编号，类似于窗口号'")
    private String groupName;

    /**窗口工作人员或医生在排队系统中的编号*/
    @Column(columnDefinition = "varchar(100) comment '窗口工作人员或医生在排队系统中的编号'")
    private String serviceEmpCode;

    /**预约时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(columnDefinition = "varchar(100) comment '预约时间'")
    private String appointDateTime;

    /**来源*/
    @Column(columnDefinition = "varchar(100) comment '预约来源A普通叫号，C为预约'")
    private String source;

    @Column(length = 6)
    @ApiModelProperty(value="创建人ID",name="createdUserId")
    private Integer createdUserId;

    @Column(nullable = false ,length = 80)
    @ApiModelProperty(value="创建人name",name="createdUserName")
    private String createdUserName;

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Column()
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(length = 6)
    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(length = 80)
    @ApiModelProperty(value="最后更新人name",name="lastUpdateUserName")
    private String lastUpdateUserName;

    @Column(length = 2,nullable = false,columnDefinition = "number(2) comment '是否删除'")
    @ApiModelProperty(value="是否取消预约",name="amputated")
    private  Integer amputated;
}
