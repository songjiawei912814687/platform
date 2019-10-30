package com.common.Enum;

/**
 * @author: Young
 * @description: 权力来源枚举
 * @date: Created in 12:10 2018/11/14
 * @modified by:
 */
public enum ItemSourceNameEnum {
    LAW_SAME_LEVEL("1","法定本级行使"),
    CENTER_PROVINCIAL_LEVEL("2","中央下放待省级"),
    PROVINCIAL_LEVEL_CITIES("3","省级下放到区市"),
    PROVINCIAL_LEVEL_TOWN("4","省级下放到县（市、区）"),
    CITIES_LEVEL_TOWN("5","设区市下放到县（市、区）"),
    CENTER_LEVEL_CITIES("6","中央下放到设区市"),
    CENTER_LEVEL_TOWN("7","中央下放到县（市、区）"),
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

    ItemSourceNameEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
