package com.assessment.mapper.jpa;

import com.assessment.core.base.BaseMapper;
import com.assessment.model.Config;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigRepository extends BaseMapper<Config,Integer> {
    Config findByConfigKey(String key);

    List<Config> findByParentId(Integer parentId);
}
