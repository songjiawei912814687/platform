package com.attendance.enums;

/**
 * @author: Young
 * @description: 是否循环
 * @date: Created in 8:46 2018/9/20
 * @modified by:
 */
public enum IsCyleEnum {

    UN_CYLE(1,"不是循环"),
    IS_CYLE(3,"是循环"),

    ;

    private Integer code;
    private String msg;

    IsCyleEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
