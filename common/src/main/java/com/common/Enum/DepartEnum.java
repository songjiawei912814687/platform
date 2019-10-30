package com.common.Enum;

import org.apache.commons.lang3.StringUtils;

public enum DepartEnum {

    QU_FA_GAI("001008001010011","区发改局"),
//    QU_TONG("002506267","区统计局"),
    QU_ZHU("001008001010015","区住建局"),
    GUI_FU("001008001010034","规划和自然资源富阳分局"),
    QU_JIAOTONG("002506320","区交通局"),
    QU_JIN("738424192","区经信局"),
    QU_QIAO("00250603X","区侨办"),
    QU_WEN_GUANG("002506507","区文广新局"),
    QU_JIAOYU("002506515","区教育局"),
    QU_RENSHE("002506152","区人社局"),
    QU_SUI_WU("002506996","区税务局"),
    QU_KE_JI("002506136","区科技局"),
    QU_MIN_ZHENG("002506224","区民政局"),
    QU_DANG_AN("001008001010024","区档案局"),
    GONG_JI("776629965","公积金富阳分中心"),
    QU_SIFA("002506216","区司法局"),
    GONGAN_FUYANG("002506208","公安富阳分局"),
    QU_SHANGWU("733800195","区商务局"),
    QU_SHICHANG("002506451","区市场监管局"),
    QU_YANCHAO("67399306X","区烟草局"),
    QU_LUYOU("68582671X","区旅游体育局"),
    QU_SHUILI("001008001010033","区水利水电局"),
    QU_QIXIANG("470333651","区气象局"),
    QU_JINJI("470334195","区经济技术开发区"),
    QU_CHAN("733810684","区残联"),
    QU_CAI_ZHENG("002506443","区财政局"),
    QU_RENFANG("776644241","区人防办"),
    QU_ANJIAN("773571745","区安监局"),
    QU_CHENGGUAN("762001165","区城管执法局"),
    GUOTU("002506259","国土富阳分局"),
    QU_HUANBAO("002506291","区环保局"),
    QU_SHENJI("001008001010032","区审计局"),
    QU_MINZU("001008001010088","区民族宗教事务局"),
    HANGZHOU_SHIMIN("001008001010108","杭州富阳市民卡有限公司"),
    QU_WEIJI("001008001010122","区卫计局"),
    QU_NONGLIN("001008001010121","区农林局"),
    YINHANG_FUYANG("2017050800000046081","中国人民银行富阳支行"),

    ;

    private String code;
    private String desc;

    DepartEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc(String code) {
        if(StringUtils.isNotBlank(code)){
            for(var e:DepartEnum.values()){
                if(code.equals(e.code)){
                    return e.desc;
                }
            }
        }
        return "";
    }
}
