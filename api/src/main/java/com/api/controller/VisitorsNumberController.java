package com.api.controller;


import com.api.domain.output.VisitOutput;
import com.api.service.VisitorsNumberService;
import com.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("visitorsNumber")
public class VisitorsNumberController {

    @Autowired
    private VisitorsNumberService visitorsNumberService;

    /**来访人数*/
    @GetMapping("findPageList")
    public ResponseResult findPageList() throws Exception{
        VisitOutput visitOutput = visitorsNumberService.findPageList();
        if(visitOutput == null){
            return ResponseResult.success();
        }
        return ResponseResult.success(visitOutput);
    }
}
