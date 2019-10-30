package com.attendance.mapper.mybatis;

import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.RoleMenuOutput;
import com.attendance.model.RoleMenu;
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
