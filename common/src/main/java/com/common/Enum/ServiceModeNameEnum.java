package com.common.Enum;

/**
 * @author: Young
 * @description: 送达方式枚举
 * @date: Created in 18:04 2018/11/17
 * @modified by:
 */
public enum ServiceModeNameEnum {
    ARRIVE_SPOT("1","当场送达"),
    ARRIVE_EXPRESS("2","快递送达"),
    ARRIVE_NET("3","电子文件网上送达"),
    ARRIVE_STAFF("4","代办员送达 （除去处罚、强制、裁决）"),

    ;
    private String code;
    private String desc;

    ServiceModeNameEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc(String code) {
        if(code!=null){
            for(var e:values()){
                if(code.equals(e.getCode())){
                    return e.getDesc();
                }
            }
        }
        return "";
    }
}
