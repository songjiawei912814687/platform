package com.assessment.domian.output;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AttendanceDailyDate {
    private Integer employeeId;

    //迟到次数
    private Integer beLateTimes;

    //早退次数
    private Integer leaveEarlyTimes;

    //未打卡次数
    private Integer punchTimes;

    private Integer total;

    private Integer totalHours;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getBeLateTimes() {
        return beLateTimes;
    }

    public void setBeLateTimes(Integer beLateTimes) {
        this.beLateTimes = beLateTimes;
    }

    public Integer getLeaveEarlyTimes() {
        return leaveEarlyTimes;
    }

    public void setLeaveEarlyTimes(Integer leaveEarlyTimes) {
        this.leaveEarlyTimes = leaveEarlyTimes;
    }

    public Integer getPunchTimes() {
        return punchTimes;
    }

    public void setPunchTimes(Integer punchTimes) {
        this.punchTimes = punchTimes;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }
}
