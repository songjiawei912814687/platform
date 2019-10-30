package com.wechatsug.mapper.mybatis;


import com.common.model.PageData;
import com.wechatsug.model.Organization;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrganizationMapper  {
//    int insert(Organization record);
//
//    int insertSelective(Organization record);

//    List<Organization> getByName(Organization organization);

//    List<Organization> getByPath(@Param(value = "path") String path);

//    List<Organization> getByLikePath(@Param(value = "path") String path);

//    int selectCountName(String name);


//    Integer selectOrganizationIdOrganizationByName(String name);

//    List<Organization> selectByName(String name);

    Organization selectOrNoByOrId(Integer orgaId);

//    List<Organization> selectAllOrg();
//
//    String  selectMaxNo();

//    List<Organization> selectByNo(String organizationNo);


    List<Organization> selectPage(PageData pageData);

    List<Organization> selectAll(PageData pageData);
}
