package com.attendance.domian.output;

import com.attendance.model.AttendanceRuleNew;

/**
 * @author: young
 * @project_name: assist-decision
 * @description:
 * @date: Created in 2019-03-13  11:19
 * @modified by:
 */
public class AttendanceRuleNewOutput extends AttendanceRuleNew {

    private String stateName;

    public String getStateName() {
        if(this.getState()!=null) {
            switch (this.getState()) {
                case 3:
                    stateName="停用";
                    break;
                case 1:
                    stateName="启用";
                    break;
                default:
                    stateName="";
                    break;
            }
        }
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
