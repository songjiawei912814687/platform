package com.attendance.controller;

import com.attendance.domian.output.OverTimeReportOutPut;
import com.attendance.mapper.mybatis.OffApplicationMapper;
import com.attendance.mapper.mybatis.OvertimeApplicationMapper;
import com.attendance.model.Users;
import com.attendance.service.OvertimeApplicationService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.ExportExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Young
 * @description: 加班报表
 * @date: Created in 17:46 2018/9/27
 * @modified by:
 */
@RestController
@RequestMapping("overTimeReport")
@Api(value = "加班报表controller",description = "加班报表操作",tags = {"加班报表操作接口"})
public class OverTimeReportController  {

    @Autowired
    private OvertimeApplicationService overtimeApplicationService;
    @Autowired
    private OffApplicationMapper offApplicationMapper;
    @Autowired
    private OvertimeApplicationMapper overtimeApplicationMapper;
    @Autowired
    private HttpServletRequest request;


    //获取某个人的上月详情加班列表
    @GetMapping(value = "getOverTimeReport")
    public ResponseResult getOverTimeReport( @RequestParam(value = "organizationId") Integer organizationId,
                                             @RequestParam(value = "employeesId")Integer  employeesNameId,
                                             @RequestParam(value = "reportDate")String reportDate){
       return ResponseResult.success(overtimeApplicationMapper.selectOverTime(organizationId,employeesNameId,reportDate));
    }

    //获取上月详情调休列表
    @GetMapping(value = "getOffTimeReport")
    public ResponseResult getOffTimeReport(@RequestParam(value = "organizationId") Integer organizationId,
                                           @RequestParam(value = "employeesId")Integer employeesNameId,
                                           @RequestParam(value = "reportDate")String reportDate){
        //需要获取上个月加班的详情
        return ResponseResult.success(offApplicationMapper.selectOffTime(organizationId,employeesNameId,reportDate));
    }

    //导出功能
    @GetMapping(value = "/exportExcel")
    @ApiOperation("导出到excel功能")
    public Object exportExcel(HttpServletResponse response, HttpServletRequest request) throws Exception {
        PageData pageData=new PageData(request);
        String a=null;
        if(pageData.containsKey("reportDate")){
            if(pageData.get("reportDate")!=null&&!pageData.get("reportDate").equals("")){
                a=pageData.get("reportDate").toString();
            }
        }
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<OverTimeReportOutPut> list = overtimeApplicationService.overTimeReport(pageData);
        String title = "加班报表";
        String excelName = "加班报表";
        if(a!=null){
            String[] strings=a.split("-");
            title=strings[0]+"月"+strings[1]+"月"+title;
        }
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs = null;
        String[]rowsName = {"序列","单位","姓名","市民卡银行卡卡号","加班次数","调休次数","合计"};
        for(int i=0;i<list.size();i++){
            OverTimeReportOutPut overTimeReportOutPut = list.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = overTimeReportOutPut.getOrganizationName();
            objs[2] = overTimeReportOutPut.getEmployeesName();
            objs[3] = overTimeReportOutPut.getBankCardNumber();
            objs[4] = overTimeReportOutPut.getOverTimeCount();
            objs[5] = overTimeReportOutPut.getRestCount();
            objs[6] = overTimeReportOutPut.getAmount();
            dataList.add(objs);
        }
        ExportExcel exportExcel = new ExportExcel(title,rowsName,dataList,excelName);
        try {
            String excelpath =  exportExcel.export(response, request);
            return ResponseResult.success(excelpath);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    //获取分页查询加班报表
    @GetMapping(value = "getReportByCondition")
    @ApiOperation("获取分页查询加班报表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="organizationName",value="组织名称",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="reportDate",value="报表日期",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="employeesName",value="人员名称",required=false,dataType="string", paramType = "query")
    })
    public ResponseResult getReportDate(String organizationName,String employeesName,String reportDate){

        if(reportDate == null){
            return ResponseResult.error("请输入时间");
        }

        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PageData pageData=new PageData(request);
        pageData.put("organizationName",organizationName);
        pageData.put("employeesName",employeesName);
        pageData.put("reportDate",reportDate);
        return ResponseResult.success(overtimeApplicationService.overTimeReport(pageData));
    }
}
