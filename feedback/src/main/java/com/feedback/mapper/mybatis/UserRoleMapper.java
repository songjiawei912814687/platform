package com.feedback.mapper.mybatis;

import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.domain.output.UserRoleOutput;
import com.feedback.model.UserRole;
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
