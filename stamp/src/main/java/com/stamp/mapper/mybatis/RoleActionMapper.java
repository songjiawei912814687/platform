package com.stamp.mapper.mybatis;

import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.MenuOutput;
import com.stamp.domain.output.RoleActionOutput;
import com.stamp.model.RoleAction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleActionMapper extends MybatisBaseMapper<RoleActionOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(RoleAction record);

    int insertSelective(RoleAction record);

    RoleAction selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleAction record);

    int updateByPrimaryKey(RoleAction record);

    List<RoleActionOutput> findByRoleId(@Param(value = "roleId") Integer roleId);

    List<RoleActionOutput> findByRoleIdIn(List<Integer> ids);

    List<MenuOutput> findMenuByRoleId(@Param(value = "roleId") Integer roleId);

    List<MenuOutput> findMenuByRoleIdIn(List<Integer> ids);
}