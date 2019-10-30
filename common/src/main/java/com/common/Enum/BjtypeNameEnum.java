package com.common.Enum;

/**
 * @author: Young
 * @description: 办件类型
 * @date: Created in 12:06 2018/11/14
 * @modified by:
 */
public enum BjtypeNameEnum {
    IMMEDIATELY_DO("1","即办件"),
    PROMISE_DO("2","承诺件"),
    OTHER_DO("3","其他"),
    ;
    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc(String code){
        if(code!=null){
            for(var e:values()){
                if(code.equals(e.getCode())){
                    return e.getDesc();
                }
            }
        }
        return "";
    }

    BjtypeNameEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
