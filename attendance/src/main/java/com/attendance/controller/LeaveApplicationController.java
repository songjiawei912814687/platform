package com.attendance.controller;


import com.attendance.core.base.BaseController;
import com.attendance.core.base.BaseService;
import com.attendance.domian.input.updateStateInput;
import com.attendance.domian.output.LeaveApplicationOutput;
import com.attendance.mapper.jpa.LeaveApplicationRepository;
import com.attendance.mapper.mybatis.EmployeesMapper;
import com.attendance.mapper.mybatis.LeaveApplicationMapper;
import com.attendance.model.Employees;
import com.attendance.model.LeaveApplication;
import com.attendance.model.Users;
import com.attendance.service.AttendanceDailyService;
import com.attendance.service.AttendanceDataService;
import com.attendance.service.LeaveApplicationService;
import com.common.Enum.ApprovalTypeEnum;
import com.common.Enum.LeaveApplicationStatusEnum;
import com.common.model.PageData;
import com.common.request.Audit;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


/**
 * @author: Young
 * @description: 请假申请接口
 * @date: Created in 22:44 2018/9/11
 * @modified by:
 */
@RestController
@RequestMapping("/leave_application")
@Api(value = "请假controller",description = "请假操作",tags = {"请假申请操作接口"})
public class LeaveApplicationController extends BaseController<LeaveApplicationOutput, LeaveApplication,Integer> {

    @Autowired
    private LeaveApplicationService leaveApplicationService;
    @Autowired
    private AttendanceRuleController attendanceRuleController;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private AttendanceDataService attendanceDataService;
    @Autowired
    private AttendanceDailyService attendanceDailyService;
    @Autowired
    private EmployeesMapper employeesMapper;
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;

    private static Logger log = LoggerFactory.getLogger(AttendanceDataController.class);

    @Override
    public BaseService<LeaveApplicationOutput,LeaveApplication, Integer> getService() {
        return leaveApplicationService;
    }

    /**
     * 获取使用中的考勤规则
     * @return
     */
    @GetMapping(value = "getAttendanceRule")
    public ResponseResult getAttendanceRule(){
       return attendanceRuleController.selectInUse();
    }

    @Override
    @PostMapping(value = "form")
    @ApiOperation("新增/跟新请假信息")
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="请假人相关信息",value="传入json格式",required=true) LeaveApplication leaveApplication) throws Exception {

        var users = this.getService().getUsers();
        if(users.getUserType()!=0 || users.getAdministratorLevel() == 9){
            return ResponseResult.error("请登录人员账号发起请假申请或者编辑");
        }
        Employees employees = employeesMapper.selectByPrimaryKey(users.getEmployeeId());
        if(employees.getSex()==0&&leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.MATERNITY_LEAVE)){
            return ResponseResult.error("男性不能申请产假");
        }
        if(employees.getSex()==1&&leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.PATERNITY_LEAVE)){
            return ResponseResult.error("女性不能申请陪产假");
        }
        if(employees.getWindowState() == 1){
            if(leaveApplication.getReplacePerson() == null || "".equals(leaveApplication.getCreatedUserName())){
                log.error("窗口人员申请请假请填写替岗人员姓名");
                return ResponseResult.error("窗口人员申请请假请填写替岗人员姓名");
            }
        }
        return leaveApplicationService.formPost(id, leaveApplication);
    }


    @PostMapping(value = "formWechat")
    @ApiOperation("新增/跟新请假信息")
    public ResponseResult formWechat(Integer id, @Validated @RequestBody @ApiParam(name="请假人相关信息",value="传入json格式",required=true) LeaveApplication leaveApplication) throws Exception {

        var users = this.getService().getUsers();
        if(users.getUserType()!=0 || users.getAdministratorLevel() == 9){
            return ResponseResult.error("请登录人员账号发起请假申请或者编辑");
        }
        Employees employees = employeesMapper.selectByPrimaryKey(users.getEmployeeId());
        if(employees.getSex()==0&&leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.MATERNITY_LEAVE)){
            return ResponseResult.error("男性不能申请产假");
        }
        if(employees.getSex()==1&&leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.PATERNITY_LEAVE)){
            return ResponseResult.error("女性不能申请陪产假");
        }
        if(employees.getWindowState() == 1){
            if(leaveApplication.getReplacePerson() == null || "".equals(leaveApplication.getCreatedUserName())){
                log.error("窗口人员申请请假请填写替岗人员姓名");
                return ResponseResult.error("窗口人员申请请假请填写替岗人员姓名");
            }
        }
        return leaveApplicationService.saveLeaveWechat(id, leaveApplication);
    }

//    @PostMapping(value = "update_state_byId")
//    public ResponseResult updateStateById(@RequestBody updateStateInput updateStateInput) throws Exception {
//        if(updateStateInput.getId()==null|| updateStateInput.getState()==null){
//            return ResponseResult.error("请传入审核的对象");
//        }
//        LeaveApplication leaveApplication = new LeaveApplication();
//        leaveApplication.setId(updateStateInput.getId());
//        leaveApplication.setStatus(updateStateInput.getState());
//
//
//        int update = leaveApplicationService.update(updateStateInput.getId(), leaveApplication);
//        if(update>0){
//            PageData pageData = new PageData();
//            pageData.put("resourceId",updateStateInput.getId());
//            pageData.put("type", ApprovalTypeEnum.LEAVE_TYPE.getCode());
//            if(updateStateInput.getState()==1){
//                attendanceDailyService.updateDaily(update,2,leaveApplicationMapper.selectByPrimaryKey(updateStateInput.getId()).getOrganizationId());
//            }
//            return ResponseResult.success();
//        }else{
//            return ResponseResult.error(SYS_EORRO);
//        }
//    }

    @GetMapping("addLeaveWechat")
    public ResponseResult addLeaveWechat() throws Exception {
        leaveApplicationService.addLeaveWechat();
        return ResponseResult.success();
    }

    @PostMapping(value = "update_state_byId")
    public ResponseResult updateStateById(@RequestBody updateStateInput updateStateInput) throws Exception {
        if(updateStateInput.getId()==null|| updateStateInput.getState()==null){
            return ResponseResult.error("请传入审核的对象");
        }
        LeaveApplication leaveApplication = new LeaveApplication();
        leaveApplication.setId(updateStateInput.getId());
        leaveApplication.setStatus(updateStateInput.getState());


        int update = leaveApplicationService.update(updateStateInput.getId(), leaveApplication);
        if(update>0){
            //将审批的结果推送到微信端的请假
            leaveApplicationService.approvalResult(updateStateInput);

            PageData pageData = new PageData();
            pageData.put("resourceId",updateStateInput.getId());
            pageData.put("type", ApprovalTypeEnum.LEAVE_TYPE.getCode());
            if(updateStateInput.getState()==1){
                attendanceDailyService.updateDaily(update,2,leaveApplicationMapper.selectByPrimaryKey(updateStateInput.getId()).getOrganizationId());
            }
            return ResponseResult.success();
        }else{
            return ResponseResult.error(SYS_EORRO);
        }

    }

    @Override
    @GetMapping(value = "logicDelete")
    @ApiOperation("删除请假信息")
    public ResponseResult logicDelete(String idList) throws InvocationTargetException, IntrospectionException, IllegalAccessException, MethodArgumentNotValidException {
        if(idList==null){
            return ResponseResult.error(PARAM_EORRO);
        }

        //根据传入的id在数据库中查找该条请假申请
        LeaveApplication leaveApplicationCurrent = leaveApplicationRepository.findLeaveApplicationById(Integer.parseInt(idList));
        if (leaveApplicationCurrent == null) {
            return ResponseResult.error("未找到该条请假记录");
        }
        //如果不是本人创建无法编辑
        if(!leaveApplicationCurrent.getCreatedUserId().equals(this.getService().getUsers().getId())){
            return ResponseResult.error("非本人创建无法删除");
        }
        ResponseResult deleteResult = super.logicDelete(idList);
        if(deleteResult.getCode()==200){
            String[] idArray = idList.split(",");
            for(String id:idArray){
                if(Audit.deleteByResourceId(loadBalancerClient,id,ApprovalTypeEnum.LEAVE_TYPE.getCode())){
                    return ResponseResult.success("删除请假审批成功");
                }
                return ResponseResult.error("删除请假审批失败");
            }

        }
        return ResponseResult.error("删除请假失败");
    }

    @Override
    @GetMapping(value = "selectById")
    public ResponseResult selectById(Integer id){
       LeaveApplicationOutput leaveApplicationOutput = leaveApplicationService.selectById(id);
       if(leaveApplicationOutput == null){
           return ResponseResult.error("没有查看到详情数据");
       }
       return ResponseResult.success(leaveApplicationOutput);
    }


    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的请假信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="organizationName",value="组织机构名字",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="employeesName",value="申请人名字",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="startDate",value="请假开始时候",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="endDate",value="请假结束时间",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="applicationType",value="请假结束时间",required=false,dataType="int", paramType = "query")
                        })
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        pageData.put("userId",attendanceDataService.getUsers().getId().toString());
        if(attendanceDataService.getUsers().getAdministratorLevel()!=9){
            if(attendanceDataService.getUsers().getUserType()==0){
                pageData.put("employeeId",attendanceDataService.getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",attendanceDataService.getUsers().getOrganizationId().toString());
            }
        }
        return super.selectPageList(pageData);
    }

    /**
     * 调用获取员工的接口
     * @param request
     * @return
     */
    @GetMapping(value = "/getEmployees")
    public ResponseResult getEmployees(HttpServletRequest request){
        PageData pageData = new PageData(request);
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,"/employees/selectPageListWithinAuthority","personnel",pageData);
        return response;
    }

    /**
     * 调用获取组织的接口
     * @param request
     * @return
     */
    @GetMapping(value = "/organization/getTree")
    public ResponseResult getTree(HttpServletRequest request){
        PageData pageData = new PageData(request);
        RestTemplate restTemplate = new RestTemplate();
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/organization/getAllWithinAuthority","personnel",pageData,request);
        return response;
    }

    @GetMapping(value = "getUserInfo")
    @ApiOperation("获取当前登录人信息")
    public ResponseResult getUserIfo(){
        Users userInfo = leaveApplicationService.getCurrentUserInfo();
        return ResponseResult.success(userInfo);
    }


//    @PostMapping(value="addJob")
//    public ResponseResult addJob(@RequestBody Jobs jobs) throws JsonProcessingException {
//        PageData pageData = new PageData(request);
//        pageData.put("name", jobs.getName());
//        pageData.put("responsibilities",jobs.getResponsibilities());
//        ResponseResult response = ServiceCall.postResult(loadBalancerClient,"/jobs/form","personnel",pageData);
//        return response;
//    }



    /**4.导出到excel**/
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ResponseResult export(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = leaveApplicationService.export(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.error("导出失败");
    }

    @GetMapping(value = "getLeaveDetailByEmployeeIdAndDate")
    @ApiOperation(value="获取单个员工的请假记录")
    public ResponseResult getLeaveDetailByEmployeeIdAndDate(Integer employeeId, String date) {
        if( employeeId==null||"".equals(employeeId)||date==null||"".equals(date) ){
            ResponseResult.error(PARAM_EORRO);
        }
        PageData pageData = new PageData();
        pageData.put("employeeId",employeeId);
        pageData.put("date",date);
        return ResponseResult.success(leaveApplicationService.getLeaveDetailByEmployeeIdAndDate(pageData));
    }

    @GetMapping(value = "getLeaveDetail")
    @ApiOperation(value="获取单个员工的请假记录")
    public ResponseResult getLeaveDetail(HttpServletRequest request) {
        PageData pageData = new PageData(request);
        return ResponseResult.success(leaveApplicationService.getLeaveDetail(pageData));
    }

//    @PostMapping("calLeaveDays")
//    public ResponseResult calLeaveDays(String startDate, String endDate, String startTime, String endTime) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        double calDay = leaveApplicationService.calLeaveDays(sdf.parse(startDate),sdf.parse(endDate),startTime,endTime);
//        return ResponseResult.success(calDay);
//    }

























}
