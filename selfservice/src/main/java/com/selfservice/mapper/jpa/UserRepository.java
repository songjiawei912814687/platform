package com.selfservice.mapper.jpa;

import com.selfservice.core.base.BaseMapper;
import com.selfservice.model.Users;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseMapper<Users,Integer> {


    Users findByUsername(String username);



}
