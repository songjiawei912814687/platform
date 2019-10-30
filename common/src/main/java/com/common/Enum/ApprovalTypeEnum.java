package com.common.Enum;

public enum ApprovalTypeEnum {

    LEAVE_TYPE(1,"请假申请","attendance"),
    OVERTIME_TYPE(3,"加班申请","attendance"),
    METTING_TYPE(5,"会议室申请","metting"),
    EMPLOYEE_INDUCTION_TYPE(7,"人员入职申请","employee"),
    EMPLOYEE_DEPARTURE_TYPE(9,"人员离职申请","employee"),
    ASSESSMENT_APPEAL_TYPE(11,"考核申诉","assessment"),
    JOB_TRANSFER(13,"岗位调动","employee"),
    ADJUST_TYPE(15,"调休","attendance"),
    EMPLOYEE_RECORD_TYPE(17,"员工加减分记录","assessment"),

    ;

    private Integer code;
    private String msg;
    private String model;


    ApprovalTypeEnum(Integer code, String msg,String model) {
        this.code = code;
        this.msg = msg;
        this.model = model;
    }



    public static String getMsg(Integer code){
        for(var e : ApprovalTypeEnum.values()){
            if(e.code == code){
                return e.msg;
            }
        }
        return "";
    }

    public static String getModel(Integer code){
        for(var e : ApprovalTypeEnum.values()){
            if(e.code == code){
                return e.model;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
