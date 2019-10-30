package com.stamp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Young
 * @description: 组织机构实体类
 * @date: Created in 9:12 2018/8/31
 * @modified by:
 */


@Entity
@Table(name = "organization")
@ApiModel(value = "organization组织", description = "组织")
public class Organization implements Serializable {
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organizationGenerator")
    @SequenceGenerator(name = "organizationGenerator", sequenceName = "organizationNew_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="组织机构id,后台生成无法修改",name="organizationNo")
    private Integer id;

    @Column(length = 6)
    @ApiModelProperty(value="等同的组织id,后台生成无法修改",name="linkedId")
    private Integer linkedId;

    //组织机构编号
    @Length(min=1, max=30, message="组织机构名称长度只能在{min}和{max}之间")
    @Column(nullable = false, length = 30)
    @ApiModelProperty(value="组织机构编号,长度最大30",name="organizationNo")
    private String organizationNo;

    //组织机构编码
    @Column(length = 30)
    @ApiModelProperty(value="组织机构编码,长度最大30",name="organizationCode")
    private String organizationCode;

    //组织机构名称
    @NotBlank
    @Length(min=1, max=105, message="组织机构名称长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 105)
    @ApiModelProperty(value="组织机构名称,长度最大105",name="name",required=true)
    private String name;

    //上级组织机构
    @NotNull
    @Range(min=0,max = 999999,message = "上级组织机构只能为0-999999之间的数字")
    @Column(nullable = false, length = 6)
    @ApiModelProperty(value="上级组织机构,长度最大6,若未选择上级组织机构默认为0",name="parentId",required=true)
    private Integer parentId;

    //机构类型
    @Column(nullable = false, length = 2)
    private Integer type;

    //员工工号首字母
    @Column(nullable = true, length = 2)
    private Integer firstLetter;

    //是否考核
    @NotNull
    @Range(min=0,max = 1,message = "是否考核只能为0或1")
    @Column(nullable = false, length = 1)
    @ApiModelProperty(value="是否考核,长度最大1,状态：0已考核,1未考核  默认0",name="assessmentState",example="0",required=true)
    private Integer assessmentState=0;

    //手机号
    @Column(length = 12)
    @ApiModelProperty(value="手机号,长度最大12",name="phoneNumber")
    private String phoneNumber;

    //办公电话
    @Column(length = 13)
    @ApiModelProperty(value="办公电话,长度最大13",name="officePhone")
    private String officePhone;

    //传真
    @Column(length = 13)
    @ApiModelProperty(value="传真,长度最大13",name="fax")
    private String fax;

    //地址
    @Column(length = 255)
    @ApiModelProperty(value="地址，长度最大255",name="address")
    private String address;

    //组织机构描述
    @Column(length = 255)
    @ApiModelProperty(value="描述,长度最大255",name="description")
    private String description;

    //分管领导
    @Column(length = 8)
    @ApiModelProperty(value="分管领导,长度最大8",name="leadership")
    private Integer leadership;

    //部门管理员
    @Column(length = 8)
    @ApiModelProperty(value="部门管理员,长度最大8",name="departmentalManager")
    private Integer departmentalManager;

    //删除
    @Range(min=0,max = 1,message = "是否删除只能为0或1")
    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="是否删除,长度最大1，默认0",name="amputated",required = true)
    private Integer amputated;

    //组织路径
    @Length(min=1, max=511, message="描述长度只能在{min}和{max}之间")
    @Column(nullable = true,length = 511)
    @ApiModelProperty(value="组织路径,长度最大511，后台处理",name="path")
    private String path;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="创建人ID,长度最大6",name="createdUserId")
    private Integer createdUserId;//

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="创建人name,长度最大55",name="createdUserName")
    private String createdUserName;//

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;//

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;//

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="最后更新人Id,长度最大6",name="lastUpdateUserId")
    private Integer lastUpdateUserId;//

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="最后更新人name,长度最大55",name="lastUpdateUserName")
    private String lastUpdateUserName;//

    @Range(min=0,max = 999,message = "排序只能在{min}和{max}之间")
    @Column(nullable = false,length = 3)
    @ApiModelProperty(value="排序,长度不超过3",name="displayOrder",required=true)
    private Integer displayOrder;

    /**0代表不下发，1-代表需要下发*/
    @Column(length = 2)
    @ApiModelProperty(value="是否下发到取号叫号",name="isSyncQueue")
    private Integer isSyncQueue;

    /**考勤规则配置表的主键id*/
    @Column(length = 8)
    private Integer attendanceRuleConfigId;

    /**是否配置审批流 0-配置  1-不配置*/
    @Column(length = 2)
    private Integer configApprove;

    /**配置审批流规则*/
    private Double approveRule;

    public Integer getConfigApprove() {
        return configApprove;
    }

    public void setConfigApprove(Integer configApprove) {
        this.configApprove = configApprove;
    }

    public Double getApproveRule() {
        return approveRule;
    }

    public void setApproveRule(Double approveRule) {
        this.approveRule = approveRule;
    }

    public Integer getAttendanceRuleConfigId() {
        return attendanceRuleConfigId;
    }

    public void setAttendanceRuleConfigId(Integer attendanceRuleConfigId) {
        this.attendanceRuleConfigId = attendanceRuleConfigId;
    }

    public Integer getIsSyncQueue() {
        return isSyncQueue;
    }

    public void setIsSyncQueue(Integer isSyncQueue) {
        this.isSyncQueue = isSyncQueue;
    }


    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrganizationNo() {
        return organizationNo;
    }

    public void setOrganizationNo(String organizationNo) {
        this.organizationNo = organizationNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAssessmentState() {
        return assessmentState;
    }

    public void setAssessmentState(Integer assessmentState) {
        this.assessmentState = assessmentState;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLeadership() {
        return leadership;
    }

    public void setLeadership(Integer leadership) {
        this.leadership = leadership;
    }

    public Integer getDepartmentalManager() {
        return departmentalManager;
    }

    public void setDepartmentalManager(Integer departmentalManager) {
        this.departmentalManager = departmentalManager;
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public Integer getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(Integer lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public String getLastUpdateUserName() {
        return lastUpdateUserName;
    }

    public void setLastUpdateUserName(String lastUpdateUserName) {
        this.lastUpdateUserName = lastUpdateUserName;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public Integer getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(Integer linkedId) {
        this.linkedId = linkedId;
    }

    public Integer getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(Integer firstLetter) {
        this.firstLetter = firstLetter;
    }

    public Organization() {
    }

    public Organization(Integer id, String organizationNo, String fax, String address, String description, Integer leadership, Integer departmentalManager, Integer amputated, String path, Integer createdUserId, String createdUserName, Date createdDateTime, Date lastUpdateDateTime, Integer lastUpdateUserId, String lastUpdateUserName, Integer displayOrder, Integer firstLetter) {
        this.organizationNo = organizationNo;
        this.name = name;
        this.parentId = parentId;
        this.type = type;
        this.assessmentState = assessmentState;
        this.phoneNumber = phoneNumber;
        this.officePhone = officePhone;
        this.fax = fax;
        this.address = address;
        this.description = description;
        this.leadership = leadership;
        this.departmentalManager = departmentalManager;
        this.amputated = amputated;
        this.path = path;
        this.createdUserId = createdUserId;
        this.createdUserName = createdUserName;
        this.createdDateTime = createdDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.lastUpdateUserId = lastUpdateUserId;
        this.lastUpdateUserName = lastUpdateUserName;
        this.id = id;
        this.displayOrder=displayOrder;
        this.firstLetter = firstLetter;
    }

}
