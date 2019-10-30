package com.selfservice.mapper.jpa;

import com.selfservice.core.base.BaseMapper;
import com.selfservice.model.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends BaseMapper<UserRole,Integer> {

    public List<UserRole> findAllByUserId(Integer userId);

    public int deleteAllByUserIdAndRoleId(Integer userId, Integer roleId);
}
