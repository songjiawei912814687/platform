package com.common.Enum;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author: Young
 * @description: 短信类型
 * @date: Created in 19:54 2018/10/25
 * @modified by:
 */
@Getter
@NoArgsConstructor
public enum MessageTypeEnum {

    WINDOW_VISIT(1,"窗口回访"),
    APPROVAL(2,"审批类型"),
    WINDOW_APPRAISE(3,"窗口评价"),
    MEET_REMINDER(4,"会议提醒"),
    INFO_DEPART_MANAGER(5,"通知部门管理员"),
    WINDOW_RESEND(6,"窗口再次评价"),
    WINDOW_RE_VISIT(7,"窗口再次回访"),
    MANUAL_TYPE(99,"手动发送"),

    ;

    private Integer code;
    private String msg;


    public static String getMsg(Integer code){

        for(var e : MessageTypeEnum.values()){
            if(e.getCode().equals(code)){
                return e.getMsg();
            }
        }
        return "";
    }
    MessageTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
