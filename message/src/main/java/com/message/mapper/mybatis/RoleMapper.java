package com.message.mapper.mybatis;


import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.RoleOutput;
import com.message.model.Role;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper extends MybatisBaseMapper<RoleOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    RoleOutput selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> getRolesNotInUse(PageData PageData);

    List<Role> judgeRoleIsInUse(PageData pageData);
}
