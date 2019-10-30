package com.personnel.domian.input;

import com.personnel.model.FoodOrganization;

import java.util.Arrays;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-08  17:20
 * @modified by:
 */
public class FoodOrganiztionInput {

    private FoodOrganization[] SData;

    public FoodOrganization[] getSData() {
        return SData;
    }

    public void setSData(FoodOrganization[] SData) {
        this.SData = SData;
    }


    @Override
    public String toString() {
        return "{" +
                "SData:" + Arrays.toString(SData) +
                '}';
    }


}
