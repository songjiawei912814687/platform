package com.personnel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-05  21:05
 * @modified by:
 */
@Entity
public class FoodEmployees implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "foodEmpGenerator")
    @SequenceGenerator(name = "foodEmpGenerator", sequenceName = "foodEmpNew_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    private String sID;

    //卡号
    private String sCardID;

    //个人编号
    private String sPerCode;

    //校区编号
    private BigDecimal nAreaNum;

    //卡户姓名
    private String sAccName;

    //第三方部门编号
    private String sDepCode;

    //身份编号
    private BigDecimal nClsNum;

    //姓名
    private String sAccSex;

    //手机号
    private String sMobileCode;

    //邮箱
    private String sEMail;

    //邮政编码
    private String sPostCode;

    //出生日期
    private String sBirthDay;

    //证件编号
    private String sCertCode;

    //常用地址
    private String sAddress;

    //照片
    private String sAccPhoto;

    //下发状态0-未下发，1-下发,4-删除
    private Integer state;

    @Range(min=0,max = 1,message = "是否删除只能为0或1")
    @Column(nullable =false,length = 6)
    @ApiModelProperty(value="是否删除,0-未删除 1-已删除 默认为0",name="amputated",example = "0")
    private  Integer amputated;

    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;//

    @ApiModelProperty(value="下发成功时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    public FoodEmployees() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsCardID() {
        return sCardID;
    }

    public void setsCardID(String sCardID) {
        this.sCardID = sCardID;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }


    public String getsPerCode() {
        return sPerCode;
    }

    public void setsPerCode(String sPerCode) {
        this.sPerCode = sPerCode;
    }

    public BigDecimal getnAreaNum() {
        return nAreaNum;
    }

    public void setnAreaNum(BigDecimal nAreaNum) {
        this.nAreaNum = nAreaNum;
    }

    public String getsAccName() {
        return sAccName;
    }

    public void setsAccName(String sAccName) {
        this.sAccName = sAccName;
    }

    public String getsDepCode() {
        return sDepCode;
    }

    public void setsDepCode(String sDepCode) {
        this.sDepCode = sDepCode;
    }

    public BigDecimal getnClsNum() {
        return nClsNum;
    }

    public void setnClsNum(BigDecimal nClsNum) {
        this.nClsNum = nClsNum;
    }

    public String getsAccSex() {
        return sAccSex;
    }

    public void setsAccSex(String sAccSex) {
        this.sAccSex = sAccSex;
    }

    public String getsMobileCode() {
        return sMobileCode;
    }

    public void setsMobileCode(String sMobileCode) {
        this.sMobileCode = sMobileCode;
    }

    public String getsEMail() {
        return sEMail;
    }

    public void setsEMail(String sEMail) {
        this.sEMail = sEMail;
    }

    public String getsPostCode() {
        return sPostCode;
    }

    public void setsPostCode(String sPostCode) {
        this.sPostCode = sPostCode;
    }

    public String getsBirthDay() {
        return sBirthDay;
    }

    public void setsBirthDay(String sBirthDay) {
        this.sBirthDay = sBirthDay;
    }

    public String getsCertCode() {
        return sCertCode;
    }

    public void setsCertCode(String sCertCode) {
        this.sCertCode = sCertCode;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getsAccPhoto() {
        return sAccPhoto;
    }

    public void setsAccPhoto(String sAccPhoto) {
        this.sAccPhoto = sAccPhoto;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    @Override
    public String toString() {
        return "{" +
                "sID:'" + sID + '\'' +
                ", sCardID:'" + sCardID + '\'' +
                ", sPerCode:'" + sPerCode + '\'' +
                ", nAreaNum:" + nAreaNum +
                ", sAccName:'" + sAccName + '\'' +
                ", sDepCode:'" + sDepCode + '\'' +
                ", nClsNum:" + nClsNum +
                ", sAccSex:'" + sAccSex + '\'' +
                ", sMobileCode:'" + sMobileCode + '\'' +
                ", sEMail:'" + "" + '\'' +
                ", sPostCode:'" + sPostCode + '\'' +
                ", sBirthDay:'" + sBirthDay + '\'' +
                ", sCertCode:'" + sCertCode + '\'' +
                ", sAddress:'" + "" + '\'' +
                ", sAccPhoto:'" + "" + '\'' +
                '}';
    }

    public String toStringId() {
        return "{" +
                "SData:[{"+
                "sID:'" + sID + '\'' +
                ", state:'" + state + '\'' +
                '}'+"]}";
    }
}
