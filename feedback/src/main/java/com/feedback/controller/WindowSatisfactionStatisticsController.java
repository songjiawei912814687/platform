package com.feedback.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.feedback.core.base.BaseController;
import com.feedback.core.base.BaseService;
import com.feedback.domain.output.FeedbackInfoOutput;
import com.feedback.model.FeedbackInfo;
import com.feedback.service.FeedbackInfoService;
import com.feedback.service.WindowSatisfactionStatisticsService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
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
import java.util.GregorianCalendar;
import java.util.List;


@RestController
@RequestMapping(value = "/windowSatisfactionStatistics")
@Api(value = "人员满意度统计controller",description = "人员满意度统计操作",tags = {"人员满意度统计操作接口"})
public class WindowSatisfactionStatisticsController extends BaseController<FeedbackInfoOutput, FeedbackInfo,Integer> {

    @Autowired
    private WindowSatisfactionStatisticsService windowSatisfactionStatisticsService;

    @Autowired
    private FeedbackInfoService feedbackInfoService;

    @Override
    public BaseService<FeedbackInfoOutput, FeedbackInfo, Integer> getService() {
        return feedbackInfoService;
    }



    /**
     * 获取分页的窗口满意率统计列表
     */
    @GetMapping(value = "findWindowSatisfactionStatistics")
    @ApiOperation("获取分页的人员满意度统计报表")
    @ApiImplicitParams({
       @ApiImplicitParam(name="startTime",value="统计开始时间",required=false,dataType="String", paramType = "query"),
       @ApiImplicitParam(name="endTime",value="统计结束时间",required=false,dataType="String", paramType = "query"),
            @ApiImplicitParam(name = "employeesNo",value = "工号",required = false,dataType = "string",paramType = "query")})
    public ResponseResult findWindowSatisfactionStatistics(HttpServletRequest request) throws Exception{
        PageData pageData = new PageData(request);
        return ResponseResult.success(windowSatisfactionStatisticsService.getWindowSatisfactionStatistics(pageData));

    }


    /**
     * 导出人员满意度统计报表信息
     *
     * @param
     * @return
     */
    @ApiOperation("导出人员满意度统计报表")
    @RequestMapping(value = "windowSatisfactionStatisticsExport", method = RequestMethod.GET)
    @ApiImplicitParams({
       @ApiImplicitParam(name="startTime",value="统计开始时间",required=false,dataType="String", paramType = "query"),
       @ApiImplicitParam(name="endTime",value="统计结束时间",required=false,dataType="String", paramType = "query"),
            @ApiImplicitParam(name = "employeesNo",value = "工号",required = false,dataType = "string",paramType = "query")})
    public ResponseResult export(HttpServletResponse response, HttpServletRequest request) {
        PageData pageData = new PageData(request);
        try {
            String str = windowSatisfactionStatisticsService.windowSatisfactionStatisticsExport(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


//    /**查询不满意*/
//    @GetMapping(value = "/selectUnstatisOrRunMany")
//    public ResponseResult selectUnstatisOrRunMany(HttpServletRequest request){
//        PageData pageData = new PageData(request);
//
//        List<FeedbackInfoOutput> feedbackInfoOutputs = windowSatisfactionStatisticsService.selectUnstatisOrRunMany(pageData);
//        return ResponseResult.success(feedbackInfoOutputs);
//    }


}
