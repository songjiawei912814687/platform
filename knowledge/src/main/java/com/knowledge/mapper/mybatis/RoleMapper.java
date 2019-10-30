package com.knowledge.mapper.mybatis;


import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.RoleOutput;
import com.knowledge.model.Role;

public interface RoleMapper extends MybatisBaseMapper<RoleOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    RoleOutput selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}
