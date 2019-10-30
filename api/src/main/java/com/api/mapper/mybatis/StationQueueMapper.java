package com.api.mapper.mybatis;

import com.api.core.base.MybatisBaseMapper;
import com.api.domain.output.StationQueueOutput;
import org.springframework.stereotype.Repository;

@Repository
public interface StationQueueMapper extends MybatisBaseMapper<StationQueueOutput> {
}
