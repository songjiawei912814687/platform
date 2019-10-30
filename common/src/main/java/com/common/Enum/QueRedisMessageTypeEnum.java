package com.common.Enum;

/**
 * @author: young
 * @project_name: svn
 * @description: 排队叫号系统消息类型
 * @date: Created in 2018-12-12  16:03
 * @modified by:
 */
public enum QueRedisMessageTypeEnum {
    USER_LOGIN(1,"用户登录"),
    USER_LOGOUT(2,"用户注销"),
    WAIT_QUEUE(4,"等候着入队"),
    WAIT_POP(8,"等候着出队"),
    CALL_WAIT(16,"呼叫等待着"),
    START_BUSINESS(32,"开始办理业务"),
    COMPLETE_BUSINESS(64,"完成办理业务"),
    SUBMIT_COMMENTS(128,"提交评论结果"),
    PAUSE_SERVICE(256,"暂停服务"),
    REFRESH_WAIT(512,"刷新等候列表"),
    START_FEEDBACK(1024,"开始评价动作"),
    CANCEL_PAUSE_SERVICE(8192,"取消暂停服务"),
    PASS_NUMBER(8388608,"过号"),
    CANCEL_QUEUE(33554432,"取消排队"),
    WAIT_CALL_NUMBER(67108864,"等候着叫号"),

    ;

    private Integer code;
    private String desc;

    QueRedisMessageTypeEnum(Integer code, String desc) {
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
