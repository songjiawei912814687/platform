package com.personnel.mapper.jpa;

import com.personnel.core.base.BaseMapper;
import com.personnel.model.Jobs;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobsRepository extends BaseMapper<Jobs, Integer> {

    List<Jobs> findByName(String name);
}
