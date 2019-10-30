package com.stamp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "employees")
@ApiModel(value = "employees员工", description = "员工")
@Data
public class Employees implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeesGenerator")
    @SequenceGenerator(name = "employeesGenerator", sequenceName = "employeesNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 8)
    private Integer id;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="工号，工号后台程序生成且不可修改",name="employeesNo")
    private String employeeNo;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="名字，长度不大于55",name="name",required=true)
    private String name;


    @Column(nullable = false,length = 255)
    @ApiModelProperty(value="头像，长度不大于255",name="icon",required=true)
    private String icon;


    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="性别，（男-0；女-1）",name="sex",example = "0",required=true)
    private Integer sex;

    @Column(length = 25)
    @ApiModelProperty(value="市民卡卡号，长度不大于25",name="citizenCards")
    private String citizenCards;


    @Column(length = 25)
    @ApiModelProperty(value="市民卡银行卡号，长度不大于25",name="bankCardNumber")
    private String bankCardNumber;

    @Column(nullable = false,length = 2)
    private Integer userCompile;

    @Pattern(regexp = "^1\\d{10}$",message = "手机号码格式不合法")
    @Column(nullable = false,length = 12)
    @ApiModelProperty(value="手机号码",name="phoneNumber",required=true)
    private String phoneNumber;

    @Column(length = 13)
    @ApiModelProperty(value="办公电话",name="officePhone")
    private String officePhone;


    @Column(length = 55)
    @ApiModelProperty(value="常用邮箱",name="email")
    private String email;


    @Column(length = 6)
    @ApiModelProperty(value="职务，长度不大于6",name="jobsId")
    private Integer jobsId;


    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="婚姻状态，0-未婚  1-已婚  2-离异 3-丧偶",name="maritalStatus",required=true)
    private Integer maritalStatus;


    @Column(nullable = false,length = 20)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value="出生日期",name="dateBirth",required=true)
    private Date dateBirth;


    @Column(nullable = false,length = 18)
    @ApiModelProperty(value="身份证",name="idCard",required=true)
    private String idCard;

    @ApiModelProperty(value="学历",name="recordOfFormalSchooling",required=true)
    private Integer recordOfFormalSchooling;


    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="账号状态：0-激活 1-未激活",name="activationiId",example = "0",required=true)
    private Integer activationId;



    @Column(nullable = false,length = 20)
    @ApiModelProperty(value="民族，长度不大于20",name="national",required=true)
    private String national;

    @Column(length = 55)
    @ApiModelProperty(value="后台，名称长度不大于55",name="office")
    private String office;

    @Column(length = 255)
    @ApiModelProperty(value="车牌号，长度不大于255",name="plateNo")
    private String plateNo;

    @Column
    @ApiModelProperty(value="入职日期",name="inductionDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date inductionDateTime;

    @Column
    @ApiModelProperty(value="离职日期",name="departureDateTime",dataType = "data")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date departureDateTime;


    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="在职状态，0-待入职 1-在职 3-离职",name="workingState",required=true)
    private Integer workingState;


    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="是否考勤，0-是 1-否",name="attendanceState",required=true)
    private Integer attendanceState;

    @Column(nullable = false,length = 1)
    private Integer windowState;


    @Column(length = 1)
    @ApiModelProperty(value="是否后备干部，0-否 1-是",name="reserveCadresState",example = "0",required=true)
    private Integer reserveCadresState;


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


    @Column(nullable =false,length = 6)
    @ApiModelProperty(value="是否删除,0-未删除 1-已删除 默认为0",name="amputated",example = "0")
    private  Integer amputated;

    @Column(nullable =false,length = 6)
    private Integer organizationId;

    @Column(length = 6)
    private Integer windowId;

    @Column(length = 25)
    private String windowNo;

    private Integer partyBranch;

    @ApiModelProperty(value="入党时间",name="joinPartyDate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date joinPartyDate;

    //市民卡物理地址
    @ApiModelProperty(value="市民卡物理地址",name="citizenCardPhysicalAddress")
    private String citizenCardPhysicalAddress;

    @Transient
    private List<String> plateNoList;
}