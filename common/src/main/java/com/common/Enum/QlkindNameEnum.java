package com.common.Enum;

/**
 * @author: Young
 * @description: 权力事项类型枚举
 * @date: Created in 11:22 2018/11/14
 * @modified by:
 */
public enum QlkindNameEnum {
    ADMINISTRATION_PERMISSION("01","行政许可"),
    ADMINISTRATION_PUNISH("03","行政处罚"),
    ADMINISTRATION_FORCE("04","行政强制"),
    ADMINISTRATION_LEVY("05","行政征收"),
    ADMINISTRATION_PAY("06","行政给付"),
    ADMINISTRATION_DECIDE("07","行政裁决"),
    ADMINISTRATION_SURE("08","行政确认"),
    ADMINISTRATION_AWARD("09","行政奖励"),
    ADMINISTRATION_OTHER_POWER("10","其他行政权力"),
    AUDIT_INFORMATION("13","审核转报"),
    PUBLIC_SERVICE("14","公共服务"),
    INNER_MANAGEMENT("15","内部管理"),
    SEEC_MATTERS("16","联办事项"),

    ;

    private String  code;
    private String desc;

    QlkindNameEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc(String code){
        if(code!=null){
            for(var e:values()){
                if (code.equals(e.getCode())) {
                    return e.getDesc();
                }
            }
        }
        return "";
    }
}
