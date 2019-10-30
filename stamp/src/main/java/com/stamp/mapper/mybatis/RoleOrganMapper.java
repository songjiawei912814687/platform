package com.stamp.mapper.mybatis;

import com.common.model.PageData;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.OrganPermissterOutput;
import com.stamp.domain.output.RoleOrganOutput;
import com.stamp.model.RoleOrgan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleOrganMapper extends MybatisBaseMapper<RoleOrganOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(RoleOrgan record);

    int insertSelective(RoleOrgan record);

    RoleOrganOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleOrgan record);

    int updateByPrimaryKey(RoleOrgan record);

    List<OrganPermissterOutput> findOrganByuserId(PageData pageData);

    List<OrganPermissterOutput> findAllOrgan();
}
