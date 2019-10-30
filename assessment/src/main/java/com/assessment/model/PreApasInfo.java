package com.assessment.model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@DynamicUpdate
@Entity
@Table(name = "preApasInfo")
public class PreApasInfo implements Serializable {
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "preApasInfoGenerator")
    @SequenceGenerator(name = "preApasInfoGenerator", sequenceName = "preApasInfo_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;
    //申报号
    @Column(length = 21)
    private  String projId;
    //查询密码,由业务系统随机自动生成的数字：234765
    private  String projPwd;
    @Column(length = 6)
    //是否是在垂管系统中运行的事项
    private  String is_Manubrium;
    //审批事项编号
    @Column(length = 50)
    private String serviceCode;
    //事项终审部门编码
    @Column(length = 50)
    private  String service_DeptId;
    //办理方式
    @Column(length = 2)
    private String bus_Mode;
    //办理方式说明
    @Column(length = 500)
    private String bus_Mode_Desc;
    //权力事项版本号
    @Column(length = 11)
    private  Integer serviceVersion;
    //权力事项名称
    @Column(length = 200)
    private  String serviceName;
    //申报名称
    @Column(length = 200)
    private  String projectName;
    //办件类型: 即办件、承诺件
    @Column(length = 20)
    private  String infoType;
    //业务类型
    @Column(length = 1)
    private  String bus_Type;
    //关联业务标识
    @Column(length = 50)
    private  String rel_Bus_Id;
    //申报者名称：如为个人，则填写姓名；如为法人，则填写单位名称
    @Column(length = 100)
    private String applyName;
    //申报者证件类型: 身份证、组织机构代码证等
    @Column(length = 30)
    private  String apply_CardType;
    //申报者证件号码
    @Column(length = 100)
    private  String apply_CardNumber;
    //联系人/代理人姓名
    @Column(length = 100)
    private  String contactMan;
    //联系人/代理人证件类型
    @Column(length = 100)
    private  String contactMan_CardType;
    //联系人/代理人证件号码
    @Column(length = 100)
    private String contactMan_CardNumber;
    //联系人手机号码
    @Column(length = 13)
    private  String telPhone;
    //邮编
    @Column(length = 6)
    private String postCode;
    //通讯地址
    @Column(length = 100)
    private String  address;
    //法人代表
    @Column(length = 50)
    private String  legalMan;
    //收件部门编码
    @Column(length = 50)
    private  String deptId;
    //收件部门名称
    @Column(length = 50)
    private String deptName;
    //实施机构组织机构代码
    @Column(length = 50)
    private  String ss_OrgCode;
    //创建用户标识
    @Column(length = 50)
    private String  receive_UseId;
    //创建用户名称
    @Column(length = 200)
    private  String receive_Name;
    //申报来源
    @Column(length = 1)
    private  String  applyFrom;
    //审批类型: 绿色通道、联合会审、其他
    @Column(length = 2)
    private  String approve_Type;
    //项目性质: 重点项目、专项项目、其他
    @Column(length = 2)
    private  String  apply_Propertiy;
    //申报、收件时间 时间格式：yyyy-mm-ddhh24:mi:ss
    @Column(length = 19)
    private  String receiveTime;
    //项目关联号(有注册项目的需要填写项目关联号)
    @Column(length = 32)
    private  String  belongto;
    // 收件部门所属行政区划编码
    @Column(length = 20)
    private  String  areacode;
    //数据状态: 0=作废1=有效
    @Column(length = 1)
    private  String dataState;
    //所属系统
    @Column(length = 50)
    private  String belongSystem;
    //备用字段
    @Column(length = 500)
    private  String extend;
    //数据产生时间
    @Column(length = 20)
    private  String create_Time;
    //同步状态。插入：I，更新：U，删除：D，已同步：S
    @Column(length = 1)
    private  String sync_Status;
    //版本号,默认值=1，如果有信息变更，则版本号递增
    @Column(length = 11)
    private  String  dataVersion;
    @Column(length = 30)
    private  String  op;
    private Date tong_Time;
    @Column(length = 20)
    private  Integer tongID;
    @Column(length = 30)
    private  String organizationName;
    @Column(length = 6)
    private Integer organizationId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getProjPwd() {
        return projPwd;
    }

    public void setProjPwd(String projPwd) {
        this.projPwd = projPwd;
    }

    public String getIs_Manubrium() {
        return is_Manubrium;
    }

    public void setIs_Manubrium(String is_Manubrium) {
        this.is_Manubrium = is_Manubrium;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getService_DeptId() {
        return service_DeptId;
    }

    public void setService_DeptId(String service_DeptId) {
        this.service_DeptId = service_DeptId;
    }

    public String getBus_Mode() {
        return bus_Mode;
    }

    public void setBus_Mode(String bus_Mode) {
        this.bus_Mode = bus_Mode;
    }

    public String getBus_Mode_Desc() {
        return bus_Mode_Desc;
    }

    public void setBus_Mode_Desc(String bus_Mode_Desc) {
        this.bus_Mode_Desc = bus_Mode_Desc;
    }

    public Integer getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(Integer serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getBus_Type() {
        return bus_Type;
    }

    public void setBus_Type(String bus_Type) {
        this.bus_Type = bus_Type;
    }

    public String getRel_Bus_Id() {
        return rel_Bus_Id;
    }

    public void setRel_Bus_Id(String rel_Bus_Id) {
        this.rel_Bus_Id = rel_Bus_Id;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getApply_CardType() {
        return apply_CardType;
    }

    public void setApply_CardType(String apply_CardType) {
        this.apply_CardType = apply_CardType;
    }

    public String getApply_CardNumber() {
        return apply_CardNumber;
    }

    public void setApply_CardNumber(String apply_CardNumber) {
        this.apply_CardNumber = apply_CardNumber;
    }

    public String getContactMan() {
        return contactMan;
    }

    public void setContactMan(String contactMan) {
        this.contactMan = contactMan;
    }

    public String getContactMan_CardType() {
        return contactMan_CardType;
    }

    public void setContactMan_CardType(String contactMan_CardType) {
        this.contactMan_CardType = contactMan_CardType;
    }

    public String getContactMan_CardNumber() {
        return contactMan_CardNumber;
    }

    public void setContactMan_CardNumber(String contactMan_CardNumber) {
        this.contactMan_CardNumber = contactMan_CardNumber;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLegalMan() {
        return legalMan;
    }

    public void setLegalMan(String legalMan) {
        this.legalMan = legalMan;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSs_OrgCode() {
        return ss_OrgCode;
    }

    public void setSs_OrgCode(String ss_OrgCode) {
        this.ss_OrgCode = ss_OrgCode;
    }

    public String getReceive_UseId() {
        return receive_UseId;
    }

    public void setReceive_UseId(String receive_UseId) {
        this.receive_UseId = receive_UseId;
    }

    public String getReceive_Name() {
        return receive_Name;
    }

    public void setReceive_Name(String receive_Name) {
        this.receive_Name = receive_Name;
    }

    public String getApplyFrom() {
        return applyFrom;
    }

    public void setApplyFrom(String applyFrom) {
        this.applyFrom = applyFrom;
    }

    public String getApprove_Type() {
        return approve_Type;
    }

    public void setApprove_Type(String approve_Type) {
        this.approve_Type = approve_Type;
    }

    public String getApply_Propertiy() {
        return apply_Propertiy;
    }

    public void setApply_Propertiy(String apply_Propertiy) {
        this.apply_Propertiy = apply_Propertiy;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getBelongto() {
        return belongto;
    }

    public void setBelongto(String belongto) {
        this.belongto = belongto;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getDataState() {
        return dataState;
    }

    public void setDataState(String dataState) {
        this.dataState = dataState;
    }

    public String getBelongSystem() {
        return belongSystem;
    }

    public void setBelongSystem(String belongSystem) {
        this.belongSystem = belongSystem;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getCreate_Time() {
        return create_Time;
    }

    public void setCreate_Time(String create_Time) {
        this.create_Time = create_Time;
    }

    public String getSync_Status() {
        return sync_Status;
    }

    public void setSync_Status(String sync_Status) {
        this.sync_Status = sync_Status;
    }

    public String getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(String dataVersion) {
        this.dataVersion = dataVersion;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Date getTong_Time() {
        return tong_Time;
    }

    public void setTong_Time(Date tong_Time) {
        this.tong_Time = tong_Time;
    }

    public Integer getTongID() {
        return tongID;
    }

    public void setTongID(Integer tongID) {
        this.tongID = tongID;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }
}
