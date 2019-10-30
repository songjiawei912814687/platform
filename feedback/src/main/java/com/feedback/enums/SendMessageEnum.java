package com.feedback.enums;

/**
 * @author: Young
 * @description:
 * @date: Created in 12:59 2018/10/24
 * @modified by:
 */
public enum SendMessageEnum {

    SUCCESS(1,"短信发送成功"),
    CONTENT_NULL(101,"短信内容为空"),
    NUMBER_NULL(102,"号码数组为空"),
    NUMBER_ARRAY_NULL(103,"号码数组为空数组"),
    ILLEGAL_NUMBER(104,"批次短信的号码中存在非法号码， SDK带有号码的验证处理"),
    NO_AUTHENTICATION(105,"未未进行身份认证或认证失败，用户请确认输入的用户名，密码和企业名是否正确"),
    NET_SIGN_NO(106,"网关签名为空， 用户需要填写网关签名编号"),
    OTHER_ERROR(107,"其它错误"),
    JMS_ERROR(108,"JMS异常， 需要联系移动集团维护人员"),
    REPETITION_NUMBER(109,"批次短信号码中存在重复号码"),
    ;

    private Integer code;
    private String desc;

    SendMessageEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(Integer code){

        for(var e : SendMessageEnum.values()){
            if(e.getCode().equals(code)){
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
