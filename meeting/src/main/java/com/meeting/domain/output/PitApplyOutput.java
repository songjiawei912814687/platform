package com.meeting.domain.output;

import com.meeting.model.PitApply;

import java.util.List;

public class PitApplyOutput extends PitApply{
    private int isCreator;

    private String roomName;

    private List<Member> memberList;

    private String employeesName;

    private String organizationName;

    public int getIsCreator() {
        return isCreator;
    }

    public void setIsCreator(int isCreator) {
        this.isCreator = isCreator;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getEmployeesName() {
        return employeesName;
    }

    public void setEmployeesName(String employeesName) {
        this.employeesName = employeesName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
