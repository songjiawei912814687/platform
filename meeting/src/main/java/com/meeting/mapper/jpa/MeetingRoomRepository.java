package com.meeting.mapper.jpa;


import com.meeting.core.base.BaseMapper;
import com.meeting.model.MeetingRoom;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRoomRepository extends BaseMapper<MeetingRoom,Integer> {
}
