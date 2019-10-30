package com.screen.mapper.mybatis;

import com.screen.core.base.MybatisBaseMapper;
import com.screen.domain.output.ScreenConfigOutput;
import com.screen.model.ScreenConfig;
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
