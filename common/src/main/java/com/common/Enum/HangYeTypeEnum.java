package com.common.Enum;

/**
 * @author: Young
 * @description: 行业主题枚举
 * @date: Created in 17:06 2018/11/15
 * @modified by:
 */
public enum  HangYeTypeEnum {

    EDUCATION("01","教育"),
    MEDICAL_TREATMENT("02","卫生计生--医疗卫生"),
    MEDICAL_BORN("03","卫生计生--生育"),
    CULTURE_SPORTS("04","文化体育"),
    RADIO_PUBLICATION("05","新闻广电出版"),
    SCIENCE_INNOVATE("06","科技创新"),
    CIVIL_WEDDING("07","民政事务--婚姻"),
    CIVIL_ADOPTION("08","民政事务--收养"),
    CIVIL_WELFARE("09","民政事务--福利救助"),
    CIVIL_LEAVE_ARMY("10","民政事务--兵役退伍"),
    CIVIL_DEAD("11","民政事务--死亡殡葬"),
    CIVIL_SOCIAL_ORGANIZATION("12","民政事务--社会组织"),
    CIVIL_OTHER("13","民政事务--其他"),
    PERSONNEL("14","人力资源"),
    SOCIAL_SECURITY("15","社会保障"),
    TRAVEL_SERVICE("16","旅游服务"),
    LAND_RESOURCE("17","国土资源"),
    HOUSE_CONSTRUCTION("18","住房建设"),
    TRANSPORTATION("19","交通运输"),
    ENVIRONMENTAL_METEOROLOGICAL("20","环保气象"),
    MARKET_INDUSTRY("21","市场监管--工商监管"),
    MARKET_TECHNOLOGY("22","市场监管--质量技术监督"),
    MARKET_FOOD("23","市场监管--食品药品监管"),
    SAFETY_PRODUCTION("24","安全生产"),
    SECURITY_FIRE("25","公安消防"),
    ULTIMATE_JUSTICE("26","司法公证"),
    PLANNING_SURVEYING("27","规划测绘"),
    ECONOMIC_DEVELOPMENT("28","经济发展"),
    FINANCE_AFFAIRS("29","财政税务"),
    NATIONAL_RELIGION("30","民族宗教"),
    FOREIGN_AFFAIRS("31","外事侨务"),
    ANIMAL_WATER("32","农林牧水"),
    MARINE_FISHERY("33","海洋渔业"),
    PUBLIC_UTILITY("34","公用事业"),
    INTERMEDIARY_SERVICES("35","中介服务"),
    OTHER("36","其他"),
    CUSTOMS_INSPECTION("37","海关检验"),
    FINANCE_SECURITIES("38","金融证券"),
    POSTAL_COMMUNICATION("39","通信邮政"),

    ;
    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc(String code) {
        if(code!=null){
            for(var e:HangYeTypeEnum.values()){
                if(code.equals(e.getCode())){
                    return e.getDesc();
                }
            }
        }
        return "";
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    HangYeTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
