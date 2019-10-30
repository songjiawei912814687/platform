package com.attendance.controller;

import com.attendance.core.base.BaseController;
import com.attendance.core.base.BaseService;
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
import com.common.Enum.*;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.google.common.collect.Lists;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author: Young
 * @description: 请假补录申请接口
 * @date: Created in 22:44 2018/9/11
 * @modified by:
 */
@RestController
@RequestMapping("/leave_application_collection")
@Api(value = "请假补录controller",description = "请假补录操作",tags = {"请假补录操作接口"})
public class LeaveApplicationCollectionController extends BaseController<LeaveApplicationOutput, LeaveApplication,Integer> {

    @Autowired
    private LeaveApplicationService leaveApplicationService;
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;
    @Autowired
    private AttendanceDataService attendanceDataService;
    @Autowired
    private EmployeesMapper employeesMapper;

    @Autowired
    private AttendanceDailyService attendanceDailyService;

    @Override
    public BaseService<LeaveApplicationOutput,LeaveApplication, Integer> getService() {
        return leaveApplicationService;
    }

    @Override
    @PostMapping(value = "form")
    @ApiOperation("新增/跟新请假补录信息")
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="请假人相关信息",value="传入json格式",required=true) LeaveApplication leaveApplication) throws Exception {
        Employees employees = employeesMapper.selectByPrimaryKey(leaveApplication.getEmployeesId());
        if(employees.getSex()==0&&leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.MATERNITY_LEAVE)){
            return ResponseResult.error("男性不能申请产假");
        }
        if(employees.getSex()==1&&leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.PATERNITY_LEAVE)){
            return ResponseResult.error("女性不能申请陪产假");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //如果是哺乳假，产假,事假,陪产假，年休假
        if (leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.BREASTFEEDING_LEAVE.getCode()) ||
                leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.MATERNITY_LEAVE.getCode()) ||
                leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.PERSONAL_LEAVE.getCode()) ||
                leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.PATERNITY_LEAVE.getCode()) ||
                leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.ANNUAL_LEAVE.getCode())) {
            //设置成循环假
            leaveApplication.setIsCycle(IsCyleEnum.IS_CYLE.getCode());
        } else {
            //非循环假
            leaveApplication.setIsCycle(IsCyleEnum.UN_CYLE.getCode());
        }

        Integer orgId = leaveApplicationService.selectOrgId(leaveApplication.getEmployeesId());
        //查询出员工所属的组织id
        leaveApplication.setOrganizationId(orgId==null?leaveApplication.getOrganizationId():orgId);

        //设置为审核完成状态
        leaveApplication.setStatus(CheckStatusEnum.CHECK_FINISH.getCode());
        //设置为补录
        leaveApplication.setIsCollection(CollectionStatusEnum.IS_COLLECTION.getCode());
        String startTime = sdf.format(leaveApplication.getStartDate()).substring(0,10)+" "+leaveApplication.getStartTime();
        leaveApplication.setReportStartDate(sdf.parse(startTime));

        String endTime = sdf.format(leaveApplication.getEndDate()).substring(0,10)+" "+leaveApplication.getEndTime();
        leaveApplication.setReportEndDate(sdf.parse(endTime));

        if(startTime.equals(endTime)){
            return ResponseResult.error("开始时间与结束时间不能相同");
        }

        if(id==null){
            List<Integer> statusList = Lists.newArrayList();
            //审核完成状态和待审核状态
            statusList.add(CheckStatusEnum.CHECK_FINISH.getCode());
            statusList.add(CheckStatusEnum.UN_CHECK.getCode());
            //查询同一个人同一时间段是否有相同的申请记录
            Integer count  = leaveApplicationMapper.selectCountByEmpAndDate(leaveApplication.getEmployeesId(),
                    leaveApplication.getStartDate(),
                    leaveApplication.getEndDate(),
                    leaveApplication.getStartTime(),
                    leaveApplication.getEndTime(),  statusList);
            //如果已经存在相同的请假申请
            if (count > 0) {
                return ResponseResult.error("请勿重复申请");
            }
        }else {
            LeaveApplication leaveApplicationCurrent = leaveApplicationRepository.findLeaveApplicationById(id);

            //获取当前登录人的id
            Integer currentId = this.getService().getUsers().getId();
            if(!currentId.equals(leaveApplicationCurrent.getCreatedUserId())){
                return ResponseResult.error("非创建人不可编辑");
            }
            if(leaveApplicationCurrent.getStartDate().equals(leaveApplication.getStartDate())&&
                    leaveApplicationCurrent.getStartTime().equals(leaveApplication.getStartTime())&&
                    leaveApplicationCurrent.getEndDate().equals(leaveApplication.getEndDate())&&
                    leaveApplicationCurrent.getEndTime().equals(leaveApplication.getEndTime())){
                return ResponseResult.error("请勿重复申请");
            }
        }
        int result ;
        if (id == null) {
            result = getService().add(leaveApplication);
        } else {
            result = getService().update(id, leaveApplication);
        }
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }else {
            PageData pageData = new PageData();
            pageData.put("resourceId",result);
            pageData.put("type", ApprovalTypeEnum.LEAVE_TYPE.getCode());
            if(!attendanceDailyService.updateDaily(result,2,leaveApplication.getOrganizationId())){
                delete(""+result);
                return  ResponseResult.error(SYS_EORRO);
            }
        }
        return ResponseResult.success();
    }

    @Override
    @GetMapping(value = "logicDelete")
    @ApiOperation("删除请假补录信息")
    public ResponseResult logicDelete(String idList) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        LeaveApplication leaveApplicationCurrent = leaveApplicationRepository.findLeaveApplicationById(Integer.parseInt(idList));
        if(leaveApplicationCurrent==null){
            return ResponseResult.error("为找到该条请假补录记录");
        }
        //获取当前登录人的id
        Integer currentId = this.getService().getUsers().getId();
        if(!currentId.equals(leaveApplicationCurrent.getCreatedUserId())){
            return ResponseResult.error("非创建人不可编辑");
        }
        return super.logicDelete(idList);
    }

    @Override
    @GetMapping(value = "selectById")
    public ResponseResult selectById(Integer id){
        LeaveApplicationOutput leaveApplicationOutput =
            leaveApplicationMapper.selectIdAndIsCollectionByIdAndStatus(id,CollectionStatusEnum.IS_COLLECTION.getCode(), CheckStatusEnum.CHECK_FINISH.getCode());
        return ResponseResult.success(leaveApplicationOutput);
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的请假补录信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="organizationName",value="组织机构名字",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="employeesName",value="申请人名字",required=false,dataType="string", paramType = "query")
                        })
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        Integer currentId = this.getService().getUsers().getId();
        Integer employeesId = this.getService().getUsers().getEmployeeId();
        Integer level = this.getService().getUsers().getAdministratorLevel();
        pageData.put("isCollection",3);
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


    @GetMapping(value = "getUserInfo")
    @ApiOperation("获取当前登录人信息")
    public ResponseResult getUserIfo(){
        Users userInfo = leaveApplicationService.getCurrentUserInfo();
        return ResponseResult.success(userInfo);
    }
}
