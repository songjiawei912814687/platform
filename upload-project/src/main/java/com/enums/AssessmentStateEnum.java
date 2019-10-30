package com.enums;

import lombok.Getter;

/**
 * @author: Young
 * @description:
 * @date: Created in 9:35 2018/8/31
 * @modified by:
 */
@Getter
public enum AssessmentStateEnum {

    CHECK(0, "考核"),
    UN_CHECK(1, "不考核");
    private Integer code;
    private String desc;

    AssessmentStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
