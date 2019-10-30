package com.knowledge.mapper.jpa;

import com.knowledge.core.base.BaseMapper;
import com.knowledge.model.Action;
import com.knowledge.model.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends BaseMapper<UserRole,Integer> {

    public List<UserRole> findAllByUserId(Integer userId);

    public int deleteAllByUserIdAndRoleId(Integer userId, Integer roleId);
}
