package com.personnel.mapper.mybatis;


import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.RoleActionOutput;
import com.personnel.model.RoleAction;

public interface RoleActionMapper extends MybatisBaseMapper<RoleActionOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(RoleAction record);

    int insertSelective(RoleAction record);

    RoleActionOutput selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleAction record);

    int updateByPrimaryKey(RoleAction record);
}
