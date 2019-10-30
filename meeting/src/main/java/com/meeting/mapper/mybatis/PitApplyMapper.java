package com.meeting.mapper.mybatis;

import com.common.model.PageData;
import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.MeetingApplyOutput;
import com.meeting.domain.output.PitApplyOutput;
import com.meeting.model.PitApply;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PitApplyMapper extends MybatisBaseMapper<PitApplyOutput>{
    int deleteByPrimaryKey(Integer id);

    int insert(PitApply record);

    int insertSelective(PitApply record);

    @Override
    PitApplyOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PitApply record);

    int updateByPrimaryKey(PitApply record);

    List<PitApplyOutput> selectByRoomId(PageData pageData);
}