package com.attendance.domian.output;


import com.attendance.model.OvertimeApplication;
import com.common.Enum.CheckStatusEnum;

import java.util.Date;

public class OvertimeApplicationOutput extends OvertimeApplication {

    private String organizationName;

    private String employeesName;

    private String statusName;

    private String bankCardNumber;

    private Date restTime;

    private String employeeNo;

    public String getStatusName() {
        if(getStatus()!=null){
            return CheckStatusEnum.getMsg(this.getStatus());
        }
        return statusName;
    }

    public Date getRestTime() {
        return restTime;
    }

    public void setRestTime(Date restTime) {
        this.restTime = restTime;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getEmployeesName() {
        return employeesName;
    }

    public void setEmployeesName(String employeesName) {
        this.employeesName = employeesName;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }
}
