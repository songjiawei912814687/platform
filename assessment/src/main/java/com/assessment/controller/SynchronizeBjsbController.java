package com.assessment.controller;

import com.assessment.model.AppraisalPlan;
import com.assessment.model.Users;
import com.assessment.service.AppraisalPlanService;
import com.assessment.service.SynchronizeBjsbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@RestController
@Api(value = "定时任务controller",description = "定时任务",tags = {"定时任务"})
@RequestMapping("jobs")
public class SynchronizeBjsbController{

    @Autowired
    private SynchronizeBjsbService synchronizeBjsbService;

    @Autowired
    private AppraisalPlanService appraisalPlanService;

    @ApiOperation(value="同步办件库")
    @GetMapping("/synchronize")
    public void SynchronizeBjsb()throws Exception{
        synchronizeBjsbService.synchronizeDate();
    }

    @ApiOperation(value="部门考核计划打分")
    @GetMapping("/departmentPlan")
    @ApiImplicitParams({@ApiImplicitParam(name="date",value="打分时间",required=false,dataType="string", paramType = "query",example = "2018-09")})
    public void departmentPlan(String date){
        Users users = appraisalPlanService.getUsers();
        appraisalPlanService.setDepartmentPlanScore(date,users,null);
    }

    @ApiOperation(value="考勤月报表")
    @GetMapping("/attendanceMonthStatic")
    public void attendanceMonthStatic(){
        appraisalPlanService.AttendanceMonthStatic("2018-09");
    }

}
