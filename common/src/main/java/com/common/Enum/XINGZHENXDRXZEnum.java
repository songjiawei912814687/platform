package com.common.Enum;

/**
 * @author: Young
 * @description: 行政相对人性质枚举
 * @date: Created in 9:05 2018/9/17
 * @modified by:
 */
public enum XINGZHENXDRXZEnum {

    PERSONAL(1,"个人"),
    LEGALPERSON(2,"法人"),
    OTHERORGS(3,"其他组织"),
    OTHERS(4,"允许个人代办"),

    ;
    private Integer code;
    private String msg;

    XINGZHENXDRXZEnum(Integer code, String msg) {
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
