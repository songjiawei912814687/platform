package com.api.mapper.mybatis;

import com.api.domain.output.MeetingRoom;
import org.springframework.stereotype.Repository;

@Repository
public interface PitMapper {
    MeetingRoom selectByIp(String ip);
}
