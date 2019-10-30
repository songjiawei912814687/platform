package com.feedback.enums;

/**
 * @author: Young
 * @description:
 * @date: Created in 12:59 2018/10/24
 * @modified by:
 */
public enum SendStateEnum {

    UN_SEND(0,"待发送"),
    SEND_SUCCESS(1,"发送成功"),
    SEND_FAILTURE(3,"发送失败");

    private Integer code;
    private String desc;

    SendStateEnum(Integer code, String desc) {
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
