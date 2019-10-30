package com.api.domain.output;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class MeetingApply {
    private String organizationName;
    private String employeesName;
    private String theme;
    private String roomName;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date appointmentDate;
    private String time;
    private Integer endDateTime;
    private Integer startDateTime;
    private Integer meetingRoomId;
    private String hostUnit;
    public String realStartTime;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getEmployeesName() {
        return employeesName;
    }

    public void setEmployeesName(String employeesName) {
        this.employeesName = employeesName;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getTime() {
        int a=getStartDateTime();
        int b=getEndDateTime();
        time=times(getStartDateTime())+"-"+times(getEndDateTime()+1);
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Integer startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Integer getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Integer endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Integer getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Integer meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public String times(Integer a){
        String times=null;
        if(a!=null) {
            switch (a) {
                case 0:
                    times = "8:00";
                    break;
                case 1:
                    times = "8:30";
                    break;
                case 2:
                    times = "9:00";
                    break;
                case 3:
                    times = "9:30";
                    break;
                case 4:
                    times = "10:00";
                    break;
                case 5:
                    times = "10:30";
                    break;
                case 6:
                    times = "11:00";
                    break;
                case 7:
                    times = "11:30";
                    break;
                case 8:
                    times = "12:00";
                    break;
                case 9:
                    times = "12:30";
                    break;
                case 10:
                    times = "13:00";
                    break;
                case 11:
                    times = "13:30";
                    break;
                case 12:
                    times = "14:00";
                    break;
                case 13:
                    times = "14:30";
                    break;
                case 14:
                    times = "15:00";
                    break;
                case 15:
                    times = "15:30";
                    break;
                case 16:
                    times = "16:00";
                    break;
                case 17:
                    times = "16:30";
                    break;
                case 18:
                    times = "17:00";
                    break;
                case 19:
                    times = "17:30";
                    break;
                case 20:
                    times = "18:00";
                    break;
                case 21:
                    times = "18:30";
                    break;
                case 22:
                    times = "19:00";
                    break;
                case 23:
                    times = "19:30";
                    break;
            }
        }
        return times;
     }

    public String getHostUnit() {
        return hostUnit;
    }

    public void setHostUnit(String hostUnit) {
        this.hostUnit = hostUnit;
    }

    public String getRealStartTime() {
        return realStartTime;
    }

    public void setRealStartTime(String realStartTime) {
        this.realStartTime = realStartTime;
    }
}
