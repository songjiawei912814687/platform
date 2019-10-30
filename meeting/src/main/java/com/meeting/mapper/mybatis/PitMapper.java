package com.meeting.mapper.mybatis;

import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.PitOutput;
import com.meeting.model.Pit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PitMapper extends MybatisBaseMapper<PitOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(Pit record);

    int insertSelective(Pit record);

    @Override
    PitOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Pit record);

    int updateByPrimaryKey(Pit record);

    List<PitOutput> selectByName(String name);

    List<PitOutput> selectByIp(String ip);
}