package com.common.Enum;

public enum  MessageCenterTypeEnum {
    DRAFT_TYPE(1,"草稿"),
    RELEASE_TYPE(3,"发布")
    ;
    private Integer code;

    private String msg;

    MessageCenterTypeEnum(Integer code, String msg) {
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
