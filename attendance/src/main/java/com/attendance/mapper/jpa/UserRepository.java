package com.attendance.mapper.jpa;

import com.attendance.core.base.BaseMapper;
import com.attendance.model.Users;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseMapper<Users,Integer> {


    Users findByUsername(String username);


    Users findByEmployeeId(Integer employeeId);
}
