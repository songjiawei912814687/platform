package com.screenData.controller;


import com.common.response.ResponseResult;
import com.screenData.domain.output.VisitOutput;
import com.screenData.service.VisitorsNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
