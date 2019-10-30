package com.feedback.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Young
 * @description: 反馈信息实体类
 * @date: Created in 14:03 2018/11/5
 * @modified by:
 */
@Entity
@Table(name = "feedback_info")
@Data
@NoArgsConstructor
public class FeedbackInfo implements Serializable {

    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FeedbackInfoGenerator")
    @SequenceGenerator(name = "FeedbackInfoGenerator", sequenceName = "feedbackInfo_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    /**群众姓名**/
    @Column(length = 20,nullable = false)
    private String personName;

    /**群众电话**/
    @Column(length = 20,nullable = false)
    private String phone;

    /**受理部门**/
    @Column(length = 8,nullable = false)
    private Integer organizationId;

    /**受理部门名字**/
    @Column(length = 55,nullable = false)
    private String organizationName;

    /**窗口号**/
    @Column(length = 8)
    private String windowNo;

    /**员工姓名**/
    @Column(length = 55)
    private String employeesName;

    /**员工工号**/
    @Column(length = 20)
    private String employeesNo;

    /**办理事项**/
    @Column(length = 500)
    private String matters;

    /**反馈时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date feedbackTime;

    /**评价时间**/
    @Column(length = 50)
    private String appraiseTime;

    /**发送状态0-未发送，1-已发送**/
    @Column(length = 2)
    private Integer sendState;

    /**截止时间**/
    @Column(length = 50)
    private String deadline;

    /**评价状态0-未评价，1-未评价**/
    @Column(length = 2)
    private Integer appraiseState;

    /**满意度 0-满意，1-不满意**/
    @Column(length = 2)
    private Integer satisfaction;

    /**实现率 1-跑一次， 2-跑多次**/
    @Column(length = 2)
    private Integer completeRate;

    /**投诉建议Id*/
    @Column(length = 10)
    private Integer suggestId;

    /**第一次回复 不满意的详情*/
    @Column(length = 2000)
    private String oneDetail;

    /**第二次回复 不满意详情*/
    @Column(length = 2000)
    private String twoDetail;

    /**窗口评价回复内容*/
    @Column(length = 2000)
    private String appraiseContent;

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

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(length = 6,nullable = false)
    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(length = 80,nullable = false)
    @ApiModelProperty(value="最后更新人name",name="lastUpdateUserName")
    private String lastUpdateUserName;

    @Column(length = 2,nullable = false)
    @ApiModelProperty(value="是否删除",name="amputated")
    private  Integer amputated;

    /**对应取号数据中的resourceId*/
    private Integer resourceId;

}
