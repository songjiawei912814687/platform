package com.attendance.enums;

/**
 * @author: Young
 * @description:
 * @date: Created in 11:30 2018/10/4
 * @modified by:
 */
public enum VerificationEnum {

    IS_VERIFICATION(0,"已核销"),
    UN_VERIFICATION(1,"未核销"),
    IS_USE(2,"使用中");

    private Integer code;
    private String msg;

    VerificationEnum(Integer code, String msg) {
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
