package com.screenData.mapper.mybatis;

import com.screenData.core.base.MybatisBaseMapper;
import com.screenData.domain.output.ScreenConfigOutput;
import com.screenData.model.ScreenConfig;
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
