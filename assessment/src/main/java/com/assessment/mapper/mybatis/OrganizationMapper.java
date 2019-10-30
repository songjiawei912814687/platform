package com.assessment.mapper.mybatis;

import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AppraisalOrganizationOutput;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationMapper extends MybatisBaseMapper<AppraisalOrganizationOutput> {

    List<AppraisalOrganizationOutput> getAppraisalOrganization(PageData pagedate );

    List<AppraisalOrganizationOutput> judgeOrganizationIsInUse(PageData pageData);

    List<AppraisalOrganizationOutput> getNameAndId();

    Integer selectOrganizationId(String organizationName);

    /**
     * 根据组织名称查询组织编号
     * @param name
     * @return
     */
    String selectByName(String name);

//    List<Organization> selectAllOrg();

    /**
     * 根据组织编号查询出组织id
     * @param orgaNo
     * @return
     */
    Integer selectByOrgaNo(String orgaNo);

    List<AppraisalOrganizationOutput> getByOrgId(PageData pageData);

    List<AppraisalOrganizationOutput> getByOrgIdWithinAuthority(PageData pageData);

    List<AppraisalOrganizationOutput> getByPath(String substring);

    AppraisalOrganizationOutput getById(Integer id);

    List<AppraisalOrganizationOutput> getByCopyOrganNo(String copyOrganNo);
}
