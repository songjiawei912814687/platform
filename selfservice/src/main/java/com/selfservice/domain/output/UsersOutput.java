package com.selfservice.domain.output;

import com.selfservice.model.Users;

public class UsersOutput extends Users {
    private String employeesName;

    private String organizationName;

    private String jobsName;

    private String icon;

    private String userTypeName;

    private Integer jobId;

    private Integer organId;

    private String organName;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getUserTypeName() {
        if(this.getUserType() != null){
            switch (this.getUserType()){
                case 1:
                    return "部门账号";
                case 0:
                    return "个人账号";
            }
        }
        return userTypeName;
    }
    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getJobsName() {
        return jobsName;
    }

    public void setJobsName(String jobsName) {
        this.jobsName = jobsName;
    }

    public String getEmployeesName() {
        return employeesName;
    }

    public void setEmployeesName(String employeesName) {
        this.employeesName = employeesName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }


    public Integer getOrganId() {
        return organId;
    }

    public void setOrganId(Integer organId) {
        this.organId = organId;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }
}
