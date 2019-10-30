package com.meeting.domain.input;


import java.util.List;

public class PermissionsInput {
    private Integer roomId;

    private List<RoleMenuInput> roleMenuIntputs;

    public List<RoleMenuInput> getRoleMenuIntputs() {
        return roleMenuIntputs;
    }


    public void setRoleMenuIntputs(List<RoleMenuInput> roleMenuIntputs) {
        this.roleMenuIntputs = roleMenuIntputs;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}
