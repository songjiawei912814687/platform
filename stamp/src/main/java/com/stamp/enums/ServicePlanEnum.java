package com.stamp.enums;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-04-17  09:39
 * @modified by:
 */
public enum  ServicePlanEnum {
    TWO_IN_ONE("A","二合一（营业执照+公章）"),
    THREE_IN_ONE_A("B1","三合一(营业执照+公章+发票)"),
    THREE_IN_ONE_B("B2","三合一(营业执照+公章+开户)"),
    FOUR_IN_ONE("C","四合一（营业执照+公章+发票+开户）"),

    ;

    private String code;
    private String desc;

    ServicePlanEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code){
        if(code!=null&&code!=""){
            for(var e:ServicePlanEnum.values()){
                if(code.equals(e.getCode())){
                    return e.getDesc();
                }
            }
        }
        return "";
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
