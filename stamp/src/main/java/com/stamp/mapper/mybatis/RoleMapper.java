package com.stamp.mapper.mybatis;

import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.RoleOutput;
import com.stamp.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends MybatisBaseMapper<RoleOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    RoleOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> findByUserId(@Param(value = "createdUserId") Integer createdUserId, @Param(value = "userId") Integer userId);

    List<Role> findByUserIdNotIn(@Param(value = "createdUserId") Integer createdUserId,@Param(value = "userId")Integer userId);
}