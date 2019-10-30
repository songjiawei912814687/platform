package com.knowledge.enums;

public enum SeachTypeEnum {
    QUE_TYPE(100L,"咨询问题"),
    QIT_TYPE(200L,"权利事项")

    ;
    private Long code;

    private String msg;

    SeachTypeEnum(Long code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
