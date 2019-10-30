package com.message.mapper.jpa;

import com.message.core.base.BaseMapper;
import com.message.model.Users;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseMapper<Users,Integer> {


    Users findByUsername(String username);



}
