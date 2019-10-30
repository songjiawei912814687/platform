package com.meeting.domain.output;

import com.meeting.model.MeetingApply;

import java.util.List;

public class MeetingApplyOutput extends MeetingApply {

    private String employeesName;

    private String organizationName;

    private String roomName;

    //可容纳人数
    private Integer containNumber;

    private String stateName;

    private int isCreator;

    private String isNeedTeaName;

    private String isNeedNeetworkName;

    private String phoneNumber;


    public Integer getContainNumber() {
        return containNumber;
    }

    public void setContainNumber(Integer containNumber) {
        this.containNumber = containNumber;
    }

    public int getIsCreator() {
        return isCreator;
    }

    public void setIsCreator(int isCreator) {
        this.isCreator = isCreator;
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

    public String getRoomName() {

        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getStateName() {
        if(this.getState()!=null){
            switch (getState()){
                case 0:
                    stateName="未审核";
                    break;
                case 1:
                    stateName="审核通过";
                    break;
                case 2:
                    stateName="审核不通过";
                    break;
                default:
                    stateName="";
                    break;
            }
        }
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    private List<Member> memberList;

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

    public String getIsNeedTeaName() {
        if(this.getIsNeedTea()!=null){
            switch (getIsNeedTea()){
                case 0:
                    isNeedTeaName="否";
                    break;
                case 1:
                    isNeedTeaName="是";
                    break;
                default:
                    isNeedTeaName="";
                    break;
            }
        }
        return isNeedTeaName;
    }

    public void setIsNeedTeaName(String isNeedTeaName) {
        this.isNeedTeaName = isNeedTeaName;
    }

    public String getIsNeedNeetworkName() {
        if(this.getIsNeedNeetwork()!=null){
            switch (getIsNeedNeetwork()){
                case 0:
                    isNeedNeetworkName="否";
                    break;
                case 1:
                    isNeedNeetworkName="是";
                    break;
                default:
                    isNeedNeetworkName="";
                    break;
            }
        }
        return isNeedNeetworkName;
    }

    public void setIsNeedNeetworkName(String isNeedNeetworkName) {
        this.isNeedNeetworkName = isNeedNeetworkName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String times;

    public String getTimes() {
        if(getStartDateTime()!=null){
            switch (getStartDateTime()){
                case 0:
                    times="8:00";
                    break;
                case 1:
                    times="8:30";
                    break;
                case 2:
                    times="9:00";
                    break;
                case 3:
                    times="9:30";
                    break;
                case 4:
                    times="10:00";
                    break;
                case 5:
                    times="10:30";
                    break;
                case 6:
                    times="11:00";
                    break;
                case 7:
                    times="11:30";
                    break;
                case 8:
                    times="12:00";
                    break;
                case 9:
                    times="12:30";
                    break;
                case 10:
                    times="13:00";
                    break;
                case 11:
                    times="13:30";
                    break;
                case 12:
                    times="14:00";
                    break;
                case 13:
                    times="14:30";
                    break;
                case 14:
                    times="15:00";
                    break;
                case 15:
                    times="15:30";
                    break;
                case 16:
                    times="16:00";
                    break;
                case 17:
                    times="16:30";
                    break;
                case 18:
                    times="17:00";
                    break;
                case 19:
                    times="17:30";
                    break;
                case 20:
                    times="18:00";
                    break;
                case 21:
                    times="18:30";
                    break;
                case 22:
                    times="19:00";
                    break;
                case 23:
                    times="19:30";
                    break;
            }
        }
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
