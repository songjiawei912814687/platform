package com.assessment.domian.output;

import com.assessment.core.util.AppConsts;
import com.assessment.model.AppraisalIndex;

public class AppraisalIndexOutput extends AppraisalIndex {
    private  String objectName;

    private  String stateName;

    private String typeName;

    public String getStateName() {
        if (getState() != null) {
            if (getState() == AppConsts.start) {
                stateName = "启用";
            } else if (getState() == AppConsts.stop) {
                stateName = "停用";
            }
        }
        return stateName;
    }

    public String getObjectName() {
        if (getObjectType() != null) {
            if (getObjectType() == AppConsts.Window) {
                objectName = "窗口";
            } else if (getObjectType() == AppConsts.StaffMember) {
                objectName = "工作人员";
            }
        }
        return objectName;
    }

    public String getTypeName() {
        if (getObjectType() != null) {
            if (getType() == AppConsts.Dynamic_Index) {
                typeName = "动态效能指标";
            } else if (getType() == AppConsts.Normal_Index) {
                typeName = "常态效能指标";
            }else if(getType() == AppConsts.StaffMember_Index){
                typeName = "工作人员指标";
            }
        }
        return typeName;
    }
}
