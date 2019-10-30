package com.common.Enum;


/**
 * @author: Young
 * @description: 审核状态枚举
 * @date: Created in 11:36 2018/9/15
 * @modified by:
 */
public enum  CheckStatusEnum {

    UN_CHECK(0,"待审核"),
    CHECK_FINISH(1,"审核通过"),
    CHECK_UN_PASS(3,"审核未通过"),
    CANCEL(5,"已撤销")
    ;
    private Integer code;
    private String msg;

    CheckStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public static String getMsg(Integer code) {
        for(var e:CheckStatusEnum.values()){
            if(e.getCode().equals(code)){
                return e.getMsg();
            }
        }
        return "";
    }
    public String getMsg() {
        return msg;
    }

    public static void main(String[] args) {
        String desc = getMsg(0);
        System.out.println(desc);
    }

}
