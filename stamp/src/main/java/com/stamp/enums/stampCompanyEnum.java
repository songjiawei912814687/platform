//package com.stamp.enums;
//
///**
// * @author: young
// * @project_name: svn
// * @description: 刻章单位
// * @date: Created in 2019-04-17  09:58
// * @modified by:
// */
//public proenum  stampCompanyEnum {
//
//    ONE(1,"杭州富阳子恒图文工作室"),
//    TWO(2,"杭州富阳中兴刻章店"),
//    THREE(3,"杭州富阳新政印务有限公司"),
//    FOUR(4,"杭州富阳行正图文设计室"),
//    FIVE(5,"杭州富阳迅捷刻章店"),
//    SIX(6,"杭州富阳富春街道朱记刻字店"),
//    SEVEN(7,"富阳市邓字广告有限公司"),
//    EIGHT(8,"杭州富阳区富春街道富祥刻章店"),
//    NIGH(9,"杭州富阳区富春街道何增刻印社"),
//    TEN(10,"杭州富阳钱程刻字社"),
//    EVLEVEN(11,"杭州富阳立行广告工作室"),
//    TEWLVE(12,"杭州富阳鸿图文社"),
//    ;
//    private Integer code;
//    private String desc;
//
//    stampCompanyEnum(Integer code, String desc) {
//        this.code = code;
//        this.desc = desc;
//    }
//
//    public Integer getCode() {
//        return code;
//    }
//
//    public static String getDesc(Integer code){
//        if(code!=null){
//            for(var e:stampCompanyEnum.values()){
//                if (code.equals(e.getCode())) {
//                    return e.getDesc();
//                }
//            }
//        }
//        return "";
//    }
//
//    public String getDesc() {
//        return desc;
//    }
//}
