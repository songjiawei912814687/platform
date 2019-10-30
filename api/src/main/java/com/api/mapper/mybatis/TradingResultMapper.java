package com.api.mapper.mybatis;

import com.api.core.base.MybatisBaseMapper;
import com.api.domain.output.TradingResultOutput;
import com.api.model.TradingResult;
import org.springframework.stereotype.Repository;

@Repository
public interface TradingResultMapper extends MybatisBaseMapper<TradingResultOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(TradingResult record);

    int insertSelective(TradingResult record);

    TradingResultOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TradingResult record);

    int updateByPrimaryKey(TradingResult record);
}