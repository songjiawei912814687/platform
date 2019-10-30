package com.meeting.mapper.mybatis;

import com.common.model.PageData;
import com.github.pagehelper.Page;
import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.MeetingApplyOutput;
import com.meeting.domain.output.Member;
import com.meeting.model.MeetingApply;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingApplyMapper extends MybatisBaseMapper<MeetingApplyOutput>{
    int deleteByPrimaryKey(Integer id);

    int insert(MeetingApply record);

    int insertSelective(MeetingApply record);

    @Override
    MeetingApplyOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MeetingApply record);

    int updateByPrimaryKey(MeetingApply record);

    List<Member> selectByids(List<Integer> ids);

    Page<MeetingApplyOutput> selectMyMeetingApply(PageData pageData);

    Page<MeetingApplyOutput> selectByRoomId(PageData pageData);

    Page<MeetingApplyOutput> selectOutputByRoomId(Integer meetingRoomId);

    List<Member> selectByIds(List<Integer> ids);
}