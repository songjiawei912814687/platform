package com.feedback.model;
import javax.persistence.*;
import java.io.Serializable;
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

    /**权力编码**/
    @Id
    @Column(length = 50)
    private String QL_Full_ID;


    /**写入同步时间*/
    private Date UPDATE_DATE;

    /**办件类型**/
    @Column(length = 2)
    private String BJTYPE;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="Content_involve", columnDefinition="CLOB")
    private String Content_involve;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="Applicable_object", columnDefinition="CLOB")
    private String Applicable_object;

    @Column(length = 2)
    private String QL_KIND;

    @Column(length = 2)
    private String ITEMSOURCE;

    @Column(length = 100)
    private String Acp_institution;

    @Column(length = 100)
    private String Dec_institution;

    @Column(length = 50)
    private String LEAD_DEPT;

    @Column(length = 5)
    private String SHIXIANGSCtype;

    @Column(length = 20)
    private String Apply_type;

    @Column(length = 100)
    private String Apply_type_tel;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="ACCEPT_ADDRESS_INFO", columnDefinition="CLOB")
    private String ACCEPT_ADDRESS_INFO;

    @Column(length = 80)
    private String LINK_TEL;

    @Column(length = 80)
    private String SUPERVISE_TEL;

    @Column(length = 100)
    private String BANJIAN_FINISHFILES;

    @Column(length = 11)
    private Integer PROMISE_DAY;

    @Column(length = 11)
    private Integer ANTICIPATE_DAY;

    @Column(length = 30)
    private String Service_mode;

    @Column(length = 10)
    private String Service_day;

    @Column(length = 11)
    private Integer APPLYERMIN_COUNT;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="APPLY_CONDITION", columnDefinition="CLOB")
    private String APPLY_CONDITION;

    @Column(length = 2)
    private String Count_limit;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="Ban_requirement", columnDefinition="CLOB")
    private String Ban_requirement;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="RELATED", columnDefinition="CLOB")
    private String RELATED;

    @Column(length = 2000)
    private String OUT_FLOW_DESC;

    @Column(length = 255)
    private String IM_FLOW_url;
    @Column(length = 1)
    private String CHARGE_FLAG;
    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="CHARGE_BASIS", columnDefinition="CLOB")
    private String CHARGE_BASIS;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="LAWBASIS", columnDefinition="CLOB")
    private String LAWBASIS;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="XINGZHENXDRXY", columnDefinition="CLOB")
    private String XINGZHENXDRXY;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="QA_INFO", columnDefinition="CLOB")
    private String QA_INFO;


    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="MATERIAL_INFO", columnDefinition="CLOB")
    private String MATERIAL_INFO;

    public QltQlsx() {
    }

    public Date getUPDATE_DATE() {
        return UPDATE_DATE;
    }

    public void setUPDATE_DATE(Date UPDATE_DATE) {
        this.UPDATE_DATE = UPDATE_DATE;
    }

    public String getQL_Full_ID() {
        return QL_Full_ID;
    }

    public void setQL_Full_ID(String QL_Full_ID) {
        this.QL_Full_ID = QL_Full_ID;
    }

    public String getBJTYPE() {
        return BJTYPE;
    }

    public void setBJTYPE(String BJTYPE) {
        this.BJTYPE = BJTYPE;
    }

    public String getContent_involve() {
        return Content_involve;
    }

    public void setContent_involve(String content_involve) {
        Content_involve = content_involve;
    }

    public String getApplicable_object() {
        return Applicable_object;
    }

    public void setApplicable_object(String applicable_object) {
        Applicable_object = applicable_object;
    }

    public String getQL_KIND() {
        return QL_KIND;
    }

    public void setQL_KIND(String QL_KIND) {
        this.QL_KIND = QL_KIND;
    }

    public String getITEMSOURCE() {
        return ITEMSOURCE;
    }

    public void setITEMSOURCE(String ITEMSOURCE) {
        this.ITEMSOURCE = ITEMSOURCE;
    }

    public String getAcp_institution() {
        return Acp_institution;
    }

    public void setAcp_institution(String acp_institution) {
        Acp_institution = acp_institution;
    }

    public String getDec_institution() {
        return Dec_institution;
    }

    public void setDec_institution(String dec_institution) {
        Dec_institution = dec_institution;
    }

    public String getLEAD_DEPT() {
        return LEAD_DEPT;
    }

    public void setLEAD_DEPT(String LEAD_DEPT) {
        this.LEAD_DEPT = LEAD_DEPT;
    }

    public String getSHIXIANGSCtype() {
        return SHIXIANGSCtype;
    }

    public void setSHIXIANGSCtype(String SHIXIANGSCtype) {
        this.SHIXIANGSCtype = SHIXIANGSCtype;
    }

    public String getApply_type() {
        return Apply_type;
    }

    public void setApply_type(String apply_type) {
        Apply_type = apply_type;
    }

    public String getApply_type_tel() {
        return Apply_type_tel;
    }

    public void setApply_type_tel(String apply_type_tel) {
        Apply_type_tel = apply_type_tel;
    }

    public String getACCEPT_ADDRESS_INFO() {
        return ACCEPT_ADDRESS_INFO;
    }

    public void setACCEPT_ADDRESS_INFO(String ACCEPT_ADDRESS_INFO) {
        this.ACCEPT_ADDRESS_INFO = ACCEPT_ADDRESS_INFO;
    }

    public String getLINK_TEL() {
        return LINK_TEL;
    }

    public void setLINK_TEL(String LINK_TEL) {
        this.LINK_TEL = LINK_TEL;
    }

    public String getSUPERVISE_TEL() {
        return SUPERVISE_TEL;
    }

    public void setSUPERVISE_TEL(String SUPERVISE_TEL) {
        this.SUPERVISE_TEL = SUPERVISE_TEL;
    }

    public String getBANJIAN_FINISHFILES() {
        return BANJIAN_FINISHFILES;
    }

    public void setBANJIAN_FINISHFILES(String BANJIAN_FINISHFILES) {
        this.BANJIAN_FINISHFILES = BANJIAN_FINISHFILES;
    }

    public Integer getPROMISE_DAY() {
        return PROMISE_DAY;
    }

    public void setPROMISE_DAY(Integer PROMISE_DAY) {
        this.PROMISE_DAY = PROMISE_DAY;
    }

    public Integer getANTICIPATE_DAY() {
        return ANTICIPATE_DAY;
    }

    public void setANTICIPATE_DAY(Integer ANTICIPATE_DAY) {
        this.ANTICIPATE_DAY = ANTICIPATE_DAY;
    }

    public String getService_mode() {
        return Service_mode;
    }

    public void setService_mode(String service_mode) {
        Service_mode = service_mode;
    }

    public String getService_day() {
        return Service_day;
    }

    public void setService_day(String service_day) {
        Service_day = service_day;
    }

    public Integer getAPPLYERMIN_COUNT() {
        return APPLYERMIN_COUNT;
    }

    public void setAPPLYERMIN_COUNT(Integer APPLYERMIN_COUNT) {
        this.APPLYERMIN_COUNT = APPLYERMIN_COUNT;
    }

    public String getAPPLY_CONDITION() {
        return APPLY_CONDITION;
    }

    public void setAPPLY_CONDITION(String APPLY_CONDITION) {
        this.APPLY_CONDITION = APPLY_CONDITION;
    }

    public String getCount_limit() {
        return Count_limit;
    }

    public void setCount_limit(String count_limit) {
        Count_limit = count_limit;
    }

    public String getBan_requirement() {
        return Ban_requirement;
    }

    public void setBan_requirement(String ban_requirement) {
        Ban_requirement = ban_requirement;
    }

    public String getRELATED() {
        return RELATED;
    }

    public void setRELATED(String RELATED) {
        this.RELATED = RELATED;
    }

    public String getOUT_FLOW_DESC() {
        return OUT_FLOW_DESC;
    }

    public void setOUT_FLOW_DESC(String OUT_FLOW_DESC) {
        this.OUT_FLOW_DESC = OUT_FLOW_DESC;
    }

    public String getIM_FLOW_url() {
        return IM_FLOW_url;
    }

    public void setIM_FLOW_url(String IM_FLOW_url) {
        this.IM_FLOW_url = IM_FLOW_url;
    }

    public String getCHARGE_FLAG() {
        return CHARGE_FLAG;
    }

    public void setCHARGE_FLAG(String CHARGE_FLAG) {
        this.CHARGE_FLAG = CHARGE_FLAG;
    }

    public String getCHARGE_BASIS() {
        return CHARGE_BASIS;
    }

    public void setCHARGE_BASIS(String CHARGE_BASIS) {
        this.CHARGE_BASIS = CHARGE_BASIS;
    }

    public String getLAWBASIS() {
        return LAWBASIS;
    }

    public void setLAWBASIS(String LAWBASIS) {
        this.LAWBASIS = LAWBASIS;
    }

    public String getXINGZHENXDRXY() {
        return XINGZHENXDRXY;
    }

    public void setXINGZHENXDRXY(String XINGZHENXDRXY) {
        this.XINGZHENXDRXY = XINGZHENXDRXY;
    }

    public String getQA_INFO() {
        return QA_INFO;
    }

    public void setQA_INFO(String QA_INFO) {
        this.QA_INFO = QA_INFO;
    }

    public String getMATERIAL_INFO() {
        return MATERIAL_INFO;
    }

    public void setMATERIAL_INFO(String MATERIAL_INFO) {
        this.MATERIAL_INFO = MATERIAL_INFO;
    }
}
