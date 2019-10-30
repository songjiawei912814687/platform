package com.selfservice.domain.output;

import com.common.Enum.ItemSourceNameEnum;
import com.common.Enum.XINGZHENXDRXZEnum;

public class HandlingGuide {
    private Integer id;

    private String qlsxId;//权力事项id

    private String qlInnerCode;

    private String name ;//事项名称

    private String contentInvolve; //适用范围

    private String suitable;//适用对象

    private String suitableName;//适用名称

    private  String lawBasis;//审批依据

    private String acpInstitution;//受理机构

    private String onlineApplication; //在线申请描述

    private String windowsApplication; //窗口申请描述

    private String onTime;  //办公时间  受理地点类型为行政服务中心或部门服务窗口的办公地点和办公时间段的信息

    private String linkTel;//咨询时间

    private String limitInfo;//办理时限

    private String anticipateDay;//法定期限

    private String anticipateType;//法定期限单位

    private String bjType;//办件类型

    private Integer ptId;

    public void setSuitableName(String suitableName) {
        this.suitableName = suitableName;
    }

    public Integer getPtId() {
        return ptId;
    }

    public void setPtId(Integer ptId) {
        this.ptId = ptId;
    }

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

    public String getContentInvolve() {
        return contentInvolve;
    }

    public void setContentInvolve(String contentInvolve) {
        this.contentInvolve = contentInvolve;
    }

    public String getSuitable() {
        return suitable;
    }

    public void setSuitable(String suitable) {
        this.suitable = suitable;
    }

        public String getSuitableName() {
            StringBuilder stringBuilder = new StringBuilder("");
            if(getSuitable()!=null){
                if(getSuitable().trim().indexOf("1;")>=0){
                    stringBuilder = stringBuilder.append(XINGZHENXDRXZEnum.PERSONAL.getMsg()+",");
                }else if(getSuitable().trim().indexOf("2;")>=0){
                    stringBuilder = stringBuilder.append(XINGZHENXDRXZEnum.LEGALPERSON.getMsg()+",");
                }else if(getSuitable().trim().indexOf("3;")>=0){
                    stringBuilder = stringBuilder.append(XINGZHENXDRXZEnum.OTHERORGS.getMsg()+",");
                }else if(getSuitable().trim().indexOf("4;")>=0){
                    stringBuilder = stringBuilder.append(XINGZHENXDRXZEnum.OTHERS.getMsg()+",");
                }
            }
            if(stringBuilder.toString().equals("")){
                return stringBuilder.toString();
            }
            return stringBuilder.substring(0,stringBuilder.length()-1);
        }

    public String getQlInnerCode() {
        return qlInnerCode;
    }

    public void setQlInnerCode(String qlInnerCode) {
        this.qlInnerCode = qlInnerCode;
    }

    public String getLawBasis() {
        return lawBasis;
    }

    public void setLawBasis(String lawBasis) {
        this.lawBasis = lawBasis;
    }

    public String getAcpInstitution() {
        return acpInstitution;
    }

    public void setAcpInstitution(String acpInstitution) {
        this.acpInstitution = acpInstitution;
    }

    public String getOnlineApplication() {
        return onlineApplication;
    }

    public void setOnlineApplication(String onlineApplication) {
        this.onlineApplication = onlineApplication;
    }

    public String getWindowsApplication() {
        return windowsApplication;
    }

    public void setWindowsApplication(String windowsApplication) {
        this.windowsApplication = windowsApplication;
    }

    public String getOnTime() {
        return onTime;
    }

    public void setOnTime(String onTime) {
        this.onTime = onTime;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getLimitInfo() {
        return limitInfo;
    }

    public void setLimitInfo(String limitInfo) {
        this.limitInfo = limitInfo;
    }

    public String getAnticipateDay() {
        return anticipateDay;
    }

    public void setAnticipateDay(String anticipateDay) {
        this.anticipateDay = anticipateDay;
    }

    public String getAnticipateType() {
        return anticipateType;
    }

    public void setAnticipateType(String anticipateType) {
        this.anticipateType = anticipateType;
    }

    public String getQlsxId() {
        return qlsxId;
    }

    public void setQlsxId(String qlsxId) {
        this.qlsxId = qlsxId;
    }

    public String getBjType() {
        return bjType;
    }

    public void setBjType(String bjType) {
        this.bjType = bjType;
    }

    private String materialInfo;

    public String getMaterialInfo() {
        return materialInfo;
    }

    public void setMaterialInfo(String materialInfo) {
        this.materialInfo = materialInfo;
    }

    private String promiseDay;

    public String getPromiseDay() {
        return promiseDay;
    }

    public void setPromiseDay(String promiseDay) {
        this.promiseDay = promiseDay;
    }
}
