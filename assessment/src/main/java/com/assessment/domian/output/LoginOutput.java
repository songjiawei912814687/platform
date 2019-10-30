package com.assessment.domian.output;

import java.util.List;
import java.util.Map;

public class LoginOutput {


    private String userName;

    private String organizationName;

    private String jobName;

    private String icon;

    private String employeesName;

    private Integer employeeId;

    private Integer organizationId;

    private Integer jobId;

    private Integer organId;

    private String organName;

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getEmployeesName() {
        return employeesName;
    }

    public void setEmployeesName(String employeesName) {
        this.employeesName = employeesName;
    }

    private List<MenuOutput> menuOutputs;

    private Map<String,List<String>> actions;


    public List<MenuOutput> getMenuOutputs() {
        return menuOutputs;
    }

    public void setMenuOutputs(List<MenuOutput> menuOutputs) {
        this.menuOutputs = menuOutputs;
    }

    public Map<String, List<String>> getActions() {
        return actions;
    }

    public void setActions(Map<String, List<String>> actions) {
        this.actions = actions;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
