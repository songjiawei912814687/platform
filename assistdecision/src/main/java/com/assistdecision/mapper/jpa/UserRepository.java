package com.assistdecision.mapper.jpa;


import com.assistdecision.core.base.BaseMapper;
import com.assistdecision.model.Users;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends BaseMapper<Users,Integer> {


    Users findByUsername(String username);

    List<Users> findAllByEmployeeId(Integer id);

}
