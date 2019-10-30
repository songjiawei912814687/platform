package com.api.domain.input;

import com.api.model.TradingResult;

import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 21:00 2018/11/27
 * @modified by:
 */
public class TradingResultInput {

    List<TradingResult> tradingResultList;

    public List<TradingResult> getTradingResultList() {
        return tradingResultList;
    }

    public void setTradingResultList(List<TradingResult> tradingResultList) {
        this.tradingResultList = tradingResultList;
    }
}
