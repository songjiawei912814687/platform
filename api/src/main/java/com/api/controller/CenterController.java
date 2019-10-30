package com.api.controller;

import com.api.domain.output.EmployeesOutput;
import com.api.domain.output.WindowEmployeesOutput;
import com.api.model.Config;
import com.api.model.Employees;
import com.api.service.OrganizationService;
import com.api.service.WindowEmployeesService;
import com.api.service.WindowService;
import com.common.response.ResponseResult;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("center")
@Api(value = "中间数据controller",description = "中间数据操作",tags = {"中间数据操作接口"})
public class CenterController {
    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/getOrganization")
    @ApiOperation("业务列表")
    public ResponseResult getOrganization() {
        List<Config> configList = Lists.newArrayList();
        try {
            configList = organizationService.getDoMatters();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(configList);
    }

    @GetMapping("/getMList")
    @ApiOperation("M的List")
    public ResponseResult getMList(){
        organizationService.getMList();
        return ResponseResult.success("获取成功");
    }
    @GetMapping("/getprincipal")
    @ApiOperation("负责人")
    public ResponseResult getprincipal(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("组织机构id不能为空");
        }
        List<EmployeesOutput> employeesOutputList = Lists.newArrayList();
        try {
            employeesOutputList = organizationService.selectDepartMange(organizationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(employeesOutputList);
    }

    @GetMapping("/getWindowCount")
    @ApiOperation("服务窗口数")
    public ResponseResult getWindowCount(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("业务id不能为空");
        }
        Integer allCount = organizationService.getWindowCount(organizationId);
        return ResponseResult.success(allCount);
    }

    @GetMapping("/getWindowOpenCount")
    @ApiOperation("当前开放窗口数")
    public ResponseResult getWindowOpenCount(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("组织机构id不能为空");
        }
        Integer allOnlineCounterCount = organizationService.getWindowOpenCount(organizationId);
        return ResponseResult.success(allOnlineCounterCount);
    }

    @GetMapping("/getOpenWindow")
    @ApiOperation("可办理窗口号")
    public ResponseResult getOpenWindow(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("组织机构id不能为空");
        }
        List<String> windowNoList = organizationService.getOpenWindow(organizationId);
        return ResponseResult.success(windowNoList);
    }

    @GetMapping("/getAverageaitingime")
    @ApiOperation("平均等待时长")
    public ResponseResult getAverageaitingime(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("组织机构id不能为空");
        }
        BigDecimal average = organizationService.getAverageaitingime(organizationId);
        return ResponseResult.success(average);
    }

    @GetMapping("/getCurrentWaitingNumber")
    @ApiOperation("当前等待人数")
    public ResponseResult getCurrentWaitingNumber(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("组织机构id不能为空");
        }
        Integer allWait = organizationService.getCurrentWaitingNumber(organizationId);
        return ResponseResult.success(allWait);
    }

    @GetMapping("/getBookingOnLine")
    @ApiOperation("网上预约人数")
    public ResponseResult getBookingOnLine(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("组织机构id不能为空");
        }
        Integer allAppointment =  organizationService.getBookingOnLine(organizationId);
        return ResponseResult.success(allAppointment);
    }

    @GetMapping("/getBoardingSituation")
    @ApiOperation("上岗情况")
    public ResponseResult getBoardingSituation(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("组织机构id不能为空");
        }
        BigDecimal boardingSituation = organizationService.getBoardingSituation(organizationId);
        return ResponseResult.success(boardingSituation);
    }

    @GetMapping("/getAbsenceOfPosts")
    @ApiOperation("未上岗情况,有两个接口")
    public ResponseResult getAbsenceOfPosts(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("组织机构id不能为空");
        }
        List<String> windowNoList = organizationService.getAbsenceOfPosts(organizationId);
        return ResponseResult.success(windowNoList);
    }


    @GetMapping("/getSatisfaction")
    @ApiOperation("满意度")
    public ResponseResult getSatisfaction(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("组织机构id不能为空");
        }
        BigDecimal statisPoint = organizationService.getSatisfaction(organizationId);
        return ResponseResult.success(statisPoint);
    }

    @GetMapping("/getDissatisfaction")
    @ApiOperation("不满意情况")
    public ResponseResult getDissatisfaction(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("组织机构id不能为空");
        }

        List<String> unStatisList = organizationService.getDissatisfaction(organizationId);
        return ResponseResult.success(unStatisList);
    }

    @GetMapping("/getTodayVolume")
    @ApiOperation("今日办件量")
    public ResponseResult getTodayolume(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("组织机构id不能为空");
        }
        Integer allDoCount =  organizationService.getTodayolume(organizationId);
        return ResponseResult.success(allDoCount);
    }

    @GetMapping("/getWindowTodayVolume")
    @ApiOperation("各窗口办件")
    public ResponseResult getWindowTodayVolume(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("组织机构id不能为空");
        }
        List<String> doList = organizationService.getWindowTodayVolume(organizationId);
        return ResponseResult.success(doList);
    }

    @GetMapping("/getEmployeeList")
    @ApiOperation("窗口人员列表")
    public ResponseResult getEmployeeList(Integer organizationId) {
        if(organizationId==null||organizationId.equals("")){
            return ResponseResult.error("组织机构id不能为空");
        }
        List<WindowEmployeesOutput>windowEmployeesOutputList =
                organizationService.getEmployeeList(organizationId);
        return ResponseResult.success(windowEmployeesOutputList);
    }
}
