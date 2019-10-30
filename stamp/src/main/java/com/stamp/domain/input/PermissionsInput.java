package com.stamp.domain.input;


import java.util.List;

public class PermissionsInput {
    private Integer roleId;

    private List<RoleMenuInput> roleMenuIntputs;

    public List<RoleMenuInput> getRoleMenuIntputs() {
        return roleMenuIntputs;
    }


    public void setRoleMenuIntputs(List<RoleMenuInput> roleMenuIntputs) {
        this.roleMenuIntputs = roleMenuIntputs;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
