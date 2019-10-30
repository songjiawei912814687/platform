package com.meeting.domain.output;

import java.util.List;

public class MeetingData {
    private String date;

    private List<MeetingRoomOutput> roomList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<MeetingRoomOutput> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<MeetingRoomOutput> roomList) {
        this.roomList = roomList;
    }
}
