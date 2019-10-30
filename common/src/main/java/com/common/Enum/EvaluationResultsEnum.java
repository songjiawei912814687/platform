package com.common.Enum;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-12  18:14
 * @modified by:
 */
public enum EvaluationResultsEnum {

    SUCCESS(0,"成功"),
    FAIL(1,"失败"),

    ;
    private Integer code;
    private String desc;

    EvaluationResultsEnum(Integer code, String desc) {
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
