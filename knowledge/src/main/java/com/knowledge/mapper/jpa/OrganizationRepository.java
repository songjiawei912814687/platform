package com.knowledge.mapper.jpa;

import com.knowledge.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author: young
 * @description:
 * @date: Created in 2019-08-14  17:13
 */
public interface OrganizationRepository extends JpaRepository<Organization,Integer> {

    @Query(value = "select o from  Organization o where o.name =?1 and o.amputated = 0")
    Organization findByName(String name);
}
