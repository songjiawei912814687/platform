package com.knowledge.mapper.mybatis;


import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.RoleActionOutput;
import com.knowledge.model.RoleAction;

public interface RoleActionMapper extends MybatisBaseMapper<RoleActionOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(RoleAction record);

    int insertSelective(RoleAction record);

    RoleActionOutput selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleAction record);

    int updateByPrimaryKey(RoleAction record);
}
