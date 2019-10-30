package com.api.mapper.mybatis;

import com.api.core.base.MybatisBaseMapper;
import com.api.domain.output.OrganizationOutput;
import com.common.model.PageData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationMapper extends MybatisBaseMapper<OrganizationOutput> {
//    int insert(Organization record);
//
//    int insertSelective(Organization record);
//
//    List<Organization> getByName(Organization organization);
//
//    List<Organization> getByPath(String path);
//
//    int selectCountName(String name);
//
//    Integer selectOrganizationIdOrganizationByName(String name);

//    OrganizationOutput selectByName(String name);
//
//    int selectCount(Integer id);

    String selectNameById(Integer id);

    List<OrganizationOutput> getByParentId(Integer parentId);

    List<Integer> selectChildById(Integer id);


    @Override
    List<OrganizationOutput> selectAll(PageData pageData);

    /**根据组织编号查询出组织对象*/
    OrganizationOutput selectByOrgaNo(String orgaNo);


    List<OrganizationOutput> selectByOrgaNoList(@Param("orgaNoList") List<String> orgaNoList);
}
