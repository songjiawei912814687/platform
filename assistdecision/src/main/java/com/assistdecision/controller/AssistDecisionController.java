package com.assistdecision.controller;

import com.assistdecision.domain.output.AppraisalPlanOutput;
import com.assistdecision.domain.output.AttendanceStatisticsOutput;
import com.assistdecision.domain.output.FeedbackInfoOutput;
import com.assistdecision.domain.output.StationPeopleOutput;
import com.assistdecision.model.FeedbackInfo;
import com.assistdecision.proenum.DateTypeEnum;
import com.assistdecision.service.AssistDecisionService;
import com.common.model.PageData;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: young
 * @project_name: assist-decision
 * @description: 工作人员管理
 * @date: Created in 2019-02-27  15:09
 * @modified by:
 */
@RestController
@RequestMapping("assist_decision")
public class AssistDecisionController {

    @Autowired
    private AssistDecisionService assistDecisionService;
    @Autowired
    private LoadBalancerClient loadBalancerClient;


    /**
     * 调用获取组织的接口
     * @param request
     * @return
     */
    @GetMapping(value = "getOrganizationTree")
    public ResponseResult getTree(HttpServletRequest request){
        PageData pageData = new PageData(request);
        RestTemplate restTemplate = new RestTemplate();
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/organization/getZtree","personnel",pageData,request);
        return response;
    }

    /**工作人员管理*/
    @GetMapping("findAttendance")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "organizationId", value = "组织", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "string", paramType = "query")
    })
    public ResponseResult findAttendance(HttpServletRequest request){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        PageData pageData = new PageData(request);
        Calendar calendar = Calendar.getInstance();
        if(pageData.get("startTime")==null||pageData.get("startTime")==""){
            calendar.add(Calendar.DATE,-1);
        }
        String startTime = sdf.format(calendar.getTime())+" 00:00:00";
        String endTime  = sdf.format(calendar.getTime())+" 23:59:59";
        pageData.put("startTime",startTime);
        pageData.put("endTime",endTime);
        AttendanceStatisticsOutput attendanceStatisticsOutput = assistDecisionService.findAttendance(pageData);
        return ResponseResult.success(attendanceStatisticsOutput);
    }

    /**工作人员管理查看详情*/
    @GetMapping("findAttendanceDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "organizationId", value = "组织",  dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间",  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间",  dataType = "string", paramType = "query")
    })
    public ResponseResult findAttendanceDetail(HttpServletRequest request){

        PageData pageData = new PageData(request);
        return ResponseResult.success(assistDecisionService.findAttendanceDetail(pageData));
    }


    /**部门业务管理*/
    @GetMapping("departManager")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "month", value = "月", required = false, dataType = "int", paramType = "query"),

    })
    public ResponseResult departManager(HttpServletRequest request){
        PageData pageData = new PageData(request);
        if(pageData.get("year")==null||pageData.get("year")==""||pageData.get("month")==null||pageData.get("month")==""){

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,-1);

            String year = sdf.format(calendar.getTime());
            Integer month = Integer.parseInt(sdf1.format(calendar.getTime()));

            pageData.put("year",year);
            pageData.put("month",month.toString());
        }

        List<AppraisalPlanOutput> appraisalPlanOutputs =  assistDecisionService.departManager(pageData);
        if(appraisalPlanOutputs == null){
            return ResponseResult.success();
        }
        return ResponseResult.success(appraisalPlanOutputs);
    }

    /**平均办件时长*/
    @GetMapping("averageMinute")
    public ResponseResult averageMinute(){
        List<StationPeopleOutput> stationPeopleOutputs = assistDecisionService.averageMinute();
        if(stationPeopleOutputs==null){
            return ResponseResult.success();
        }
        return ResponseResult.success(stationPeopleOutputs);
    }

    /**部门业务百分比*/
    @GetMapping("business")
    public ResponseResult business(){
        List<FeedbackInfoOutput> feedbackInfoOutputs = assistDecisionService.business();
        if(feedbackInfoOutputs==null){
            return ResponseResult.success();
        }
        return ResponseResult.success(feedbackInfoOutputs);
    }

    /**窗口业务量*/
    @GetMapping("windowDo")
    public ResponseResult windowDo(){
        List<FeedbackInfoOutput> feedbackInfoOutputs =  assistDecisionService.windowDo();
        if(feedbackInfoOutputs==null){
            return ResponseResult.success();
        }
         return ResponseResult.success(feedbackInfoOutputs);
    }

    /**三个中心办件量折线图*/
    @GetMapping("doCount")
    public ResponseResult doCount(){
        return assistDecisionService.doCount();
    }

    /**热门事项排行榜*/
    @GetMapping("hotDo")
    public ResponseResult hotDo(){
        List<FeedbackInfoOutput>feedbackInfoOutputs = assistDecisionService.hotDo();
        return ResponseResult.success(feedbackInfoOutputs);
    }

    /**各部门满意率和实现率*/
    @GetMapping("satisAndComplete")
    public ResponseResult satisAndComplete(){
        List<FeedbackInfoOutput> feedbackInfoOutputs = assistDecisionService.satisAndComplete();
        return ResponseResult.success(feedbackInfoOutputs);
    }

    /*************************************1.中心运行情况***********************************************************************/
    /**查看各部门办件人流量*/
    @GetMapping("getVisitorNumber")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间",  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间",  dataType = "string", paramType = "query")
    })
    public ResponseResult getVisitorNumber(HttpServletRequest request){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        PageData pageData = new PageData(request);


        if(StringUtils.isBlank(pageData.getString("startTime"))&&
            StringUtils.isNotBlank(pageData.getString("endTime"))){

            pageData.put("startTime",sdf.format(new Date())+" 00:00:00");
        }else if(StringUtils.isBlank(pageData.getString("endTime"))&&
                StringUtils.isNotBlank(pageData.getString("startTime"))){

            pageData.put("endTime",sdf1.format(new Date()));
        }else if(StringUtils.isBlank(pageData.getString("endTime"))&&
                StringUtils.isBlank(pageData.getString("startTime"))){

            pageData.put("startTime",sdf.format(new Date())+" 00:00:00");
            pageData.put("endTime",sdf1.format(new Date()));
        }

        try {
            //如果开始时间大于等于结束时间，判断为错误
            if(sdf1.parse(pageData.getString("startTime")).compareTo(sdf1.parse(pageData.getString("endTime")))>=0){
                return ResponseResult.error("选择结束时间应该大于开始时间");
            }
        } catch (ParseException e) {
           return ResponseResult.error("时间转换出现异常");
        }

        List<StationPeopleOutput> stationPeopleOutputList = assistDecisionService.getVisitorNumber(pageData);
        return ResponseResult.success(stationPeopleOutputList);
    }

    /**查看各部门办件总量*/
    @GetMapping("getCompNumber")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间",  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间",  dataType = "string", paramType = "query")
    })
    public ResponseResult getCompNumber(HttpServletRequest request){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        PageData pageData = new PageData(request);


        if(StringUtils.isBlank(pageData.getString("startTime"))&&
                StringUtils.isNotBlank(pageData.getString("endTime"))){

            pageData.put("startTime",sdf.format(new Date())+" 00:00:00");
        }else if(StringUtils.isBlank(pageData.getString("endTime"))&&
                StringUtils.isNotBlank(pageData.getString("startTime"))){

            pageData.put("endTime",sdf1.format(new Date()));
        }else if(StringUtils.isBlank(pageData.getString("endTime"))&&
                StringUtils.isBlank(pageData.getString("startTime"))){

            pageData.put("startTime",sdf.format(new Date())+" 00:00:00");
            pageData.put("endTime",sdf1.format(new Date()));
        }

        List<StationPeopleOutput> stationPeopleOutputList = assistDecisionService.getCompNumber(pageData);
        return ResponseResult.success(stationPeopleOutputList);
    }

    /**各事项平均时长*/
    @GetMapping("mattersAverageMinutes")
    public ResponseResult mattersAverageMinutes(){

        List<StationPeopleOutput> stationPeopleOutputList = assistDecisionService.mattersAverageMinutes();
        return ResponseResult.success(stationPeopleOutputList);
    }

    @GetMapping("deptAverageMinutes")
    public ResponseResult deptAverageMinutes(){
        List<StationPeopleOutput> stationPeopleOutputList = assistDecisionService.deptAverageMinutes();
        return ResponseResult.success(stationPeopleOutputList);

    }

    /**********************************************2.满意度和投诉原因*****************************************************/
    @GetMapping("getSatisfyCondition")
    public ResponseResult getSatisfyCondition(String dateType){

        PageData pageData = new PageData();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        //如果是月份查询上月的结果
        if(DateTypeEnum.MONTH.getCode().equals(dateType)||
                StringUtils.isBlank(dateType)){


            cal.add(Calendar.MONTH,-1);
            cal.set(Calendar.DAY_OF_MONTH,1);
            String firstDay = sdf.format(cal.getTime());
            firstDay += " 00:00:00";

            cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH,0);
            String lastDay = sdf.format(cal.getTime());
            lastDay += "23:59:59";

            pageData.put("startTime",firstDay);
            pageData.put("endTime",lastDay);
        }else {
            //查询上年的结果
            cal.add(Calendar.YEAR,-1);
            cal.set(Calendar.MONTH,0);
            cal.set(Calendar.DATE,1);
            String firstDay = sdf.format(cal.getTime());
            firstDay += " 00:00:00";

            cal = Calendar.getInstance();
            cal.add(Calendar.YEAR,-1);
            cal.set(Calendar.MONTH,11);
            cal.set(Calendar.DATE,31);
            String lastDay = sdf.format(cal.getTime());
            lastDay += "23:59:59";

            pageData.put("startTime",firstDay);
            pageData.put("endTime",lastDay);
        }
        List<FeedbackInfoOutput> feedbackInfoOutputs = assistDecisionService.getSatisfyCondition(pageData);
        return ResponseResult.success(feedbackInfoOutputs);
    }

    @GetMapping("getCompThisMonth")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgId", value = "部门id", dataType = "int", paramType = "query"),
    })
    public ResponseResult getCompThisMonth(HttpServletRequest request){

        PageData pageData = new PageData(request);

        List<FeedbackInfoOutput> feedbackInfoOutputs = assistDecisionService.getCompThisMonth(pageData);
        PageHelper.startPage(pageData.getPageIndex(),pageData.getRows());
        PageInfo pageInfo = new PageInfo(feedbackInfoOutputs);
        return ResponseResult.success(pageInfo);
    }

    /************************************************工作人员详情*******************************************************************/
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间",  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "orgId", value = "部门id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "empName", value = "员工姓名", dataType = "string", paramType = "query")
    })
    @GetMapping("getWorkerDetail")
    public ResponseResult getWorkerDetail(HttpServletRequest request) {
        PageInfo<List<AttendanceStatisticsOutput>>  attendanceStatisticsOutputs = assistDecisionService.getWorkerDetail(request);
        return ResponseResult.success(attendanceStatisticsOutputs);

    }
}
