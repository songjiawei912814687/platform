package com.api.controller;

import com.api.domain.output.DoingOutput;
import com.api.service.DoConditionService;
import com.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Young
 * @description: 办件情形前20个工作日
 * @date: Created in 15:24 2018/11/20
 * @modified by:
 */
@RestController
@RequestMapping("do_condition")
public class DoConditionController {

    @Autowired
    private DoConditionService doConditionService;

    @GetMapping("findPageList")
    public ResponseResult findPageList() {

        DoingOutput doingOutput  = doConditionService.findPageList();
        if(doingOutput == null){
            return ResponseResult.success();
        }
        return ResponseResult.success(doingOutput);
    }
}
