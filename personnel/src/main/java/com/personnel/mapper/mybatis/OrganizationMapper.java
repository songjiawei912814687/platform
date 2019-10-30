package com.personnel.mapper.mybatis;

import com.common.model.PageData;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.OrganizationOutput;
import com.personnel.model.Organization;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationMapper extends MybatisBaseMapper<OrganizationOutput> {
    int insert(Organization record);

    int insertSelective(Organization record);

    List<Organization> getByName(Organization organization);

    List<Organization> getByPath(@Param(value = "path") String path);

    List<Organization> getByLikePath(@Param(value = "path") String path);

    int selectCountName(String name);


    Integer selectOrganizationIdOrganizationByName(String name);

    List<Organization> selectByName(String name);

    //根据主键查询出组织
    Organization selectOrNoByOrId(Integer orgaId);

    List<Organization> selectAllOrg();

    String  selectMaxNo();

    List<Organization> selectByNo(String organizationNo);


    Organization selectByOrgaName(String organizationName);


    //查询组织编号是否是中心窗口下的
    Integer selectCheckCondition(String orgaNo);


    List<Organization> selectOrganizationMobile(Integer organizationId);


    List<Organization> selectOrganizationPath();

    List<Organization> getByUserId(PageData pageData);

    List<Organization> noIsRepeat(PageData pageData);

    List<Organization> selectByParentId(Integer organizationId);

    List<Organization> getByNameWithinAuthority(PageData pageData);

    List<Organization> findInitDatas();


    List<Organization> selectByLinkedId(Integer linkedId);

    String selectByRuleConfigId(Integer id);
}
