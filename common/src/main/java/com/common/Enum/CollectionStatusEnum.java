package com.common.Enum;

/**
 * @author: Young
 * @description: 补录状态枚举
 * @date: Created in 16:37 2018/9/16
 * @modified by:
 */
public enum CollectionStatusEnum {

    UN_COLLECTION(1,"不是补录"),
    IS_COLLECTION(3,"是补录"),

    ;
    private Integer code;
    private String msg;

    CollectionStatusEnum(Integer code, String msg) {
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
