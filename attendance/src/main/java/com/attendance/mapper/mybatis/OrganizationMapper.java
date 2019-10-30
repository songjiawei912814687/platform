package com.attendance.mapper.mybatis;


import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.OrganizationOutput;
import com.attendance.model.Organization;
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
//    List<Organization> getByPath(@Param(value = "path") String path);
//
//    List<Organization> getByLikePath(@Param(value = "path") String path);
//
//    int selectCountName(String name);
//
//
//    Integer selectOrganizationIdOrganizationByName(String name);
//
//    List<Organization> selectByName(String name);
//
    Organization selectOrNoByOrId(Integer orgaId);

    /**
     * 根据考勤规则配置id和 人员工号查询到对象
     * @param ruleConfigId
     * @param empNo
     * @return
     */
    OrganizationOutput selectByAttendanceRuleConfigIdAndEmpNo(@Param("ruleConfigId") Integer ruleConfigId,
                                                              @Param("empNo") String empNo);

//    List<Organization> selectAllOrg();

//    String  selectMaxNo();

//    List<Organization> selectByNo(String organizationNo);


//    Organization selectByOrgaName(String organizationName);


//    //查询组织编号是否是中心窗口下的
//    Integer selectCheckCondition(String orgaNo);
//
//
//
//    List<Organization> selectOrganizationMobile(Integer organizationId);

    List<Organization> selectByPath(Integer organizationId);

    //根据组织id查询出组织的规则id
    Integer selectRuleIdByOrgId(Integer orgId);

    List<Organization> selectOrganizationByRuleId(Integer ruleId);

    /**
     * 根据组织id查询出审批流规则配置
     * @param orgId 组织id
     * @return
     */
    Double selectApproveRule(Integer orgId);
}
