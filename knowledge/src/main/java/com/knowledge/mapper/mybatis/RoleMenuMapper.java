package com.knowledge.mapper.mybatis;

import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.RoleMenuOutput;
import com.knowledge.model.RoleMenu;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMenuMapper extends MybatisBaseMapper<RoleMenuOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleMenu record);

    int insertSelective(RoleMenu record);

    @Override
    RoleMenuOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleMenu record);

    int updateByPrimaryKey(RoleMenu record);
}
