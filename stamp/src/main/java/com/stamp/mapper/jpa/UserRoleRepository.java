package com.stamp.mapper.jpa;


import com.stamp.core.base.BaseRepository;
import com.stamp.model.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends BaseRepository<UserRole,Integer> {

    List<UserRole> findAllByUserId(Integer userId);

    int deleteAllByUserIdAndRoleId(Integer userId, Integer roleId);

    int deleteByRoleId(Integer roleId);
}
