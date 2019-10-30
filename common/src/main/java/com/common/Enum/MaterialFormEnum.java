package com.common.Enum;

/**
 * @author: Young
 * @description: 材料清单材料形式枚举
 * @date: Created in 9:05 2018/9/17
 * @modified by:
 */
public enum MaterialFormEnum {

    ORIGINAL(1,"原件"),
    COPY(2,"复印件"),
    ORIGINALOrCOPY(3,"复印件或复印件"),
    PAPER(4,"纸质表格"),
    ELEFILE(5,"普通电子文件"),
    SYS(6,"系统自动获取，无需申请者提交"),
    PERSON(7,"系统自动获取，如数据不全则需申请者提交"),

    ;
    private Integer code;
    private String msg;

    MaterialFormEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
