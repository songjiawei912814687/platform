package com.personnel.mapper.jpa;

import com.personnel.core.base.BaseMapper;
import com.personnel.model.Organization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends BaseMapper<Organization, Integer> {

    List<Organization> findByName(String name);

    List<Organization> findByParentId(Integer id);

    List<Organization> findByNameAndAmputated(String name, Integer isDelete);

    List<Organization> findByParentIdAndAmputated(Integer parentId, int amputated);

    List<Organization> findByAmputated(int i);

    @Query("select max(organizationNo) from Organization")
    String findmaxno();

    List<Organization> findByOrganizationNoAndAmputated(String no, Integer isDelete);

    List<Organization> findAllByDepartmentalManager(Integer employeeId);

    Organization findByLinkedId(Integer id);
}
