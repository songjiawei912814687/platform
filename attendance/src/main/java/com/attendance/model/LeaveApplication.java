package com.attendance.model;

import com.common.Enum.CheckStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: Young
 * @description: 请假申请
 * @date: Created in 19:33 2018/9/11
 * @modified by:
 */

@Entity
@Table(name = "leave_application")
@Data
public class LeaveApplication implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "LeaveApplicationGenerator")
    @SequenceGenerator(name = "LeaveApplicationGenerator", sequenceName = "LeaveApplication_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value = "主键", name = "id", required = true)
    private Integer id;

    @Column(length = 2)
    private Integer startTimeKey;

    @Column(length = 2)
    private Integer endTimeKey;


    @Range(min=1,max = 999999,message = "上级组织机构Id只能为0-999999之间的数字")
    @Column(length = 6)
    @ApiModelProperty(value = "组织Id", name = "organizationId")
    private Integer organizationId;


    @Range(min=1,max = 99999999,message = "员工只能为0-999999之间的数字")
    @ApiModelProperty(value = "员工Id", name = "employeesId")
    @Column(length = 8)
    private Integer employeesId;

    @NotNull
    @Range(min=1,max = 99,message = "请假类型只能为0-99之间的数字")
    @Column(nullable = false,length = 2)
    @ApiModelProperty(value = "请假类型", name = "applicationType", required = true)
    private Integer applicationType;

    @NotNull
    @Column(nullable = false)
    @ApiModelProperty(value = "开始日期", name = "startDate", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    @NotNull
    @Column(nullable = false)
    @ApiModelProperty(value = "结束日期", name = "endDate", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    @NotNull
    @Length(min=1, max=255, message="开始时间长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 255)
    @ApiModelProperty(value = "开始时间", name = "startTime", required = true)
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private String startTime;

    @NotNull
    @Length(min=1, max=255, message="结束时间长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 255)
    @ApiModelProperty(value = "结束时间", name = "endTime", required = true)
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private String endTime;

    @ApiModelProperty(value = "完整开始时间", name = "endTime", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reportStartDate;

    @ApiModelProperty(value = "完整结束时间", name = "endTime", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reportEndDate;


    //是否循环
    @Range(min=1,max = 9)
    @Column(length = 1)
    @ApiModelProperty(value = "是否循环 否1 是3 ", name = "isCycle")
    private Integer isCycle;

    //是否补录
    @Range(min=1,max = 9,message = "是否补录只能为1或3")
    @Column(length = 1)
    @ApiModelProperty(value = "是否补录 否1 是3 ", name = "isCollection")
    private Integer isCollection;

    @Length(min=1, max=255, message="说明长度只能在{min}和{max}之间")
    @Column(length = 255)
    @ApiModelProperty(value = "说明", name = "description")
    private String description;

    @Column(nullable = false,length = 2)
    @ApiModelProperty(value = "状态", name = "status")
    private Integer status= CheckStatusEnum.UN_CHECK.getCode();

    //排序
    @Range(min=1,max = 99999999,message = "请假类型只能为0-99999999之间的数字")
    @Column(length = 8)
    @ApiModelProperty(value = "排序", name = "orderNumber")
    private Integer orderNumber;

    //时长
    @Column(length = 2)
    private Double duration;

    //资源
    @Transient
    private List<Attachment> attachmentList;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="创建人ID",name="createdUserId")
    private Integer createdUserId;//

    @ApiModelProperty(value="创建人name",name="createdUserName")
    private String createdUserName;//


    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;//

    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;//

    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;//

    @ApiModelProperty(value="最后更新人name",name="lastUpdateUserName")
    private String lastUpdateUserName;//

    @Column(length = 6)
    @ApiModelProperty(value="是否删除",name="amputated")
    private  Integer amputated;

    @Column(length = 255 , nullable = true)
    @ApiModelProperty(value = "替岗人员" , name = "replacePerson")
    private String replacePerson;

    /**第一个核销假时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date verificationTimeOne;

    /**第二个核销假时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date verificationTimeTwo;

    /**请假数据来源0-系统 1-微信*/
    @Column(length = 2)
    private Integer source;

    /**是否已经同步0-未同步 1-已同步*/
    @Column(length = 2)
    private Integer isSync;

    @Column(length = 2)
    /**微信请假的id*/
    private Integer leaveWeChatId;
}
