package com.personnel.domian.input;

import com.personnel.model.FoodEmployees;

import java.util.Arrays;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-08  20:48
 * @modified by:
 */
public class FoodEmployeesInput {

    private FoodEmployees[] SData;

    public FoodEmployees[] getSData() {
        return SData;
    }

    public void setSData(FoodEmployees[] SData) {
        this.SData = SData;
    }

    @Override
    public String toString() {
        return "{" + "\r\n"+
                "SData:" + Arrays.toString(SData) +"\r\n"+
                '}';
    }

}
