package com.attendance.enums;

/**
 * @author: Young
 * @description:
 * @date: Created in 20:24 2018/9/30
 * @modified by:
 */
public enum  AttendanceRuleEnum {
    MORNING_GO_WORK(1,"上午上班时间"),
    MORNING_AFTER_WORK(2,"上午下班时间"),
    AFTERNOON_GO_WORK(3,"下午上班时间"),
    AFTERNOON_AFTER_WORK(4,"下午下班时间"),
    ;

    private Integer code;
    private String msg;

    AttendanceRuleEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
