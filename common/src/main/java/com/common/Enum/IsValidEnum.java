package com.common.Enum;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-03-11  17:53
 * @modified by:
 */
public enum  IsValidEnum {

    IS_VALID(0,"有效"),
    UN_VALID(1,"无效"),
    ;

    private Integer code;
    private String desc;

    IsValidEnum(Integer code, String desc) {
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
