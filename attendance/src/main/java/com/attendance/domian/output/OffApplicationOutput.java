package com.attendance.domian.output;


import com.attendance.model.OffApplication;
import com.common.Enum.CheckStatusEnum;

public class OffApplicationOutput extends OffApplication {

    private String organizationName;

    private String employeesName;

    private String statusName;


    public String getStatusName() {
        if(getStatus()!=null){
            return statusName = CheckStatusEnum.getMsg(getStatus());
        }
        return "";
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
}
