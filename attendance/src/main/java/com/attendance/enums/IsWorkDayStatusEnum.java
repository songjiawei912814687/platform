package com.attendance.enums;

/**
 * @author: Young
 * @description:
 * @date: Created in 17:08 2018/9/19
 * @modified by:
 */
public enum IsWorkDayStatusEnum {

    NOT_WORKDAY(1,"非工作日"),
    IS_WORKDAY(3,"是工作日"),

    ;

    private Integer code;
    private String msg;

    IsWorkDayStatusEnum(Integer code, String msg) {
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

