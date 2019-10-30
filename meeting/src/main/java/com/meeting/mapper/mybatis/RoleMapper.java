package com.meeting.mapper.mybatis;


import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.RoleOutput;
import com.meeting.model.Role;

public interface RoleMapper extends MybatisBaseMapper<RoleOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    RoleOutput selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}
