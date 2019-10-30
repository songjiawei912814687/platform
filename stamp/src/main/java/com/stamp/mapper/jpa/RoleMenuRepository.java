package com.stamp.mapper.jpa;


import com.stamp.core.base.BaseRepository;
import com.stamp.model.RoleMenu;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMenuRepository extends BaseRepository<RoleMenu,Integer> {
    int deleteAllByRoleId(Integer roleId);
}
