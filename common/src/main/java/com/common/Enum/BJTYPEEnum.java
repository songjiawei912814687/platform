package com.common.Enum;

/**
 * @author: Young
 * @description: 办件类型
 * @date: Created in 9:05 2018/9/17
 * @modified by:
 */
public enum BJTYPEEnum {

    CURRENT(1,"即办件"),
    PROMISE(2,"承诺件"),
    OTHERS(3,"其他"),

    ;
    private Integer code;
    private String msg;

    BJTYPEEnum(Integer code, String msg) {
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
