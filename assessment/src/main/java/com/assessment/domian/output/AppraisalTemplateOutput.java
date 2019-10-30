package com.assessment.domian.output;

import com.assessment.core.util.AppConsts;
import com.assessment.model.AppraisalTemplate;

public class AppraisalTemplateOutput extends AppraisalTemplate {

    //状态
    private  String stateName;
    //模板类型
    private String typeName;
    //对象类型
    private String objectTypeName;
    //对象名称以，隔开
    private  String objectNameList;

    private String objectIdList;

    private  String organizationName;

    private  String roleName;

    private Integer employeeId;

    private String employeeName;

    private  Integer organizationId;

    private Integer roleId;

    public String getStateName() {
        if (getState() != null) {
            if (getState() == AppConsts.start) {
                stateName = "启用";
            } else if (getState() == AppConsts.StaffMember) {
                stateName = "停用";
            }
        }
        return stateName;
    }

    public String getTypeName() {
        if (getType() != null) {
            if (getType() == AppConsts.Annual_Assessment) {
                typeName = "年度考核";
            } else if (getType() == AppConsts.Monthly_Assessment) {
                typeName = "月度考核";
            }
        }
        return typeName;
    }

    public String getObjectTypeName() {
        if (getObjectType() != null) {
            if (getObjectType() == AppConsts.Window) {
                objectTypeName = "窗口";
            } else if (getObjectType() == AppConsts.StaffMember) {
                objectTypeName = "工作人员";
            }
        }
        return objectTypeName;
    }

    public String getObjectNameList() {
        return objectNameList;
    }

    public void setObjectNameList(String objectNameList) {
        this.objectNameList = objectNameList;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getObjectIdList() {
        return objectIdList;
    }

    public void setObjectIdList(String objectIdList) {
        this.objectIdList = objectIdList;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
