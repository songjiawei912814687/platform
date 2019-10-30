package com.common.Enum;

public enum EmployeeTypeEnum {
    WINDOW_TYPE(1,"窗口"),
    BACKGROUND_TYPE(0,"后台"),
    ADMINISTRATIVE_TYPE(2,"行政");
;
    private Integer code;
    private String msg;



    public static String getMsg(Integer code){
        for(var e : EmployeeTypeEnum.values()){
            if(e.code == code){
                return e.msg;
            }
        }
        return "";
    }

    EmployeeTypeEnum(Integer code, String msg) {
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
