package com.message.mapper.mybatis;

import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.ActionOutput;
import com.message.model.Action;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionMapper extends MybatisBaseMapper<ActionOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(Action record);

    int insertSelective(Action record);

    ActionOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Action record);

    int updateByPrimaryKey(Action record);

    List<ActionOutput> findByRoles(List<Integer> ids);
}
