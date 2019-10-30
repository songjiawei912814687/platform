package com.wechatsug.enums;

/**
 * @author: Young
 * @description:
 * @date: Created in 12:59 2018/10/24
 * @modified by:
 */
public enum IsTimingEnum {

    NO(0,"不定时"),
    YES(1,"定时"),

    ;
    private Integer code;
    private String desc;

    IsTimingEnum(Integer code, String desc) {
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
