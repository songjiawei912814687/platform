package com.personnel.mapper.jpa;

import com.personnel.core.base.BaseMapper;
import com.personnel.model.Users;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseMapper<Users,Integer> {


    Users findByUsername(String username);



}
