package com.assistdecision.domain.output;


import com.assistdecision.core.util.AppConsts;
import com.assistdecision.model.AppraisalPlan;

import java.math.BigDecimal;

public class AppraisalPlanOutput extends AppraisalPlan {
    private String employeeName;
    private String organizationName;
    private String isStarName;
    private String stateName;
    private String office;
    private String windowName;
    private Integer windowState;
    private String windowOrOfficeName;
    private String isEnabledName;

    /**考核描述*/
    private String description;

    /**最终一共加了或者减了多少分*/
    private BigDecimal ruleScore;

    /**计分类型，加分或者是减分*/
    private Integer scoreType;
    private String scoreTypeName;


    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getRuleScore() {
        return ruleScore;
    }

    public void setRuleScore(BigDecimal ruleScore) {
        this.ruleScore = ruleScore;
    }

    public Integer getScoreType() {
        return scoreType;
    }

    public void setScoreType(Integer scoreType) {
        this.scoreType = scoreType;
    }

    public String getScoreTypeName() {
        if(this.getScoreType()!=null){
            if(this.getScoreType()==1){
               return scoreTypeName = "减分";
            }else if(this.getScoreType()==0){
               return scoreTypeName = "加分";
            }else {
               return scoreTypeName = "";
            }
        }
        return "";
    }

    public void setScoreTypeName(String scoreTypeName) {
        this.scoreTypeName = scoreTypeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setObjectTypeName(String objectTypeName) {
        this.objectTypeName = objectTypeName;
    }

    //模板类型
    private String typeName;
    //对象类型
    private String objectTypeName;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getIsStarName() {
        if(getIsStar()!=null){
            switch (getIsStar()){
                case 0:
                    isStarName="否";
                    break;
                case 1:
                    isStarName="是";
                    break;
                default:
                    isStarName="";
                    break;
            }
        }
        return isStarName;
    }

    public void setIsStarName(String isStarName) {
        this.isStarName = isStarName;
    }

    public String getStateName() {
        if(getState()!=null){
            switch (getState()){
                case 0:
                    stateName="已生成";
                    break;
                case 1:
                    stateName="已同步";
                    break;
                case 2:
                    stateName="已提交";
                    break;
                case 3:
                    stateName="已确认";
                    break;
                default:
                    stateName="";
                    break;

            }
        }
        return stateName;
    }

    public String getTypeName() {
        if (getType() != null) {
            if (getType() == AppConsts.Annual_Assessment) {
                typeName = "年度考核";
            } else if (getType() == AppConsts.Monthly_Assessment) {
                typeName = "月度考核";
            }
        }
        return typeName;
    }

    public String getObjectTypeName() {
        if (getObjectType() != null) {
            if (getObjectType() == AppConsts.Window) {
                objectTypeName = "窗口";
            } else if (getObjectType() == AppConsts.StaffMember) {
                objectTypeName = "工作人员";
            }
        }
        return objectTypeName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getWindowOrOfficeName() {
        if(this.getWindowState()!=null){
            switch (this.getWindowState()){
                case 0:
                    windowOrOfficeName=this.office;
                    break;
                case 1:
                    windowOrOfficeName=this.windowName;
                    break;
                default:
                    windowOrOfficeName="";
                    break;
            }
        }
        return windowOrOfficeName;
    }

    public void setWindowOrOfficeName(String windowOrOfficeName) {
        this.windowOrOfficeName = windowOrOfficeName;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getWindowName() {
        return windowName;
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }

    public Integer getWindowState() {
        return windowState;
    }

    public void setWindowState(Integer windowState) {
        this.windowState = windowState;
    }

    //市民卡银行卡号(员工基本信息)
    private String bankCardNumber;

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getIsEnabledName() {
        if (getIsEnabled() != null) {
          if(getIsEnabled()==1){
              isEnabledName="否";
          }else {
              isEnabledName="是";
          }
        }
        return isEnabledName;
    }

    public void setIsEnabledName(String isEnabledName) {
        this.isEnabledName = isEnabledName;
    }
}
