package com.assessment.controller;

import com.assessment.domian.output.AssessmentDepartReportOutput;
import com.assessment.service.AppraisalPlanService;
import com.assessment.service.AssessmentDepartReportService;
import com.common.model.PageData;
import com.common.request.GetConfig;
import com.common.response.ResponseResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author: Young
 * @description: 部门考核报表
 * @date: Created in 14:20 2018/10/11
 * @modified by:
 */
@RestController
@RequestMapping("assessment_depart_Report")
@Slf4j
public class AssessmentDepartReportController  {

    @Autowired
    private AssessmentDepartReportService assessmentDepartReportService;
    @Autowired
    private AppraisalPlanService appraisalPlanService;

    @GetMapping(value = "get_list")
    public ResponseResult getList(@RequestParam("year") Integer year,@RequestParam("month") Integer month){

       return assessmentDepartReportService.getCountList(year, month);
    }


    //TODO 统计口径
    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的部门考核报表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="organizationId",value="组织ID",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="year",value="年",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="month",value="月",required=false,dataType="int", paramType = "query")
            })
    public ResponseResult findDepartmentPlan(HttpServletRequest request){
        PageData pageData = new PageData(request);
        Integer year = (pageData.get("year")==null||"".equals(pageData.get("year")))?null:Integer.valueOf(pageData.get("year").toString());
        Integer month = (pageData.get("month")==null||"".equals(pageData.get("month")))?null:Integer.valueOf(pageData.get("month").toString());
        if((year!=null&&(year>9999||year%1!=0))||(month!=null&&(month<1||month>12||month%1!=0))){
            return ResponseResult.error("参数错误");
        }
        if(year==null&&month==null){
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(new Date()); // 设置为当前时间
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
        }
        pageData.put("year",year);
        pageData.put("month",month);
        return assessmentDepartReportService.findDepartList(pageData);
    }

    @GetMapping("excel")
    public ResponseResult getExcel(HttpServletRequest request, HttpServletResponse response){
        return assessmentDepartReportService.exportExcel(request, response);
    }

    @GetMapping("Synchronize")
    public ResponseResult SynchronizeBjsb(){
        return  null;
    }
}
