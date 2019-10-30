package com.common.Enum;

/**
 * @author: Young
 * @description:
 * @date: Created in 9:56 2018/11/6
 * @modified by:
 */
public enum SatisfactionEnum {

    SATISFIED(0,"满意"),
    UN_SATISFIED(1,"不满意"),

    ;
    private Integer code;
    private String desc;

    SatisfactionEnum(Integer code, String desc) {
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
