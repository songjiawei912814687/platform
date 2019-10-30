package com.feedback.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "suggestions")
@ApiModel(value = "Suggestions投诉建议", description = "投诉建议")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suggestions implements Serializable {
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suggestionsGenerator")
    @SequenceGenerator(name = "suggestionsGenerator", sequenceName = "suggestions_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="投诉建议id,后台生成无法修改",name="id")
    private Integer id;

    @Column(length = 10)
    @ApiModelProperty(value="反馈信息id",name="feedbackId")
    private Integer feedbackId;

    /**父id*/
    @Column(length = 10)
    private Integer parentId;

    /**投诉内容*/
    @Column(length = 2000)
    private String content;

    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="来源",name="resource",required=true)
    private Integer dateResource;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="投诉人name,长度最大55",name="suggestionName")
    private String suggestionName;

    @Column(nullable = false,length = 12)
    @ApiModelProperty(value="投诉人手机号,长度最大12",name="suggestionPhoneNumber")
    private String suggestionPhoneNumber;

    @Column(nullable = true,length = 105)
    @ApiModelProperty(value="上级组织机构名称,长度最大105",name="upperOrganizationName")
    private String upperOrganizationName;

    @Column(length = 6)
    @ApiModelProperty(value="上级组织机构id,长度最大6",name="upperOrganizationId")
    private Integer upperOrganizationId;

    @Column(nullable = false,length = 105)
    @ApiModelProperty(value="组织机构名称,长度最大105",name="organizationName",required=true)
    private String organizationName;

    @Column(length = 6)
    @ApiModelProperty(value="组织机构id,长度最大6",name="organizationId")
    private Integer organizationId;

    @Column(length = 105)
    @ApiModelProperty(value="窗口号,长度最大105",name="windowName")
    private String windowName;

    @Column(length = 6)
    @ApiModelProperty(value="窗口id,长度最大6",name="windowId")
    private Integer windowId;


    @Column(length = 105)
    @ApiModelProperty(value="人员名称,长度最大105",name="employeeName",required=true)
    private String employeeName;


    @Column(length = 6)
    @ApiModelProperty(value="人员id,长度最大6",name="employeeId")
    private Integer employeeId;


    @Column(length = 55)
    @ApiModelProperty(value="人员工号,长度最大55",name="employeeNo")
    private String employeeNo;

    @Column(nullable = false)
    @ApiModelProperty(value="投诉时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Range(min=0, max=99, message="发布状态只能在{min}和{max}之间")
    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="发布,0表示待发布，1表示已发布，默认为待发布",name="publish")
    private Integer publish;

    @ApiModelProperty(value="发布时间",name="outDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishDateTime;

    @ApiModelProperty(value="逾期时间",name="outDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date outDateTime;

    private String outTimes;//处理时长

    @Column(length = 2)
    @ApiModelProperty(value="逾期状态,0表示未逾期，1表示已逾期，默认为未逾期",name="outOfDate")
    private Integer outOfDate;

    @Column(nullable = true,length = 6)
    @ApiModelProperty(value="指定回复人ID,长度最大6",name="replyUserId")
    private Integer replyUserId;

    @Column(length = 255)
    @ApiModelProperty(value="处理结果,长度最大255",name="dealResult")
    private String dealResult;

    @ApiModelProperty(value="处理时间",name="dealTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dealTime;

    @Column(nullable = false, length = 2)
    @ApiModelProperty(value="处理状态,0表示未处理，1表示已处理，默认为未处理",name="dealState",required=true)
    private Integer dealState;

    /**添加投诉内容状态0-未添加。。。1-已经添加*/
    @Column(length = 10)
    private Integer replyType;


    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="是否删除,长度最大1，默认0",name="amputated",required = true)
    private Integer amputated;

    @Column(length = 6)
    @ApiModelProperty(value="创建人ID,长度最大6",name="createdUserId")
    private Integer createdUserId;

    @Column(length = 55)
    @ApiModelProperty(value="创建人name,长度最大55",name="createdUserName")
    private String createdUserName;


    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(length = 6)
    @ApiModelProperty(value="最后更新人Id,长度最大6",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(length = 55)
    @ApiModelProperty(value="最后更新人name,长度最大55",name="lastUpdateUserName")
    private String lastUpdateUserName;

    @Transient
    private List<Attachment> attachmentList;


    /**是否电话回访0代表未回访，1代表回访*/
    @Column(length = 2)
    private Integer isPhoneCall=0;

    /**是否有效 0.无效 1.有效*/
    @Column(length = 2)
    private Integer isUse=0;


}
