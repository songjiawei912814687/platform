package com.message.domain.output;

import com.common.Enum.MessageTypeEnum;
import com.message.enums.SendMessageEnum;
import com.message.model.SMSSend;

public class SMSSendOutput extends SMSSend {
    private String stateName;


    private String typeName;

    public String getTypeName() {
        if(this.getType() != null){
            return MessageTypeEnum.getMsg(this.getType());
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStateName() {
        if(this.getState() != null){
            return SendMessageEnum.getDesc(this.getState());
        }
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
