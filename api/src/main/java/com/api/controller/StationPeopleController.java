package com.api.controller;

import com.api.service.StationPeopleService;
import com.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * @author: young
 * @project_name: svn
 * @description: 获取取号叫号数据的方法
 * @date: Created in 2019-01-11  14:16
 * @modified by:
 */
@RestController
@RequestMapping("stationPeople")
public class StationPeopleController {

    @Autowired
    private StationPeopleService stationPeopleService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**同步sqlServer取号叫号源数据到oracle的取号叫号源数据*/
    @PostMapping("getStationPeople")
    public ResponseResult getStationPeople(String startTime,String endTime) throws Exception {
        stationPeopleService.getStationPeople(startTime,endTime);

        return ResponseResult.success();
    }

    /**同步sqlServer评价结果源数据到oracle的评价结果源数据*/
    @PostMapping("getEvalResult")
    public ResponseResult getEvalResult(String startTime,String endTime) throws Exception {
        stationPeopleService.getEvalResult(startTime,endTime);
        return ResponseResult.success();
    }

    /**oracle中取号源数据同步到自己的取号表中*/
    @GetMapping("sysStationPeople")
    public ResponseResult sysStationPeople(){
        stationPeopleService.sysStationPeople();
        return ResponseResult.success("同步取号叫号源数据到取号叫号中成功");
    }

    /**oracle中评价结果源数据同步到自己的评价表中*/
    @GetMapping("sysEvalResult")
    public ResponseResult sysEvalResult(){
        stationPeopleService.sysEvalResult();
        return ResponseResult.success("同步评价结果源数据到评价结果成功");
    }

    /**评价结果原表数据推送到反馈信息*/
    @GetMapping("synFeedBack")
    public ResponseResult synFeedBack(){
        stationPeopleService.synFeedBack();
        return ResponseResult.success("同步反馈信息成功");
    }

    @GetMapping("sysSourceCounter")
    public ResponseResult sysSourceCounter() throws Exception {
        stationPeopleService.sysSourceCounter();
        return ResponseResult.success("同步窗口登陆信息成功");
    }
}
