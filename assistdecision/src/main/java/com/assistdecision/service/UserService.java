package com.assistdecision.service;


import com.assistdecision.core.base.BaseMapper;
import com.assistdecision.core.base.BaseService;
import com.assistdecision.core.base.MybatisBaseMapper;
import com.assistdecision.domain.output.UsersOutput;
import com.assistdecision.mapper.jpa.UserRepository;
import com.assistdecision.mapper.mybatis.UsersMapper;
import com.assistdecision.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService extends BaseService<UsersOutput, Users,Integer> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public BaseMapper<Users,Integer> getMapper() {
        return userRepository;
    }

    @Override
    public MybatisBaseMapper<UsersOutput> getMybatisBaseMapper() {
        return usersMapper;
    }


    public Users getByUserName(String name){
        Users users = new Users();
//        Sort sort = new Sort(Sort.Direction.ASC, "id");
//        Pageable p = new PageRequest(1,2,sort);
        return userRepository.findByUsername(name);
    }

    public List<Users> getByEmployeeId(Integer id){
        return userRepository.findAllByEmployeeId(id);
    }

    public UsersOutput selectByEmployeeId(Integer employeeId) {
       return usersMapper.selectByEmployeeId(employeeId);
    }
}
