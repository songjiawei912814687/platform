package com.meeting.mapper.mybatis;


import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.RoleActionOutput;
import com.meeting.model.RoleAction;

public interface RoleActionMapper extends MybatisBaseMapper<RoleActionOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(RoleAction record);

    int insertSelective(RoleAction record);

    RoleActionOutput selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleAction record);

    int updateByPrimaryKey(RoleAction record);
}
