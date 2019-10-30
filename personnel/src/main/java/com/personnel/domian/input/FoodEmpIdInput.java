package com.personnel.domian.input;

import com.personnel.domian.output.FoodEmployeesOutput;

import java.util.Arrays;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-18  18:20
 * @modified by:
 */
public class FoodEmpIdInput {

    private FoodEmployeesOutput[] SData;

    public FoodEmployeesOutput[] getSData() {
        return SData;
    }

    public void setSData(FoodEmployeesOutput[] SData) {
        this.SData = SData;
    }

    @Override
    public String toString() {
        return "{" +
                "SData:" + Arrays.toString(SData) +
                '}';
    }
}
