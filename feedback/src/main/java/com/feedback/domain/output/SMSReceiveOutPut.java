package com.feedback.domain.output;


import com.feedback.model.SMSReceive;

/**
 * @author: Young
 * @description:
 * @date: Created in 11:40 2018/10/25
 * @modified by:
 */
public class SMSReceiveOutPut extends SMSReceive {

    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
