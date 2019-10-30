package com.screenData.controller;


import com.common.response.ResponseResult;
import com.screenData.domain.output.WaitingNumberOutput;
import com.screenData.service.WaitingNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("waitingNumber")
public class WaitingNumberController {

    @Autowired
    private WaitingNumberService waitingNumberService;

    /**当前大厅等待人数*/
    @GetMapping("findPageList")
    public ResponseResult findPageList() {
        List<WaitingNumberOutput> waitingNumberOutputs = waitingNumberService.findPageList();
        return ResponseResult.success(waitingNumberOutputs);
    }
}
