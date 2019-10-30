package com.screenData.controller;

import com.common.response.ResponseResult;
import com.screenData.service.HotDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Young
 * @description: 热门事项办件量
 * @date: Created in 15:43 2018/11/20
 * @modified by:
 */
@RestController
@RequestMapping("hot")
public class HotDoController {

    @Autowired
    private HotDoService hotDoService;

    @GetMapping(value = "findPageList")
    public ResponseResult findPageList(){
        return hotDoService.findPageList();
    }
}
