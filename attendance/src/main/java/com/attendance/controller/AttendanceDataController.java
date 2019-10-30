package com.attendance.controller;

import com.attendance.core.base.BaseController;
import com.attendance.core.base.BaseService;
import com.attendance.domian.output.AttendanceDailyDate;
import com.attendance.domian.output.AttendanceDataOutput;
import com.attendance.domian.output.AttendanceMonthReportOutput;
import com.attendance.mapper.mybatis.AttendanceDataMapper;
import com.attendance.mapper.mybatis.EmployeesMapper;
import com.attendance.mapper.mybatis.OrganizationMapper;
import com.attendance.model.AttendanceData;
import com.attendance.model.AttendanceStatistics;
import com.attendance.model.Organization;
import com.attendance.service.AttendanceDailyService;
import com.attendance.service.AttendanceDataService;
import com.attendance.service.AttendanceSourceService;
import com.attendance.service.AttendanceStatisticsNewService;
import com.common.model.PageData;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("ALL")
@RestController
@RequestMapping(value = "/attendanceData")
@Api(value = "考勤数据controller",description = "考勤数据操作",tags = {"考勤数据操作接口"})
@Slf4j
public class AttendanceDataController extends BaseController<AttendanceDataOutput, AttendanceData, Integer> {
    @Autowired
    private AttendanceDataService attendanceDataService;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private AttendanceDataMapper attendanceDataMapper;
    @Autowired
    private EmployeesMapper employeesMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private AttendanceDailyService attendanceDailyService;
    @Autowired
    private AttendanceSourceService attendanceSourceService;
    @Autowired
    private AttendanceStatisticsNewService attendanceStatisticsNewService;


    @Override
    public BaseService<AttendanceDataOutput,AttendanceData, Integer> getService() {
        return attendanceDataService;
    }


    @Override
    @ApiOperation("新增或修改考勤数据信息")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(
            Integer id,
            @Validated @RequestBody @ApiParam(name="考勤数据信息",value="传入json格式",required=true) AttendanceData attendanceData)
            throws Exception {
            if(attendanceData.getEmployeeId()==null||attendanceData.getEmployeeId().equals("")
                    ||attendanceData.getPunchTime()==null||attendanceData.getPunchTime().equals("")
                    ||attendanceData.getOrganizationId()==null||attendanceData.getOrganizationId().equals("")){
                return ResponseResult.error(PARAM_EORRO);
            }
        attendanceData.setAuthentication("补录");
        attendanceData.setAttendanceDeviceName("无");
        int add = attendanceDataService.add(attendanceData);
        if(add<=0){
            return  ResponseResult.error(SYS_EORRO);
        }else{
            if(!attendanceDailyService.updateDaily(add,1,attendanceData.getOrganizationId())){
                delete(""+add);
                return  ResponseResult.error(SYS_EORRO);
            }
            return ResponseResult.success();
        }
    }

    @Override
    @GetMapping(value = "delete")
    @ApiOperation(value="删除一条考勤数据信息")
    public ResponseResult delete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        return super.delete(idList);
    }

    @GetMapping(value = "findPageList")
    @ApiOperation(value="获取分页的考勤数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "no", value = "员工工号",  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "员工姓名",  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "string", paramType = "query")
    })
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData=new PageData(request);
        pageData.put("userId",attendanceDataService.getUsers().getId().toString());
        if(attendanceDataService.getUsers().getAdministratorLevel()!=9){
            if(attendanceDataService.getUsers().getUserType()==0){
                pageData.put("employeeId",attendanceDataService.getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",attendanceDataService.getUsers().getOrganizationId().toString());
            }
        }
        return attendanceDataService.selectAttendancePage(pageData);
    }

    @GetMapping(value = "findAttendanceDailyDate")
    @ApiOperation(value="获取考勤日报表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="days",value="考勤日",required=false,dataType="int", paramType = "query",example = "2018-09-29"),
            @ApiImplicitParam(name="organizationId",value="隶属部门",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="name",value="姓名",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="no",value="员工工号",required=false,dataType="string", paramType = "query")})
    public ResponseResult findAttendanceDailyDate(HttpServletRequest request){
        try {
            PageData pageData=new PageData(request);
            Integer a=0;
            Integer b=0;
            Integer c=0;
            if(pageData.getMap().get("stateType")!=null&&!pageData.getMap().get("stateType").equals("")){
              a=Integer.parseInt(pageData.getMap().get("stateType"));
            }
            if(a!=0){
                switch (a){
                    case 1:
                        pageData.put("stateName","正常");
                        break;
                    case 2:
                        pageData.put("stateName","异常");
                        break;
                }
            }

            if(pageData.getMap().get("isLeave")!=null&&!pageData.getMap().get("isLeave").equals("")){
                b=Integer.parseInt(pageData.getMap().get("isLeave"));
            }
            if(b!=0){
                switch (b){
                    case 1:
                        pageData.put("isLeave","是");
                        break;
                    case 2:
                        pageData.put("isLeave","否");
                        break;
                }
            }

            if(pageData.getMap().get("verification")!=null&&!pageData.getMap().get("verification").equals("")){
                c=Integer.parseInt(pageData.getMap().get("verification"));
            }
            if(c!=0){
                switch (c){
                    case 1:
                        pageData.put("verification","正常");
                        break;
                    case 2:
                        pageData.put("verification","异常");
                        break;
                }
            }
            pageData.put("userId",attendanceDataService.getUsers().getId().toString());
            if(attendanceDataService.getUsers().getAdministratorLevel()!=9){
                if(attendanceDataService.getUsers().getUserType()==0){
                    pageData.put("employeeId",attendanceDataService.getUsers().getEmployeeId().toString());
                }else {
                    pageData.put("orgId",attendanceDataService.getUsers().getOrganizationId().toString());
                }
            }
            if(pageData.containsKey("organizationId")&&pageData.get("organizationId")!=null&&!"".equals(pageData.get("organizationId"))){
                List<Organization> organization = organizationMapper.selectByPath(Integer.valueOf(pageData.get("organizationId").toString()));
                HashSet<Integer> ids  = new HashSet<>();
                for (Organization o:organization) {
                    ids.add(o.getId());
                }
                Object[] objects1 = ids.toArray();
                StringBuilder strs =  new StringBuilder("");
                if(objects1!=null&&objects1.length>0){
                    for (Object o:objects1) {
                        strs.append(o+",");
                    }
                    String str = strs.toString().substring(0,strs.length()-1);
                    pageData.put("path","("+ str+")");
                }
            }
            List<AttendanceDailyDate> list = attendanceDataService.findAttendanceDailyDate(pageData);
            return  ResponseResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ResponseResult.error("获取考勤日报表失败");
    }



    @GetMapping(value = "/getEmployees")
    public ResponseResult getEmployees(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/employees/selectPageListWithinAuthority","personnel",null,request);
        return response;
    }
    //请假或加班或加班模块申请通过、请假补录、考勤补录之后调整考勤日报表静态数据、员工月考核计划数据
    @GetMapping(value = "/adjustAttendanceDailyReport")
    public void adjustAttendanceDailyReport(HttpServletRequest request)throws Exception{
        attendanceDataService.adjustAttendanceDailyReport(new PageData(request));
    }

    /**
     * 导出考勤日报表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "attendanceDailyDateExport", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="days",value="考勤日",required=false,dataType="int", paramType = "query",example = "2018-09-29"),
            @ApiImplicitParam(name="organizationId",value="隶属部门",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="name",value="姓名",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="no",value="员工工号",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="jobsId",value="职务",required=false,dataType="string", paramType = "query")})
    public ResponseResult attendanceDailyDateExport(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = attendanceDataService.export(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.error("获取报表失败");
    }


    @ApiOperation("生成某一天的考勤日报表数据的临时接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="dates",value="手动生成报表时间",required=false,dataType="String", paramType = "query")})
    @RequestMapping(value = "createDailyReport", method = RequestMethod.POST)
    public ResponseResult createDailyReport(HttpServletRequest request)
            throws Exception {
        PageData pageData = new PageData(request);

        String date = pageData.get("dates").toString();
//        String date = "2019-10-12";
        attendanceStatisticsNewService.createAttendanceDailyReport(date);
        return ResponseResult.success();
    }


    @ApiOperation("生成一段时间内每一天考勤日报表数据的临时接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="beginDates",value="手动生成报表时间",required=false,dataType="String", paramType = "query"),
            @ApiImplicitParam(name="endDates",value="手动生成报表时间",required=false,dataType="String", paramType = "query")})
    @RequestMapping(value = "createDailyReports", method = RequestMethod.POST)
    public ResponseResult createDailyReports(HttpServletRequest request)
            throws Exception {
        PageData pageData = new PageData(request);

//        String beginDates = "2019-10-12";
//        String endDates = "2019-10-15";

        String beginDates = pageData.get("beginDates").toString();
        String endDates = pageData.get("endDates").toString();
        attendanceStatisticsNewService.updateStatics(beginDates,endDates);
        return ResponseResult.success();
    }



    /**
     * 考勤月报表
     * @param request
     * @return
     */
    @GetMapping(value = "findAttendanceMonthReport")
    @ApiOperation(value="获取考勤月报表")
    @ApiImplicitParams({
       @ApiImplicitParam(name="startTime",value="开始日期",required=false,dataType="String", paramType = "query",example = "2018-09-29"),
       @ApiImplicitParam(name="endTime",value="结束日期",required=false,dataType="String", paramType = "query",example = "2018-09-29"),
       @ApiImplicitParam(name="name",value="姓名",required=false,dataType="string", paramType = "query"),
       @ApiImplicitParam(name="no",value="员工工号",required=false,dataType="string", paramType = "query")})
    public ResponseResult findAttendanceMonthReport(HttpServletRequest request){
        try {
            PageData pageData = new PageData(request);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String startTime = pageData.getMap().get("startTime");
            String endTime = pageData.getMap().get("endTime");
            if(startTime == null||endTime == null){
                startTime =  sdf.format(calendar.getTime());
                Date date = new Date();
                calendar.setTime(date);
                calendar.add(Calendar.DATE,1);
                date = calendar.getTime();
                endTime = sdf.format(date);
            }
            pageData.put("startTime",startTime);
            pageData.put("endTime",endTime);
            pageData.put("userId",attendanceDataService.getUsers().getId().toString());
            if(attendanceDataService.getUsers().getAdministratorLevel()!=9){
                if(attendanceDataService.getUsers().getUserType()==0){
                    pageData.put("employeeId",attendanceDataService.getUsers().getEmployeeId().toString());
                }else {
                    pageData.put("orgId",attendanceDataService.getUsers().getOrganizationId().toString());
                }
            }
            List<AttendanceMonthReportOutput> list = attendanceDataService.findAttendanceMonthReport(pageData);
            return  ResponseResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ResponseResult.error("获取考勤月报表失败");
    }


    /**
     * 考勤异常（迟到、早退、未打卡）明细
     * @param request
     * @return
     */
    @GetMapping(value = "getUnusualDetail")
    @ApiOperation(value="获取考勤月报表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="begin",value="开始日期",required=true,dataType="String", paramType = "query",example = "2018-09-29"),
            @ApiImplicitParam(name="end",value="结束日期",required=true,dataType="String", paramType = "query",example = "2018-09-29"),
            @ApiImplicitParam(name="type",value="异常类型",required=false,dataType="Integer", paramType = "query"),
            @ApiImplicitParam(name="employeeId",value="员工id",required=false,dataType="string", paramType = "query")})
    public ResponseResult getUnusualDetail(HttpServletRequest request){
        try {
            PageData pageData = new PageData(request);
            List<AttendanceStatistics> list = attendanceDataService.getUnusualDetail(pageData);
            return  ResponseResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ResponseResult.error("获取考勤月报表失败");
    }

    /**
     * 导出考勤月报表
     * @param
     * @return
     */
    @ApiOperation(value="导出考勤月报表")
    @RequestMapping(value = "attendanceMonthExport", method = RequestMethod.GET)
    @ApiImplicitParams({
       @ApiImplicitParam(name="startTime",value="开始日期",required=false,dataType="String", paramType = "query",example = "2018-09-29"),
       @ApiImplicitParam(name="endTime",value="结束日期",required=false,dataType="String", paramType = "query",example = "2018-09-29"),
       @ApiImplicitParam(name="name",value="姓名",required=false,dataType="string", paramType = "query"),
       @ApiImplicitParam(name="no",value="员工工号",required=false,dataType="string", paramType = "query")})
    public ResponseResult attendanceMonthExport(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = attendanceDataService.attendanceMonthExport(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.error("获取报表失败");
    }


    /**
     *  落地式打卡数据
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("getLDKDate")
    @ApiImplicitParams({
            @ApiImplicitParam(name="startDate",value="开始日期",required=false,dataType="String", paramType = "query",example = "2018-09-29"),
            @ApiImplicitParam(name="endDate",value="结束日期",required=false,dataType="String", paramType = "query",example = "2018-09-29")})
    public ResponseResult getLDKDate(String startDate,String endDate){
        PageData pageData = new PageData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //如果不传时间默认拉取当天的数据
        if(startDate==null||startDate==""||endDate==null||endDate==""){
            startDate = sdf.format(cal.getTime())+" 00:00:00";
            endDate = sdf.format(cal.getTime())+" 23:59:59";
        }else{
            startDate = startDate + " 00:00:00";
            endDate = endDate + " 23:59:59";
        }
        pageData.put("startTime",startDate);
        pageData.put("endTime",endDate);
        return attendanceSourceService.getLDKDate(startDate, endDate);
    }


    /**
     *  挂壁式打卡数据
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    @GetMapping("getHikAttendance")
    @ApiImplicitParams({
            @ApiImplicitParam(name="startDate",value="开始日期",required=false,dataType="String", paramType = "query",example = "2018-09-29"),
            @ApiImplicitParam(name="endDate",value="结束日期",required=false,dataType="String", paramType = "query",example = "2018-09-29")})
    public ResponseResult getHIK(String startDate,String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        PageData pageData = new PageData();

        //如果不传时间默认拉取当天的数据
        if(startDate==null||startDate==""||endDate==null||endDate==""){
            startDate = sdf.format(cal.getTime())+" 00:00:00";
            endDate = sdf.format(cal.getTime())+" 23:59:59";
        }else{
            startDate = startDate +" 00:00:00";
            endDate = endDate + " 23:59:59";
        }
        pageData.put("startTime",startDate);
        pageData.put("endTime",endDate);
        attendanceSourceService.addData(startDate, endDate);
        return ResponseResult.success();
    }
//
//    /**初始化考勤日报表**/
//    @GetMapping("InitStatistics")
//    public void InitStatistics() throws ParseException {
//        attendanceStatisticsNewService.updateStatics();
//    }



}
