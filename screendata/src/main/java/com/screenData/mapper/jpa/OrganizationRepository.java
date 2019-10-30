package com.screenData.mapper.jpa;

import com.screenData.core.base.BaseMapper;
import com.screenData.model.Organization;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends BaseMapper<Organization,Integer> {
}
