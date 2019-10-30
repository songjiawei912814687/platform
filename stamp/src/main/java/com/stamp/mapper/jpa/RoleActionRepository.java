package com.stamp.mapper.jpa;


import com.stamp.core.base.BaseRepository;
import com.stamp.model.RoleAction;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleActionRepository extends BaseRepository<RoleAction,Integer> {
    int deleteAllByRoleId(Integer roleId);
}
