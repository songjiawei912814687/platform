package com.assessment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "employees")
@ApiModel(value = "employees员工", description = "员工")
public class Employees implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeesGenerator")
    @SequenceGenerator(name = "employeesGenerator", sequenceName = "employeesNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 8)
    private Integer id;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="工号，工号后台程序生成且不可修改",name="employeesNo")
    private String employeeNo;

    @NotBlank(message = "名字不能为空")
    @Length(min=1, max=55, message="用户名长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="名字，长度不大于55",name="name",required=true)
    private String name;

    @NotBlank(message = "头像路径不能为空")
    @Length(min=1, max=255, message="头像路径长度只能在1-255之间")
    @Column(nullable = false,length = 255)
    @ApiModelProperty(value="头像，长度不大于255",name="icon",required=true)
    private String icon;

    @NotNull
    @Range(min=0,max = 1,message = "性别的只能为0或1")
    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="性别，（男-0；女-1）",name="sex",example = "0",required=true)
    private Integer sex;

    @Length(min=1, max=25, message="市民卡卡号长度只能在1-25之间")
    @Column(length = 25)
    @ApiModelProperty(value="市民卡卡号，长度不大于25",name="citizenCards")
    private String citizenCards;

    @Length(min=1, max=25, message="市民卡银行卡号长度只能在1-25之间")
    @Column(length = 25)
    @ApiModelProperty(value="市民卡银行卡号，长度不大于25",name="bankCardNumber")
    private String bankCardNumber;

    @NotNull
    @Column(nullable = false,length = 2)
    private Integer userCompile;

    @NotNull
    @Pattern(regexp = "^1\\d{10}$",message = "手机号码格式不合法")
    @Column(nullable = false,length = 12)
    @ApiModelProperty(value="手机号码",name="phoneNumber",required=true)
    private String phoneNumber;

    @Column(length = 13)
    @ApiModelProperty(value="办公电话",name="officePhone")
    private String officePhone;

    @Email(message = "请输入正确的邮箱")
    @Column(length = 55)
    @ApiModelProperty(value="常用邮箱",name="email")
    private String email;

    @Column(length = 6)
    @ApiModelProperty(value="职务，长度不大于6",name="jobsId")
    private Integer jobsId;

    @NotNull
    @Range(min=0,max = 3,message = "婚姻状态:0-未婚  1-已婚  2-离异 3-丧偶")
    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="婚姻状态，0-未婚  1-已婚  2-离异 3-丧偶",name="maritalStatus",required=true)
    private Integer maritalStatus;

    @NotNull
    @Column(nullable = false,length = 20)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value="出生日期",name="dateBirth",required=true)
    private Date dateBirth;

    @Length(min=1, max=25, message="身份证长度只能在1-18之间")
    @Column(nullable = false,length = 18)
    @ApiModelProperty(value="身份证",name="idCard",required=true)
    private String idCard;

    @ApiModelProperty(value="学历",name="recordOfFormalSchooling",required=true)
    private Integer recordOfFormalSchooling;

    @Range(min=0,max = 1,message = "账号状态值只能为0或1")
    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="账号状态：0-激活 1-未激活",name="activationiId",example = "0",required=true)
    private Integer activationId;

    @NotNull
    @Length(min = 1,max = 20,message = "民族名称长度介于1到20之间")
    @Column(nullable = false,length = 20)
    @ApiModelProperty(value="民族，长度不大于20",name="national",required=true)
    private String national;

    @Column(length = 55)
    @ApiModelProperty(value="后台，名称长度不大于55",name="office")
    private String office;

    @Column(length = 25)
    @ApiModelProperty(value="车牌号，长度不大于25",name="plateNo")
    private String plateNo;

    @Column
    @ApiModelProperty(value="入职日期",name="inductionDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date inductionDateTime;

    @Column
    @ApiModelProperty(value="离职日期",name="departureDateTime",dataType = "data")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date departureDateTime;

    @Range(min=0,max = 1,message = "在职状态值只能为0或1")
    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="在职状态，0-待入职 1-在职 3-离职",name="workingState",required=true)
    private Integer workingState;

    @Range(min=0,max = 1,message = "是否考勤只能为0或1")
    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="是否考勤，0-是 1-否",name="attendanceState",required=true)
    private Integer attendanceState;

    @Column(nullable = false,length = 1)
    private Integer windowState;

    @NotNull
    @Range(min=0,max = 1,message = "是否后备干部只能为0或1")
    @Column(length = 1)
    @ApiModelProperty(value="是否后备干部，0-否 1-是",name="reserveCadresState",example = "0",required=true)
    private Integer reserveCadresState;

    @NotNull
    @Column(nullable = false,length = 1)
    private Integer partyMemberState;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="创建人ID",name="createdUserId")
    private Integer createdUserId;//

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="创建人name",name="createdUserName")
    private String createdUserName;//

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    private Date createdDateTime;//

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    private Date lastUpdateDateTime;//

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;//

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="最后更新人name",name="lastUpdateUserName")
    private String lastUpdateUserName;//

    @Range(min=0,max = 1,message = "是否删除只能为0或1")
    @Column(nullable =false,length = 6)
    @ApiModelProperty(value="是否删除,0-未删除 1-已删除 默认为0",name="amputated",example = "0")
    private  Integer amputated;

    @Column(nullable =false,length = 6)
    private Integer organizationId;

    @Column(length = 6)
    private Integer windowId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCitizenCards() {
        return citizenCards;
    }

    public void setCitizenCards(String citizenCards) {
        this.citizenCards = citizenCards;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getUserCompile() {
        return userCompile;
    }

    public void setUserCompile(Integer userCompile) {
        this.userCompile = userCompile;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getJobsId() {
        return jobsId;
    }

    public void setJobsId(Integer jobsId) {
        this.jobsId = jobsId;
    }

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getReserveCadresState() {
        return reserveCadresState;
    }

    public void setReserveCadresState(Integer reserveCadresState) {
        this.reserveCadresState = reserveCadresState;
    }

    public Integer getActivationId() {
        return activationId;
    }

    public void setActivationId(Integer activationId) {
        this.activationId = activationId;
    }

    public Integer getWorkingState() {
        return workingState;
    }

    public void setWorkingState(Integer workingState) {
        this.workingState = workingState;
    }

    public Integer getAttendanceState() {
        return attendanceState;
    }

    public void setAttendanceState(Integer attendanceState) {
        this.attendanceState = attendanceState;
    }

    public Integer getPartyMemberState() {
        return partyMemberState;
    }

    public void setPartyMemberState(Integer partyMemberState) {
        this.partyMemberState = partyMemberState;
    }

    public Integer getWindowState() {
        return windowState;
    }

    public void setWindowState(Integer windowState) {
        this.windowState = windowState;
    }

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public Integer getWindowId() {
        return windowId;
    }

    public void setWindowId(Integer windowId) {
        this.windowId = windowId;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public Date getInductionDateTime() {
        return inductionDateTime;
    }

    public void setInductionDateTime(Date inductionDateTime) {
        this.inductionDateTime = inductionDateTime;
    }

    public Date getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(Date departureDateTime) {
        this.departureDateTime = departureDateTime;
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

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }

    public Integer getRecordOfFormalSchooling() {
        return recordOfFormalSchooling;
    }

    public void setRecordOfFormalSchooling(Integer recordOfFormalSchooling) {
        this.recordOfFormalSchooling = recordOfFormalSchooling;
    }

    public Employees() {
    }
}
