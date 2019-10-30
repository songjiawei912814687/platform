package com.stamp.enums;

public enum AttachmentEnum {
    MEETING_TYPE(1,"会议预约"),
    LEAVE_APPLICATION_TYPE(2,"请假申请"),
    APPRAISAL_EMPLOYEE_RECORD_TYPE(3,"员工加减分记录"),
    APPRAISAL_COMPLAIN_TYPE(4,"考核申诉"),
    DIMISSION_TYPE(5,"离职申请"),
    DOWNLOADCEN_TYPE(6,"下载中心"),
    COMPLAINT_TYPE(7,"投诉附件"),
    FEEDACK_TYPE(8,"反馈附件"),
    EXAMPLE_SHEET(9,"材料清单示例表"),
    BLANK_SHEET(10,"材料清单空白表"),
    PIT_TYPE(11,"招标评标预约"),
    STAMP(12,"刻章单位"),
    ;

    private Integer code;
    private String msg;

    AttachmentEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsg(Integer code){
        for(var e : AttachmentEnum.values()){
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
