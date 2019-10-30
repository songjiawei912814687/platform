package com.assistdecision.service;


import com.assistdecision.core.base.BaseMapper;
import com.assistdecision.core.base.BaseService;
import com.assistdecision.core.base.MybatisBaseMapper;
import com.assistdecision.domain.output.UserRoleOutput;
import com.assistdecision.mapper.jpa.UserRoleRepository;
import com.assistdecision.mapper.mybatis.UserRoleMapper;
import com.assistdecision.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService extends BaseService<UserRoleOutput, UserRole,Integer> {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public BaseMapper<UserRole, Integer> getMapper() {
        return userRoleRepository;
    }

    @Override
    public MybatisBaseMapper<UserRoleOutput> getMybatisBaseMapper() {
        return userRoleMapper;
    }


    public List<UserRole> findByUserId(Integer userId){
        var a = userRoleRepository.findAllByUserId(userId);
        return a;
    }


    public int deleteByUserIdANdRoleId(Integer userId,Integer roleId){
        return userRoleRepository.deleteAllByUserIdAndRoleId(userId,roleId);
    }




}
