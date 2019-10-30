package com.stamp.domain.output;

import com.stamp.enums.ServicePlanEnum;
import com.stamp.model.PrintInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: young
 * @project_name: platform
 * @description:
 * @date: Created in 2019-04-13  13:18
 * @modified by:
 */
@Getter
@Setter
public class PrintInfoOutput extends PrintInfo {

    //服务套餐名称
    private String servicePlanName;

    //刻章单位名称
    private String stampCompanyName;

    public String getServicePlanName() {
        if(getServicePlan()!=null||getServicePlan()!=""){
            servicePlanName = ServicePlanEnum.getDesc(this.getServicePlan());
        }
        return servicePlanName;
    }

}
