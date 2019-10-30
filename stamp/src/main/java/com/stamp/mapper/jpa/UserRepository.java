package com.stamp.mapper.jpa;


import com.stamp.core.base.BaseRepository;
import com.stamp.model.Users;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends BaseRepository<Users,Integer> {


    Users findByUsername(String username);

    List<Users> findAllByEmployeeId(Integer id);

    List<Users> findAllByOrganizationIdAndUserType(Integer organId, Integer userType);

}
