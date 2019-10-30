package com.feedback.mapper.mybatis;


import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.domain.output.OrganizationOutput;
import com.feedback.model.Organization;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationMapper extends MybatisBaseMapper<OrganizationOutput> {
//    int insert(Organization record);
//
//    int insertSelective(Organization record);

//    List<Organization> getByName(Organization organization);

//    List<Organization> getByPath(@Param(value = "path") String path);

//    int selectCountName(String name);

//    Integer selectOrganizationIdOrganizationByName(String name);

    /**
     * 根据组织名称查询组织对象
     * @param name
     * @return
     */
    Organization selectByName(String name);

//    List<Organization> selectAllOrg();

    /**
     * 根据组织编号查询出组织对象
     * @param orgaNo
     * @return
     */
    Organization selectByOrgaNo(String orgaNo);

    @Override
    OrganizationOutput selectByPrimaryKey(Integer id);


    List<Organization> selectOrganizationMobile(Integer organizationId);
}
