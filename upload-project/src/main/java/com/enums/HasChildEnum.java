package com.enums;

import lombok.Getter;

/**
 * @author: Young
 * @description:
 * @date: Created in 9:39 2018/8/31
 * @modified by:
 */
@Getter
public enum HasChildEnum {

    HAVE(0, "有"),
    HAVE_NONE(1, "没有");

    private Integer code;
    private String desc;

    HasChildEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
