package com.common.Enum;

/**
 * @author: Young
 * @description: 法定期限单位
 * @date: Created in 9:05 2018/9/17
 * @modified by:
 */
public enum LIMITTIMETYPEEnum {

    WORKDAY(1,"工作日"),
    MONTH(2,"月"),
    YEAR(3,"年"),
    DAYS(4,"天"),

    ;
    private Integer code;
    private String msg;

    LIMITTIMETYPEEnum(Integer code, String msg) {
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
