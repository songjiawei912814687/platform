package com.api.domain.input;

import com.api.model.TradingCenter;

import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 17:03 2018/11/27
 * @modified by:
 */
public class TradingCenterInput  {

    private List<TradingCenter> tradingCenterList;

    public List<TradingCenter> getTradingCenterList() {
        return tradingCenterList;
    }

    public void setTradingCenterList(List<TradingCenter> tradingCenterList) {
        this.tradingCenterList = tradingCenterList;
    }
}
