package com.personnel.domian.output;


import com.personnel.model.JobChange;

public class JobChangeOutput extends JobChange {
    private String employeesname;

    public String getEmployeesname() {
        return employeesname;
    }

    public void setEmployeesname(String employeesname) {
        this.employeesname = employeesname;
    }

    private String stateName;

    public String getStateName() {
        if(getState()!=null){
            switch (getState()){
                case 0:
                    stateName="未审核";
                    break;
                case 1:
                    stateName="审核通过";
                    break;
                case 2:
                    stateName="审核不通过";
                    break;
                default:
                    break;
            }
        }
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
