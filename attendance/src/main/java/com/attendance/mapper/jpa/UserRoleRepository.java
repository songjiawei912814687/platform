package com.attendance.mapper.jpa;

import com.attendance.core.base.BaseMapper;
import com.attendance.model.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends BaseMapper<UserRole,Integer> {

    public List<UserRole> findAllByUserId(Integer userId);

    public int deleteAllByUserIdAndRoleId(Integer userId, Integer roleId);
}
