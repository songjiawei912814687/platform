package com.attendance.domian.output;


import com.attendance.model.AttendanceRule;

public class AttendanceRuleOutput extends AttendanceRule {
    private String statename;

    public String getStatename() {
        if(this.getState()!=null) {
            switch (this.getState()) {
                case 3:
                    statename="停用";
                    break;
                case 1:
                    statename="启用";
                    break;
                default:
                    statename="";
                    break;
            }
        }
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }
}
