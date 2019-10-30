package com.meeting.enums;


/**
 * @author: Young
 * @description:
 * @date: Created in 11:36 2018/9/15
 * @modified by:
 */
public enum CheckStatusEnum {
    UN_CHECK(1,"待审核"),
    CHECK_FINISH(3,"审核通过"),
    CHECK_FAILED(5,"审核未通过"),
    ;
    private Integer code;
    private String msg;

    CheckStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
