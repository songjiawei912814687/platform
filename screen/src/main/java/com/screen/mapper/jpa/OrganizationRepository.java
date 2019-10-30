package com.screen.mapper.jpa;

import com.screen.core.base.BaseMapper;
import com.screen.model.Organization;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends BaseMapper<Organization,Integer> {
}
