package com.meeting.mapper.mybatis;

import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.RoleMenuOutput;
import com.meeting.model.RoleMenu;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMenuMapper extends MybatisBaseMapper<RoleMenuOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleMenu record);

    int insertSelective(RoleMenu record);

    @Override
    RoleMenuOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleMenu record);

    int updateByPrimaryKey(RoleMenu record);
}
