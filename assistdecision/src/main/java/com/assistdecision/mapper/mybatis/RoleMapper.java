package com.assistdecision.mapper.mybatis;

import com.assistdecision.core.base.MybatisBaseMapper;
import com.assistdecision.domain.output.RoleOutput;
import com.assistdecision.model.Role;
import com.common.model.PageData;
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

    List<Role> getRolesNotInUse(PageData PageData);

    List<Role> judgeRoleIsInUse(PageData pageData);

    List<RoleOutput> findByUserId(Integer userId);
}
