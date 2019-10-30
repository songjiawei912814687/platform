package com.common.Enum;

/**
 * @author: Young
 * @description: 事项审查类型
 * @date: Created in 12:21 2018/11/14
 * @modified by:
 */
public enum ShixiangsctypeNameEnum {

    NONE("0","无"),
    BEFORE_SHIPMENT("1","前审后批"),
    ON_RUN("2","即审即办"),
    APPROVAL_PARALLEL("3","并联审批"),
    OTHER("4","其他"),
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

    ShixiangsctypeNameEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
