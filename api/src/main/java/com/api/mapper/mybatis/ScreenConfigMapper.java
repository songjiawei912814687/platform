package com.api.mapper.mybatis;

import com.api.core.base.MybatisBaseMapper;
import com.api.domain.output.ScreenConfigOutput;
import com.api.domain.output.WindowOutput;
import com.api.model.ScreenConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenConfigMapper  extends MybatisBaseMapper<ScreenConfigOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(ScreenConfig record);

    int insertSelective(ScreenConfig record);

    ScreenConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ScreenConfig record);

    int updateByPrimaryKey(ScreenConfig record);

    List<ScreenConfigOutput> findAll();
}
