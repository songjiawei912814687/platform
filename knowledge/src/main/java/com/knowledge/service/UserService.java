package com.knowledge.service;

import com.knowledge.core.base.BaseMapper;
import com.knowledge.core.base.BaseService;
import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.UsersOutput;
import com.knowledge.mapper.jpa.UserRepository;
import com.knowledge.mapper.mybatis.UsersMapper;
import com.knowledge.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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




}
