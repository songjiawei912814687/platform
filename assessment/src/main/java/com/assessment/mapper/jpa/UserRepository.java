package com.assessment.mapper.jpa;

import com.assessment.core.base.BaseMapper;
import com.assessment.model.Users;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends BaseMapper<Users,Integer> {


    Users findByUsername(String username);

    List<Users> findAllByEmployeeId(Integer id);

}
