package com.personnel.mapper.mybatis;


import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.RoleOutput;
import com.personnel.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper extends MybatisBaseMapper<RoleOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    RoleOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<RoleOutput> findByUserId(Integer userId);

    List<RoleOutput> selectByDefaultPermissions();
}
