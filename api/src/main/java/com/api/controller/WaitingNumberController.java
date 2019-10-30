package com.api.controller;


import com.api.domain.output.WaitingNumberOutput;
import com.api.service.WaitingNumberService;
import com.common.response.ResponseResult;
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
