package com.common.Enum;

/**
 * @author: Young
 * @description: 材料形式枚举
 * @date: Created in 19:07 2018/11/17
 * @modified by:
 */
public enum FormatNameEnum {

    ORIGINAL(1,"原件"),
    COPY(2,"复印件"),
    ORIGINAL_OR_COPY(3,"原件或者复印件"),
    PAPER(4,"纸质表格"),
    ELECTRONIC_DOCUMENT(5,"普通电子文件"),
    NO_SUBMIT(6,"系统自动获取，无需申请者提交"),
    DATA_SCARCITY(7,"系统自动获取，如数据不全则需申请者提交"),
    SCAN_ORIGINAL(8,"原件扫描件"),
    PDF(9,"PDF格式电子文件"),
    ;
    private Integer code;
    private String desc;

    FormatNameEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
