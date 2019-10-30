package com.api.mapper.mybatis;

import com.api.domain.output.MeetingRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRoomMapper {
    MeetingRoom selectByIp(String ip);
}