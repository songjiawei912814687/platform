package com.common.Enum;

/**
 * @author: Young
 * @description:
 * @date: Created in 9:59 2018/11/6
 * @modified by:
 */
public enum CompleteRateEnum {

    ONE(1,"跑一次"),
    MORE(2,"跑多次"),

    ;

    private Integer code;
    private String desc;

    CompleteRateEnum(Integer code, String desc) {
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
