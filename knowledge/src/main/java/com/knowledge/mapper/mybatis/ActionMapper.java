package com.knowledge.mapper.mybatis;

import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.ActionOutput;
import com.knowledge.model.Action;
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
