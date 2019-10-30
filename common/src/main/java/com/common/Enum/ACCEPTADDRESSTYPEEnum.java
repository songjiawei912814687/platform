package com.common.Enum;

/**
 * @author: Young
 * @description: 受理地点分类
 * @date: Created in 9:05 2018/9/17
 * @modified by:
 */
public enum ACCEPTADDRESSTYPEEnum {

    CENTER(1,"行政服务中心"),
    WINDOW(2,"部门服务窗口"),
    OFFICE(3,"单位办公室"),
    PLACE(4,"案件发生地"),
    POINT(5,"代办点"),
    SERVICE(6,"便民服务中心"),

    ;
    private Integer code;
    private String msg;

    ACCEPTADDRESSTYPEEnum(Integer code, String msg) {
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
