package com.common.Enum;

public enum IsThisEnum {
    WINDOW_TYPE(1,"是"),
    BACKGROUND_TYPE(0,"否"),
;
    private Integer code;
    private String msg;



    public static String getMsg(Integer code){
        for(var e : IsThisEnum.values()){
            if(e.code == code){
                return e.msg;
            }
        }
        return "";
    }

    IsThisEnum(Integer code, String msg) {
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
