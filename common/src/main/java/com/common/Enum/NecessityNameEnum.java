package com.common.Enum;

/**
 * @author: Young
 * @description: 必要性及描述枚举
 * @date: Created in 19:16 2018/11/17
 * @modified by:
 */
public enum NecessityNameEnum {
    NECESSARY(1,"必要"),
    UN_NECESSARY(2,"非必要"),
    AFTER_REPAIR(3,"容缺后补"),
    ;
    private Integer code;
    private String desc;

    NecessityNameEnum(Integer code, String desc) {
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
