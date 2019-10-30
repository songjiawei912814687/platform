package com.api.service;

import com.api.core.base.BaseMapper;
import com.api.core.base.BaseService;
import com.api.core.base.MybatisBaseMapper;
import com.api.domain.input.TradingResultInput;
import com.api.domain.output.TradingResultOutput;
import com.api.mapper.jpa.TradingResultRepository;
import com.api.mapper.mybatis.TradingResultMapper;
import com.api.model.TradingCenter;
import com.api.model.TradingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 21:03 2018/11/27
 * @modified by:
 */
@Service
public class TradingResultService extends BaseService<TradingResultOutput, TradingResult,Integer> {

    @Autowired
    private TradingResultMapper tradingResultMapper;
    @Autowired
    private TradingResultRepository tradingResultRepository;


    @Override
    public BaseMapper<TradingResult, Integer> getMapper() {
        return tradingResultRepository;
    }

    @Override
    public MybatisBaseMapper<TradingResultOutput> getMybatisBaseMapper() {
        return tradingResultMapper;
    }


    public int addNews(TradingResultInput tradingResultInput) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        List<TradingResult> tradingResultList = tradingResultInput.getTradingResultList();
        if(tradingResultList.size()>0 && tradingResultList!=null){
            for(TradingResult tradingResult:tradingResultList){
                tradingResult.setCreatedDateTime(new Date());
                int result = this.add(tradingResult);
                if(result==0){
                    return ERROR;
                }
            }
            return SUCCESS;
        }
        return ERROR;
    }
}
