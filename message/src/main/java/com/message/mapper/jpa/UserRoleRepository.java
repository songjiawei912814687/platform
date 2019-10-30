package com.message.mapper.jpa;

import com.message.core.base.BaseMapper;
import com.message.model.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends BaseMapper<UserRole,Integer> {

    public List<UserRole> findAllByUserId(Integer userId);

    public int deleteAllByUserIdAndRoleId(Integer userId, Integer roleId);
}
