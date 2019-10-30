package com.feedback.service;

import com.feedback.core.base.BaseMapper;
import com.feedback.core.base.BaseService;
import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.domain.output.UsersOutput;
import com.feedback.mapper.jpa.UserRepository;
import com.feedback.mapper.mybatis.UsersMapper;
import com.feedback.model.Users;
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
