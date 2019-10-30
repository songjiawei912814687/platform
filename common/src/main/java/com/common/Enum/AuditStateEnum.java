package com.common.Enum;

public enum AuditStateEnum {
    AUDIT(0,"未审核"),
    AUDITED(1,"审核通过"),
    AUDITFAIL(2,"审核不通过"),
    UNDO_TYPE(3,"已撤销")
;
    private Integer code;
    private String msg;



    public static String getMsg(Integer code){
        for(var e : AuditStateEnum.values()){
            if(e.code == code){
                return e.msg;
            }
        }
        return "";
    }

    AuditStateEnum(Integer code, String msg) {
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
