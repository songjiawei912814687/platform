package com.common.Enum;

/**
 * @author: Young
 * @description:
 * @date: Created in 17:15 2018/11/8
 * @modified by:
 */
public enum AppraiseStateEnum {

    UN_APPRAISE(0,"未评价"),
    IS_APPRAISE(1,"已评价");

    private Integer code;
    private String desc;

    AppraiseStateEnum(Integer code, String desc) {
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
