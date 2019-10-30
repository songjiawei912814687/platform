package com.attendance.domian.output;


import java.util.Date;

public class AttendancePunch {
    //员工Id
    private  Integer employeeId;
    //最迟上班时间
    private Date latestWorkingTime;
    //最早下班时间
    private Date firstOffWorkTime;


    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Date getLatestWorkingTime() {
        return latestWorkingTime;
    }

    public void setLatestWorkingTime(Date latestWorkingTime) {
        this.latestWorkingTime = latestWorkingTime;
    }

    public Date getFirstOffWorkTime() {
        return firstOffWorkTime;
    }

    public void setFirstOffWorkTime(Date firstOffWorkTime) {
        this.firstOffWorkTime = firstOffWorkTime;
    }

}
