package com.stamp.domain.input;

public class UserRoleInput {
    private Integer roleId;

    private String userIdList;

    private Integer userId;

    private String roleIdList;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(String roleIdList) {
        this.roleIdList = roleIdList;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(String userIdList) {
        this.userIdList = userIdList;
    }
}
