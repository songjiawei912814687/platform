package com.meeting.enums;

/**
 * @author: Young
 * @description:
 * @date: Created in 11:05 2018/9/18
 * @modified by:
 */
public enum RoomStateEnum {

    FREE(1,"空闲中"),
    IN_ORDER(3,"使用中"),
    ;
    private Integer code;
    private String desc;

    RoomStateEnum(Integer code, String desc) {
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
