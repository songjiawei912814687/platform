package com.attendance.mapper.mybatis;


import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.RoleActionOutput;
import com.attendance.model.RoleAction;

public interface RoleActionMapper extends MybatisBaseMapper<RoleActionOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(RoleAction record);

    int insertSelective(RoleAction record);

    RoleActionOutput selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleAction record);

    int updateByPrimaryKey(RoleAction record);
}
