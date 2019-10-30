package com.common.Enum;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-02-18  15:14
 * @modified by:
 */
public enum IsSyncQueueNameEnum {
    UN_SYNC_QUEUE(0,"未下发"),
    BEING_SYNC_QUEUE(1,"正在下发"),
    IS_SYNC_QUEUE(2,"下发完成"),
    ;

    private Integer code;
    private String desc;

    IsSyncQueueNameEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public static String getDesc(Integer code) {
        if(code!=null){
            for(IsSyncQueueNameEnum isSyncQueueNameEnum: IsSyncQueueNameEnum.values()){
                if(code.equals(isSyncQueueNameEnum.getCode())){
                    return isSyncQueueNameEnum.getDesc();
                }
            }
        }
        return "";
    }

    public String getDesc() {
        return desc;
    }
}
