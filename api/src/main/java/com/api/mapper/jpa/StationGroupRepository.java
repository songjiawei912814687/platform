package com.api.mapper.jpa;

import com.api.model.StationGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationGroupRepository extends JpaRepository<StationGroup,Integer> {
    StationGroup findByCode(String code);
}
