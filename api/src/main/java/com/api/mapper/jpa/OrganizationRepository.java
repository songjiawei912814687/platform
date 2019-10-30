package com.api.mapper.jpa;

import com.api.core.base.BaseMapper;
import com.api.model.Organization;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends BaseMapper<Organization,Integer> {
}
