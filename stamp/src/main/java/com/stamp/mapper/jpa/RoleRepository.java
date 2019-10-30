package com.stamp.mapper.jpa;


import com.stamp.core.base.BaseRepository;
import com.stamp.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends BaseRepository<Role, Integer> {
    List<Role> findAllByDefaultPermissions(Integer defaultPermissions);
}
