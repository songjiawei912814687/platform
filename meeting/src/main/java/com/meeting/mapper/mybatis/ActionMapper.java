package com.meeting.mapper.mybatis;

import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.ActionOutput;
import com.meeting.model.Action;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionMapper extends MybatisBaseMapper<ActionOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(Action record);

    int insertSelective(Action record);

    ActionOutput selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Action record);

    int updateByPrimaryKey(Action record);

    List<ActionOutput> findByRoles(List<Integer> ids);
}
