package com.common.Enum;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-03-12  10:58
 * @modified by:
 */
public enum MeetingApplyEnum {

    ZERO(0,"8:00"),
    ONE(1,"8:30"),
    TWO(2,"9:00"),
    THREE(3,"9:30"),
    FOUR(4,"10:00"),
    FIVE(5,"10:30"),
    SIX(6,"11:00"),
    SEVEN(7,"11:30"),
    EIGHT(8,"12:00"),
    NINE(9,"12:30"),
    TEN(10,"13:00"),
    ELEVEN(11,"13:30"),
    TWELVE(12,"14:00"),
    THRTEEN(13,"14:30"),
    FOURTEEN(14,"15:00"),
    FIFTEEN(15,"15:30"),
    SIXTEEN(16,"16:00"),
    SEVENTEEN(17,"16:30"),
    EIGTHTEEN(18,"17:00"),
    NINETEEN(19,"17:30"),
    TWEENTY(20,"18:00"),
    TWEENTYONE(21,"18:30"),
    TWEENTYTWO(22,"19:00"),
    TWEENTYTHREE(23,"19:30"),

    ;

    private Integer code;
    private String desc;

    MeetingApplyEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(Integer code){
        for(MeetingApplyEnum e:MeetingApplyEnum.values()){
            if(e.code.equals(code)){
                return e.getDesc();
            }
        }
        return "";
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
