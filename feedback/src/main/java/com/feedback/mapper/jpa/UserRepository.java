package com.feedback.mapper.jpa;

import com.feedback.core.base.BaseMapper;
import com.feedback.model.Users;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseMapper<Users,Integer> {


    Users findByUsername(String username);



}
