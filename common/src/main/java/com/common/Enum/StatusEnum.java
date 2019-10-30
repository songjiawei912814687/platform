package com.common.Enum;

/**
 * @author: Young
 * @description: 可用状态枚举
 * @date: Created in 9:05 2018/9/17
 * @modified by:
 */
public enum StatusEnum {

    USE(1,"可用"),
    UN_USE(3,"不可用"),

    ;
    private Integer code;
    private String msg;

    StatusEnum(Integer code, String msg) {
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
