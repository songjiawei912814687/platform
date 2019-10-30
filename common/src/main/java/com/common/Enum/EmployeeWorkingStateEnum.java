package com.common.Enum;

public enum EmployeeWorkingStateEnum {
    TOBEHIRED(0,"待入职"),
    ONTHEJOB(1,"在职"),
    DEPARTURE(3,"离职"),
    ;

    private Integer code;
    private String msg;

    EmployeeWorkingStateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsg(Integer code){
        for(var e : EmployeeWorkingStateEnum.values()){
            if(e.code == code){
                return e.msg;
            }
        }
        return "";
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
