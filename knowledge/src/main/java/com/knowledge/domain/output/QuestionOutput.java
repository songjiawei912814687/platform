package com.knowledge.domain.output;

import com.knowledge.model.Question;

public class QuestionOutput extends Question{
    private String isTopName;
    private String isOpenName;
    private String organizationName;

    public String getIsTopName() {
        if(getIsTop()!=null){
            switch (getIsTop()){
                case 0:
                    isTopName="否";
                    break;
                case 1:
                    isTopName="是";
                    break;
                default:
                     isTopName="";
                     break;
            }
        }
        return isTopName;
    }

    public void setIsTopName(String isTopName) {
        this.isTopName = isTopName;
    }

    public String getIsOpenName() {
        if(getIsOpen()!=null){
            switch (getIsOpen()){
                case 0:
                    isOpenName="否";
                    break;
                case 1:
                    isOpenName="是";
                    break;
                default:
                    isOpenName="";
                    break;
            }
        }
        return isOpenName;
    }

    public void setIsOpenName(String isOpenName) {
        this.isOpenName = isOpenName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    private String stateName;
    private String name;
    private String phoneNumber;
    private String typeName;

    public String getStateName() {
        if(getState()!=null){
            switch (getState()){
                case 0:
                    stateName="未发布";
                    break;
                case 1:
                    stateName="已发布";
                    break;
            }
        }
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    private String answerStateName;

    public String getAnswerStateName() {
        if(getAnswerState()!=null){
            switch (getAnswerState()){
                case 0:
                    answerStateName="未回答";
                    break;
                case 1:
                    answerStateName="已回答";
                    break;
            }
        }
        return answerStateName;
    }

    public void setAnswerStateName(String answerStateName) {
        this.answerStateName = answerStateName;
    }
}
