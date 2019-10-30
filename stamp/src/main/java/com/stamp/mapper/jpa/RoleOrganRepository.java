package com.stamp.mapper.jpa;


import com.stamp.core.base.BaseRepository;
import com.stamp.model.RoleOrgan;

import java.util.List;


public interface RoleOrganRepository extends BaseRepository<RoleOrgan,Integer> {
    List<RoleOrgan> findAllByRoleId(Integer roleId);

    int deleteAllByRoleId(Integer roleId);
}
