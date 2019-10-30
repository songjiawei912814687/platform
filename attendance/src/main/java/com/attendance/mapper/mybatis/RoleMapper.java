package com.attendance.mapper.mybatis;


import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.RoleOutput;
import com.attendance.model.Role;

public interface RoleMapper extends MybatisBaseMapper<RoleOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    RoleOutput selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}
