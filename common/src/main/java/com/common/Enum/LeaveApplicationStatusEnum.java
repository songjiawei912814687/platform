package com.common.Enum;

/**
 * @author: Young
 * @description:
 * @date: Created in 21:14 2018/9/16
 * @modified by:
 */
public enum LeaveApplicationStatusEnum {

        TEMPORARY_OUT_BOUT_LEAVE(1,"临时外出二小时公事"),
        BREASTFEEDING_LEAVE(2,"哺乳假"),
        MATERNITY_LEAVE(4,"产假"),
        ANNUAL_LEAVE(5,"年休假"),
        BOUT_LEAVE(6,"因公外出"),
        PERSONAL_LEAVE(7,"事假"),
        SICK_LEAVE(8,"病假"),
        OTHER_LEAVE(9,"其他"),
        TEMPORARY_OUT_PRIVATE_LEAVE(10,"临时外出二小时私事"),
        WEEDING_LEAVE(11,"婚假"),
        VISIT_LEAVE(12,"探亲假"),
        PATERNITY_LEAVE(13,"陪产假"),
        FUNERAL_LEAVE(14,"丧假"),
        ;

        private Integer code;
        private String msg;

        LeaveApplicationStatusEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public static String getByCodeMsg(Integer code){
           for(var e : LeaveApplicationStatusEnum.values()){
                   if(e.code.equals(code)){
                           return e.msg;
                   }
           }
           return null;
        }

    public static void main(String[] args) {
        System.out.println(getByCodeMsg(1));
    }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
}
