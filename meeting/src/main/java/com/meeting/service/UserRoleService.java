package com.meeting.service;

import com.meeting.core.base.BaseMapper;
import com.meeting.core.base.BaseService;
import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.UserRoleOutput;
import com.meeting.mapper.jpa.UserRoleRepository;
import com.meeting.mapper.mybatis.UserRoleMapper;
import com.meeting.model.UserRole;
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
