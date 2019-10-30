package com.api.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Young
 * @description: 权力事项
 * @date: Created in 18:43 2018/10/23
 * @modified by:
 */
@Entity
@Table(name = "QLT_QLSX")
public class QltQlsx implements Serializable {

    @Id
    private String qlFullId;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="acceptAddressInfo", columnDefinition="CLOB")
    private String acceptAddressInfo;

    private Integer anticipateDay;

    /**行业主题**/
    @Column(length = 2)
    private String hangYeClassType;

    private Integer applyerminCount;

    private String applyCondition;

    private String acpInstitution;

    private String applicableObject;

    private String applyType;

    private String applyTypeTel;

    private String banjianFinishfiles;

    private String bjtype;

    private String banRequirement;

    private String chargeBasis;

    private String chargeFlag;

    private String contentInvolve;

    private String countLimit;

    private String decInstitution;

    private String imFlowUrl;

    private String itemsource;

    private String lawbasis;

    private String leadDept;

    private String linkTel;

    private String materialInfo;

    private String outFlowDesc;

    private Integer promiseDay;

    private String qaInfo;

    private String qlKind;

    private String qlName;

    private String related;

    private String shixiangsctype;

    private String superviseTel;

    private String serviceDay;

    private String serviceMode;

    private String xingzhenxdrxy;

    private Integer hot=0;

    private Integer particles=0;

    private BigDecimal tongid;

    private Date updateDate;

    public String getHangYeClassType() {
        return hangYeClassType;
    }

    public void setHangYeClassType(String hangYeClassType) {
        this.hangYeClassType = hangYeClassType;
    }

    public String getAcceptAddressInfo() {
        return acceptAddressInfo;
    }

    public void setAcceptAddressInfo(String acceptAddressInfo) {
        this.acceptAddressInfo = acceptAddressInfo == null ? null : acceptAddressInfo.trim();
    }

    public Integer getAnticipateDay() {
        return anticipateDay;
    }

    public void setAnticipateDay(Integer anticipateDay) {
        this.anticipateDay = anticipateDay;
    }

    public Integer getApplyerminCount() {
        return applyerminCount;
    }

    public void setApplyerminCount(Integer applyerminCount) {
        this.applyerminCount = applyerminCount;
    }

    public String getApplyCondition() {
        return applyCondition;
    }

    public void setApplyCondition(String applyCondition) {
        this.applyCondition = applyCondition == null ? null : applyCondition.trim();
    }

    public String getAcpInstitution() {
        return acpInstitution;
    }

    public void setAcpInstitution(String acpInstitution) {
        this.acpInstitution = acpInstitution == null ? null : acpInstitution.trim();
    }

    public String getApplicableObject() {
        return applicableObject;
    }

    public void setApplicableObject(String applicableObject) {
        this.applicableObject = applicableObject == null ? null : applicableObject.trim();
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType == null ? null : applyType.trim();
    }

    public String getApplyTypeTel() {
        return applyTypeTel;
    }

    public void setApplyTypeTel(String applyTypeTel) {
        this.applyTypeTel = applyTypeTel == null ? null : applyTypeTel.trim();
    }

    public String getBanjianFinishfiles() {
        return banjianFinishfiles;
    }

    public void setBanjianFinishfiles(String banjianFinishfiles) {
        this.banjianFinishfiles = banjianFinishfiles == null ? null : banjianFinishfiles.trim();
    }

    public String getBjtype() {
        return bjtype;
    }

    public void setBjtype(String bjtype) {
        this.bjtype = bjtype == null ? null : bjtype.trim();
    }

    public String getBanRequirement() {
        return banRequirement;
    }

    public void setBanRequirement(String banRequirement) {
        this.banRequirement = banRequirement == null ? null : banRequirement.trim();
    }

    public String getChargeBasis() {
        return chargeBasis;
    }

    public void setChargeBasis(String chargeBasis) {
        this.chargeBasis = chargeBasis == null ? null : chargeBasis.trim();
    }

    public String getChargeFlag() {
        return chargeFlag;
    }

    public void setChargeFlag(String chargeFlag) {
        this.chargeFlag = chargeFlag == null ? null : chargeFlag.trim();
    }

    public String getContentInvolve() {
        return contentInvolve;
    }

    public void setContentInvolve(String contentInvolve) {
        this.contentInvolve = contentInvolve == null ? null : contentInvolve.trim();
    }

    public String getCountLimit() {
        return countLimit;
    }

    public void setCountLimit(String countLimit) {
        this.countLimit = countLimit == null ? null : countLimit.trim();
    }

    public String getDecInstitution() {
        return decInstitution;
    }

    public void setDecInstitution(String decInstitution) {
        this.decInstitution = decInstitution == null ? null : decInstitution.trim();
    }

    public String getImFlowUrl() {
        return imFlowUrl;
    }

    public void setImFlowUrl(String imFlowUrl) {
        this.imFlowUrl = imFlowUrl == null ? null : imFlowUrl.trim();
    }

    public String getItemsource() {
        return itemsource;
    }

    public void setItemsource(String itemsource) {
        this.itemsource = itemsource == null ? null : itemsource.trim();
    }

    public String getLawbasis() {
        return lawbasis;
    }

    public void setLawbasis(String lawbasis) {
        this.lawbasis = lawbasis == null ? null : lawbasis.trim();
    }

    public String getLeadDept() {
        return leadDept;
    }

    public void setLeadDept(String leadDept) {
        this.leadDept = leadDept == null ? null : leadDept.trim();
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel == null ? null : linkTel.trim();
    }

    public String getMaterialInfo() {
        return materialInfo;
    }

    public void setMaterialInfo(String materialInfo) {
        this.materialInfo = materialInfo == null ? null : materialInfo.trim();
    }

    public String getOutFlowDesc() {
        return outFlowDesc;
    }

    public void setOutFlowDesc(String outFlowDesc) {
        this.outFlowDesc = outFlowDesc == null ? null : outFlowDesc.trim();
    }

    public Integer getPromiseDay() {
        return promiseDay;
    }

    public void setPromiseDay(Integer promiseDay) {
        this.promiseDay = promiseDay;
    }

    public String getQaInfo() {
        return qaInfo;
    }

    public void setQaInfo(String qaInfo) {
        this.qaInfo = qaInfo == null ? null : qaInfo.trim();
    }

    public String getQlFullId() {
        return qlFullId;
    }

    public void setQlFullId(String qlFullId) {
        this.qlFullId = qlFullId == null ? null : qlFullId.trim();
    }

    public String getQlKind() {
        return qlKind;
    }

    public void setQlKind(String qlKind) {
        this.qlKind = qlKind == null ? null : qlKind.trim();
    }

    public String getQlName() {
        return qlName;
    }

    public void setQlName(String qlName) {
        this.qlName = qlName == null ? null : qlName.trim();
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related == null ? null : related.trim();
    }

    public String getShixiangsctype() {
        return shixiangsctype;
    }

    public void setShixiangsctype(String shixiangsctype) {
        this.shixiangsctype = shixiangsctype == null ? null : shixiangsctype.trim();
    }

    public String getSuperviseTel() {
        return superviseTel;
    }

    public void setSuperviseTel(String superviseTel) {
        this.superviseTel = superviseTel == null ? null : superviseTel.trim();
    }

    public String getServiceDay() {
        return serviceDay;
    }

    public void setServiceDay(String serviceDay) {
        this.serviceDay = serviceDay == null ? null : serviceDay.trim();
    }

    public String getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(String serviceMode) {
        this.serviceMode = serviceMode == null ? null : serviceMode.trim();
    }

    public String getXingzhenxdrxy() {
        return xingzhenxdrxy;
    }

    public void setXingzhenxdrxy(String xingzhenxdrxy) {
        this.xingzhenxdrxy = xingzhenxdrxy == null ? null : xingzhenxdrxy.trim();
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Integer getParticles() {
        return particles;
    }

    public void setParticles(Integer particles) {
        this.particles = particles;
    }

    public BigDecimal getTongid() {
        return tongid;
    }

    public void setTongid(BigDecimal tongid) {
        this.tongid = tongid;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
