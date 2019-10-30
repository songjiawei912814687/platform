package com.message.domain.output;

import com.common.Enum.MessageTypeEnum;
import com.message.model.SMSReceive;

/**
 * @author: Young
 * @description:
 * @date: Created in 11:40 2018/10/25
 * @modified by:
 */
public class SMSReceiveOutPut extends SMSReceive {

    private String typeName;

    public String getTypeName() {
        if(getType()!=null){
         return MessageTypeEnum.getMsg(this.getType());
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
