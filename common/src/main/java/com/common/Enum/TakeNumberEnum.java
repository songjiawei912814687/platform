package com.common.Enum;

/**
 * @author: young
 * @project_name: svn
 * @description: 取号状态枚举
 * @date: Created in 2018-12-12  17:22
 * @modified by:
 */
public enum TakeNumberEnum {
    IS_TAKE(0,"已经取号"),
    IS_CANCEL(1,"已经取消"),
    COMPLETE(3,"完成办理"),
    START_WORK(5,"开始办理"),
    PASS_NUMBER(7,"过号"),

    ;
    private Integer code;
    private String desc;

    TakeNumberEnum(Integer code, String desc) {
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
