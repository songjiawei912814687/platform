package com.common.Enum;

/**
 * @author: young
 * @project_name: assist-decision
 * @description: 组织机构配置的规则
 * @date: Created in 2019-03-26  13:19
 * @modified by:
 */
public enum ApproveRuleEnum {

    IN_ONE_DAY(0.5,"一天之内"),
    ONE_DAY(1.0,"一天"),
    WITHIN_TWO_DAYS(1.5,"一天之外，两天之内"),
    TWO_DAYS(2.0,"两天"),
    WITHIN_THREE_DAYS(2.5,"两天之外，三天之内"),
    THREE_DAYS(3.0,"三天"),

    ;

    private Double code;
    private String desc;

    ApproveRuleEnum(Double code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Double getCode() {
        return code;
    }

    public static String getDesc(String code){
        for(var e:values()){
            if(code.equals(e.getCode())){
                return e.desc;
            }
        }
        return "";
    }

    public String getDesc() {
        return desc;
    }
}
