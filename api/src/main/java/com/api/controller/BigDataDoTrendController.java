package com.api.controller;

import com.api.service.BigDataDoTrendService;
import com.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Young
 * @description: 三个中心办件趋势线
 * @date: Created in 21:54 2018/10/30
 * @modified by:
 */
@RestController
@RequestMapping("bigData_trend")
public class BigDataDoTrendController {

    @Autowired
    private BigDataDoTrendService bigDataDoTrendService;

    @GetMapping("find_do_trend")
    public ResponseResult findDoTrend(){
        return bigDataDoTrendService.findDoTrend();
    }
}
