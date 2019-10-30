package com.api.mapper.jpa;

import com.api.core.base.BaseMapper;
import com.api.model.Config;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigRepository extends BaseMapper<Config,Integer> {
    Config findByConfigKey(String key);

    List<Config> findByParentId(Integer parentId);

    @Query("select id, parentId,configKey,configValue, configNo from Config where parentId=?1 and state =?2")
    List<Config> findAllByParentIdAndState(Integer parentId,Integer state);
}
