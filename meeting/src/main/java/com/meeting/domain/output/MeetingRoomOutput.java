package com.meeting.domain.output;


import com.meeting.model.MeetingRoom;

import java.util.List;

public class MeetingRoomOutput extends MeetingRoom {
    private List<MeetingApplyOutput> meetingList;

    public List<MeetingApplyOutput> getMeetingList() {
        return meetingList;
    }

    public void setMeetingList(List<MeetingApplyOutput> meetingList) {
        this.meetingList = meetingList;
    }
}
