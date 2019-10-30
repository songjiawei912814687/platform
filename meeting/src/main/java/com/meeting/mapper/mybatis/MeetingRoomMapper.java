package com.meeting.mapper.mybatis;

import com.common.model.PageData;
import com.github.pagehelper.Page;
import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.MeetingRoomOutput;
import com.meeting.model.MeetingRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRoomMapper extends MybatisBaseMapper<MeetingRoomOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(MeetingRoom record);

    int insertSelective(MeetingRoom record);

    @Override
    MeetingRoomOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MeetingRoom record);

    int updateByPrimaryKey(MeetingRoom record);

    List<MeetingRoomOutput> selectByName(String name);

    Page<MeetingRoomOutput> selectByTime(PageData pageData);

    List<MeetingRoomOutput> selectByIp(String ip);
}