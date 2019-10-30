package com.personnel.domian.output;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-18  19:15
 * @modified by:
 */
public class FoodEmployeesOutput {

    private String sID;

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    @Override
    public String toString() {
        return "{" +
                "sID:'" + sID + '\'' +
                '}';
    }
}
