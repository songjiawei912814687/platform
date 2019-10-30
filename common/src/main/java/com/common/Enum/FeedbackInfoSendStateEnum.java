package com.common.Enum;

/**
 * @author: Young
 * @description:
 * @date: Created in 17:44 2018/11/9
 * @modified by:
 */
public enum FeedbackInfoSendStateEnum {

    WAIT_SEND(0,"待发"),
    SEND_SUCCESS(1,"发送成功"),
    DEAL_SUCCESS(2,"处理成功"),
    UN_VALID(3,"回复的短信无效"),
    ;

    private Integer code;
    private String desc;

    FeedbackInfoSendStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
