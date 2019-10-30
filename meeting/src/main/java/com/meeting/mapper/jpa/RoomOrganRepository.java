package com.meeting.mapper.jpa;


import com.meeting.core.base.BaseMapper;
import com.meeting.model.RoomOrgan;

import java.util.List;


public interface RoomOrganRepository extends BaseMapper<RoomOrgan,Integer> {
    List<RoomOrgan> findAllByRoomId(Integer roleId);

    int deleteAllByRoomId(Integer roleId);
}
