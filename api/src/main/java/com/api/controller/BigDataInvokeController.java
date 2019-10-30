package com.api.controller;

import com.api.service.BigDataInvokeService;
import com.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: Young
 * @description: 大数据调用量柱形图
 * @date: Created in 10:24 2018/10/30
 * @modified by:
 */
@RestController
@RequestMapping("bigData")
public class BigDataInvokeController {

    @Autowired
    private BigDataInvokeService bigDataInvokeService;

    @GetMapping("/find_bigDataInvoke")
    public ResponseResult findBigData() {
        return bigDataInvokeService.findBigData();
    }
}
