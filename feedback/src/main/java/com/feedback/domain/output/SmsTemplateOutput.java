package com.feedback.domain.output;

import com.common.Enum.SmsTemplateEnum;
import com.feedback.model.SmsTemplate;


public class SmsTemplateOutput extends SmsTemplate {
    private String typeName;
    private String statName;
    private String isReplyName;

    public String getTypeName() {
        if(getType()!=null){
           typeName= SmsTemplateEnum.getMsg(getType());
        }else {
            typeName="";
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStatName() {
        if(getState()!=null){
            switch (getState()){
                case 0:
                    statName="停用";
                    break;
                case 1:
                    statName="启用";
                    break;
                default:
                    statName="";
                    break;
            }
        }
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public String getIsReplyName() {
        if(getIsReply()!=null){
            switch (getIsReply()){
                case 0:
                    isReplyName="否";
                    break;
                case 1:
                    isReplyName="是";
                    break;
                default:
                    statName="";
                    break;
            }
        }
        return isReplyName;
    }

    public void setIsReplyName(String isReplyName) {
        this.isReplyName = isReplyName;
    }
}
