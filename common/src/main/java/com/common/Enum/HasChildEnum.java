package com.common.Enum;


/**
 * @author: Young
 * @description:
 * @date: Created in 9:39 2018/8/31
 * @modified by:
 */

public enum HasChildEnum {

    HAVE(0, "有"),
    HAVE_NONE(1, "没有");

    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    HasChildEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
