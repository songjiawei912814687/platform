package com.meeting.mapper.mybatis;

import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.OrganizationOutput;
import com.meeting.model.Organization;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationMapper extends MybatisBaseMapper<OrganizationOutput> {
    int insert(Organization record);

    int insertSelective(Organization record);

    OrganizationOutput selectByPrimaryKey(Integer id);

    List<Organization> getByName(Organization organization);

    List<Organization> getByPath(@Param(value = "path") String path);

    List<Organization> getByLikePath(@Param(value = "path") String path);

    int selectCountName(String name);


    Integer selectOrganizationIdOrganizationByName(String name);

    List<Organization> selectByName(String name);

    Organization selectOrNoByOrId(Integer orgaId);

    List<Organization> selectAllOrg();

    String  selectMaxNo();

    List<Organization> selectByNo(String organizationNo);

    List<OrganizationOutput> selectByCode(String code);

    List<OrganizationOutput> selectByParentId(Integer organizationId);
}
