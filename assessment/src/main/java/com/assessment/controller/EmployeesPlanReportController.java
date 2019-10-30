package com.assessment.controller;

import com.assessment.core.base.BaseController;
import com.assessment.core.base.BaseService;
import com.assessment.domian.output.AppraisalPlanOutput;
import com.assessment.domian.output.EmployeesRecordOutput;
import com.assessment.model.AppraisalPlan;
import com.assessment.service.AppraisalEmployeeRecordService;
import com.assessment.service.AppraisalPlanService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "/employeesplanreport")
public class EmployeesPlanReportController extends BaseController<AppraisalPlanOutput, AppraisalPlan,Integer> {
    @Autowired
    private AppraisalPlanService appraisalPlanService;

    @Autowired
    private AppraisalEmployeeRecordService appraisalEmployeeRecordService;
    @Override
    public BaseService<AppraisalPlanOutput, AppraisalPlan, Integer> getService() {
        return appraisalPlanService;
    }


    //员工考核考核计划报表
    @GetMapping(value = "findEmployeesPlanReport")
    @ApiOperation("获取员工考核报表")
    @ApiImplicitParams({@ApiImplicitParam(name="organizationId",value="组织机构id",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="year",value="年",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="month",value="月",required=false,dataType="int", paramType = "query")})
    public ResponseResult findEmployeesPlanReport(HttpServletRequest request){
        PageData pageData = new PageData(request);
        if(pageData.getMap().get("year")==null
                ||pageData.getMap().get("year").equals("")
                ||pageData.getMap().get("month")==null
                ||pageData.getMap().get("month").equals("")){
            return ResponseResult.error("获取员工考核报表失败");
        }
        if(pageData.getMap().get("organizationId")!=null&&!pageData.getMap().get("organizationId").equals("")){
            String  s=appraisalPlanService.getPathById(Integer.parseInt(pageData.getMap().get("organizationId")));
            pageData.put("path",s);
        }
        pageData.put("userId",appraisalPlanService.getUsers().getId().toString());
        if(appraisalPlanService.getUsers().getAdministratorLevel()!=9){
            if(appraisalPlanService.getUsers().getUserType()==0){
                pageData.put("employeeId",appraisalPlanService.getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",appraisalPlanService.getUsers().getOrganizationId().toString());
            }
        }
        return ResponseResult.success(new PageInfo<>(appraisalPlanService.getEmployeesPlan(pageData)));
    }

    //员工考核报表详情
    @GetMapping(value = "findRecord")
    @ApiOperation("获取员工考核报表详情")
    @ApiImplicitParams({@ApiImplicitParam(name="planId",value="计划Id",required=false,dataType="int", paramType = "query")})
    public ResponseResult findRecord(HttpServletRequest request){
        PageData pageData = new PageData(request);
        if(pageData.getMap().get("planId")==null
                ||pageData.getMap().get("planId").equals("")){
            return ResponseResult.error("获取员工考核报表详情失败");
        }
        AppraisalPlanOutput appraisalPlanOutput=appraisalPlanService.selectById(Integer.parseInt(pageData.getMap().get("planId")));
        if(appraisalPlanOutput==null){
            ResponseResult.error(PARAM_EORRO);
        }
        int month=appraisalPlanOutput.getMonth();
        String m=null;
        if(month<10){
            m="0"+month;
        }else {
            m=""+month;
        }
        pageData.put("employeeId",appraisalPlanOutput.getEmployeeId());
        pageData.put("date",""+appraisalPlanOutput.getYear()+"-"+m);
        List<EmployeesRecordOutput>  list=appraisalEmployeeRecordService.getEmployeeRecord(pageData);
        return ResponseResult.success(list);
    }

    /**
     * 导出员工考核计划报表信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "employeesReportExport", method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(name="organizationId",value="组织机构id",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="year",value="年",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="month",value="月",required=false,dataType="int", paramType = "query")})
    public ResponseResult export(HttpServletResponse response, HttpServletRequest request) {
        PageData pageData = new PageData(request);
        if(pageData.getMap().get("year")==null
                ||pageData.getMap().get("year").equals("")
                ||pageData.getMap().get("month")==null
                ||pageData.getMap().get("month").equals("")){
            return ResponseResult.error("导出员工考核报表失败");
        }
        try {
            String str = appraisalPlanService.EmployeesReportExport(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

