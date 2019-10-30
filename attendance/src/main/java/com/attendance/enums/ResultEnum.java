package com.attendance.enums;

import lombok.Getter;

/**
 * @author: Young
 * @description:
 * @date: Created in 12:46 2018/9/1
 * @modified by:
 */
@Getter
public enum ResultEnum {

    ARGUMENT_ERROR(1, "参数错误"),

    CONVERT_METHOD(2, "转换方法错误"),

    NAME_EXISTS(3, "名字已存在"),

    CLASS_NOT_FOUND(4, "未找到实体类的对象"),

    ;

    private Integer code;
    private String desc;

    ResultEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
