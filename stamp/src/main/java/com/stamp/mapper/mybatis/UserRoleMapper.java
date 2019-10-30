package com.stamp.mapper.mybatis;

import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.UserRoleOutput;
import com.stamp.model.UserRole;
import org.apache.ibatis.annotations.Param;

public interface UserRoleMapper extends MybatisBaseMapper<UserRoleOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

    Integer deleteAllByUserIdAndRoleId(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
}