package com.knowledge.mapper.jpa;

import com.knowledge.core.base.BaseMapper;
import com.knowledge.model.Action;
import com.knowledge.model.Users;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseMapper<Users,Integer> {


    Users findByUsername(String username);


}
