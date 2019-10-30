package com.api.service;

import com.api.core.base.BaseMapper;
import com.api.core.base.BaseService;
import com.api.core.base.MybatisBaseMapper;
import com.api.domain.input.TradingCenterInput;
import com.api.domain.output.TradingCenterOutput;
import com.api.mapper.jpa.TradingCenterRepository;
import com.api.mapper.mybatis.TradingCenterMapper;
import com.api.model.TradingCenter;
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
 * @date: Created in 17:05 2018/11/27
 * @modified by:
 */
@Service
public class TradingCenterService extends BaseService<TradingCenterOutput, TradingCenter,Integer> {

    @Autowired
    private TradingCenterMapper tradingCenterMapper;

    @Autowired
    private TradingCenterRepository tradingCenterRepository;

    @Override
    public BaseMapper<TradingCenter, Integer> getMapper() {
        return tradingCenterRepository;
    }

    @Override
    public MybatisBaseMapper<TradingCenterOutput> getMybatisBaseMapper() {
        return tradingCenterMapper;
    }


    public int addNews(TradingCenterInput tradingCenterInput) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        List<TradingCenter> tradingCenterList = tradingCenterInput.getTradingCenterList();
        if(tradingCenterList.size()>0 && tradingCenterList!=null){
           for(TradingCenter tradingCenter:tradingCenterList){
               tradingCenter.setCreatedDateTime(new Date());
               int result = this.add(tradingCenter);
               if(result==0){
                   return ERROR;
               }
           }
           return SUCCESS;
        }
        return ERROR;
    }
}
