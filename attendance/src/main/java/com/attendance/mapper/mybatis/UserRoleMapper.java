package com.attendance.mapper.mybatis;

import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.UserRoleOutput;
import com.attendance.model.UserRole;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMapper extends MybatisBaseMapper<UserRoleOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    @Override
    UserRoleOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);
}
