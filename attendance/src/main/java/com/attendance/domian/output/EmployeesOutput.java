package com.attendance.domian.output;


import com.attendance.model.Employees;

public class EmployeesOutput extends Employees {
    private String jobsName;

    private  String organizationName;

    private  String windowName;

    public String getJobsName() {
        return jobsName;
    }

    public void setJobsName(String jobsName) {
        this.jobsName = jobsName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getWindowName() {
        return windowName;
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }

    private  String stateName;

    public String getStateName() {

        return stateName;
    }

    private String partyMemberStateName;

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    //1中共党员、2中共预备党员、3共青团员、4民主党派、5群众﻿
    public String getPartyMemberStateName() {
        if (getPartyMemberState() != null) {
           switch (getPartyMemberState()){
               case 1:
                   partyMemberStateName="中共党员";
                   break;
               case 2:
                   partyMemberStateName="中共预备党员";
                   break;
               case 3:
                   partyMemberStateName="共青团员";
                   break;
               case 4:
                   partyMemberStateName="民主党派";
                   break;
               case 5:
                   partyMemberStateName="群众";
                   break;
               default:
                   break;
           }
        }
        return partyMemberStateName;
    }

    public void setPartyMemberStateName(String partyMemberStateName) {
        this.partyMemberStateName = partyMemberStateName;
    }

    private String reserveCadresStateName;

    public String getReserveCadresStateName() {
        if (getReserveCadresState() != null) {
            switch (getReserveCadresState()){
                case 0:
                    reserveCadresStateName="否";
                    break;
                case 1:
                    reserveCadresStateName="是";
                    break;
                default:
                    break;
            }
        }
        return reserveCadresStateName;
    }

    public void setReserveCadresStateName(String reserveCadresStateName) {
        this.reserveCadresStateName = reserveCadresStateName;
    }

    private String windowStateName;

    public String getWindowStateName() {
        if (getWindowState() != null) {
            switch (getWindowState()){
                case 0:
                    windowStateName="否";
                    break;
                case 1:
                    windowStateName="是";
                    break;
                default:
                    break;
            }
        }
        return windowStateName;
    }

    public void setWindowStateName(String windowStateName) {
        this.windowStateName = windowStateName;
    }

    private String attendanceStateName;

    public String getAttendanceStateName() {
        if (getAttendanceState() != null) {
            switch (getAttendanceState()){
                case 0:
                    attendanceStateName="否";
                    break;
                case 1:
                    attendanceStateName="是";
                    break;
                default:
                    break;
            }
        }
        return attendanceStateName;
    }

    public void setAttendanceStateName(String attendanceStateName) {
        this.attendanceStateName = attendanceStateName;
    }

    private String recordOfFormalSchoolingName;

    public String getRecordOfFormalSchoolingName() {
        if (getRecordOfFormalSchooling() != null) {
            switch (getRecordOfFormalSchooling()){
                case 0:
                    recordOfFormalSchoolingName="博士";
                    break;
                case 1:
                    recordOfFormalSchoolingName="硕士";
                    break;
                case 2:
                    recordOfFormalSchoolingName="本科";
                    break;
                case 3:
                    recordOfFormalSchoolingName="大专";
                    break;
                case 4:
                    recordOfFormalSchoolingName="中专";
                    break;
                case 5:
                    recordOfFormalSchoolingName="高中";
                    break;
                case 6:
                    recordOfFormalSchoolingName="初中";
                    break;
                case 7:
                    recordOfFormalSchoolingName="小学";
                    break;
                default:
                    break;
            }
        }
        return recordOfFormalSchoolingName;
    }

    public void setRecordOfFormalSchoolingName(String recordOfFormalSchoolingName) {
        this.recordOfFormalSchoolingName = recordOfFormalSchoolingName;
    }

    private String userCompileName;

    public String getUserCompileName() {
        return userCompileName;
    }
//0-国家机关人员编制、1-国家事业单位人员编制、2-国家企业单位人员编制、3-编外人员﻿
    public void setUserCompileName(String userCompileName) {
        if (getUserCompile() != null) {
            switch (getUserCompile()){
                case 0:
                    userCompileName="国家机关人员编制";
                    break;
                case 1:
                    userCompileName="国家事业单位人员编制";
                    break;
                case 2:
                    userCompileName="国家企业单位人员编制";
                    break;
                case 3:
                    userCompileName="编外人员";
                    break;
                default:
                    break;
            }
        }
        this.userCompileName = userCompileName;
    }
}
