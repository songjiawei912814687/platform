package com.api.mapper.mybatis;

import com.api.core.base.MybatisBaseMapper;
import com.api.domain.output.TradingCenterOutput;
import com.api.model.TradingCenter;
import org.springframework.stereotype.Repository;

@Repository
public interface TradingCenterMapper extends MybatisBaseMapper<TradingCenterOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(TradingCenter record);

    int insertSelective(TradingCenter record);

    TradingCenterOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TradingCenter record);

    int updateByPrimaryKey(TradingCenter record);
}