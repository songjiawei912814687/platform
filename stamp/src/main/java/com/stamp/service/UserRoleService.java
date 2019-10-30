package com.stamp.service;


import com.stamp.core.base.BaseRepository;
import com.stamp.core.base.BaseService;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.UserRoleOutput;
import com.stamp.mapper.jpa.UserRoleRepository;
import com.stamp.mapper.mybatis.UserRoleMapper;
import com.stamp.model.UserRole;
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
    public BaseRepository<UserRole, Integer> getMapper() {
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

        Integer result =  userRoleMapper.deleteAllByUserIdAndRoleId(userId,roleId);
        if(result>0){
            return 1;
        }
        return 0;
    }

    public int deleteByRoleId(Integer roleId){
        return userRoleRepository.deleteByRoleId(roleId);
    }



}
