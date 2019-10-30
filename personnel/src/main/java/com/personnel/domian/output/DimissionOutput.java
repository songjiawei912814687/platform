package com.personnel.domian.output;

import com.personnel.model.Dimission;

public class DimissionOutput extends Dimission {
    private String employeeName;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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
}
