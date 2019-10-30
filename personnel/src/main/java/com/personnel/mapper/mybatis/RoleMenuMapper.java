package com.personnel.mapper.mybatis;

import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.RoleMenuOutput;
import com.personnel.model.RoleMenu;
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
