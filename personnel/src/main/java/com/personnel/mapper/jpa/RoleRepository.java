package com.personnel.mapper.jpa;

import com.personnel.core.base.BaseMapper;
import com.personnel.model.Role;

import java.util.List;

public interface RoleRepository extends BaseMapper<Role,Integer> {
    List<Role> findAllByDefaultPermissions(Integer defaultPermissions);
}
