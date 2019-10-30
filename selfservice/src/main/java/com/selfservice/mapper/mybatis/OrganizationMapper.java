package com.selfservice.mapper.mybatis;


import com.selfservice.core.base.MybatisBaseMapper;
import com.selfservice.domain.output.OrganizationOutput;
import com.selfservice.model.Organization;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationMapper extends MybatisBaseMapper<OrganizationOutput> {
    int insert(Organization record);

    int insertSelective(Organization record);

    List<Organization> getByName(Organization organization);

    List<Organization> getByPath(@Param(value = "path") String path);

    int selectCountName(String name);


    Integer selectOrganizationIdOrganizationByName(String name);

    Organization selectByName(String name);

    List<Organization> selectAllOrg();
}
