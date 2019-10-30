package com.common.Enum;

/**
 * @author: Young
 * @description: 申请方式
 * @date: Created in 12:29 2018/11/14
 * @modified by:
 */
public enum ApplyTypeNameEnum {

    NET_APPLY("1","网上申请"),
    WINDOW_APPLY("2","现场窗口申请"),
    POST_APPLY("3","邮寄申请"),
    MACHINE_APPLY("4","现场自动机申请"),

    ;

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc(String code) {
        if(code!=null){
            for(var e: values()){
                if(code.equals(e.getCode())){
                    return e.getDesc();
                }
            }
        }
        return "";
    }

    ApplyTypeNameEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
