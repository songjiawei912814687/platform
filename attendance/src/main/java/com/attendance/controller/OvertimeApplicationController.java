package com.attendance.controller;

import com.attendance.core.base.BaseController;
import com.attendance.core.base.BaseService;
import com.attendance.domian.input.updateStateInput;
import com.attendance.domian.output.OvertimeApplicationOutput;
import com.attendance.mapper.jpa.OvertimeApplicationRepository;
import com.attendance.model.OvertimeApplication;
import com.attendance.service.AttendanceDailyService;
import com.attendance.service.OvertimeApplicationService;
import com.common.Enum.ApprovalTypeEnum;
import com.common.model.PageData;
import com.common.request.Audit;
import com.common.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: Young
 * @description: 加班controller
 * @date: Created in 23:10 2018/9/11
 * @modified by:
 */
@RestController
@RequestMapping("/overtime_application")
@Api(value = "加班controller",description = "加班操作",tags = {"加班操作接口"})
public class OvertimeApplicationController extends BaseController<OvertimeApplicationOutput, OvertimeApplication,Integer> {

    @Autowired
    private OvertimeApplicationService overtimeApplicationService;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private AttendanceDailyService attendanceDailyService;
    @Autowired
    private OvertimeApplicationRepository repository;


    @Override
    public BaseService<OvertimeApplicationOutput, OvertimeApplication, Integer> getService() {
        return overtimeApplicationService;
    }

    @Override
    @PostMapping(value = "form")
    @ApiOperation("新增/跟新加班信息")
    public ResponseResult formPost(Integer id,@Validated @RequestBody @ApiParam(name="申请加班信息",value="传入json格式",required=true) OvertimeApplication overtimeApplication) throws Exception {
        if(this.getService().getUsers().getUserType()!=0 || this.getService().getUsers().getAdministratorLevel() == 9){
            return ResponseResult.error("不是人员账号，不能发起加班申请");
        }
        return overtimeApplicationService.formPost(id, overtimeApplication);
    }

    @Override
    @GetMapping(value = "logicDelete")
    @ApiOperation("删除加班信息")
    public ResponseResult logicDelete(String idList) throws InvocationTargetException, IntrospectionException, IllegalAccessException, MethodArgumentNotValidException {
        if(idList==null){
            return ResponseResult.error(PARAM_EORRO);
        }
        //如果是跟新操作,根据传入的主键id查询该条加班记录
        OvertimeApplication overtimeApplicationCurrent = repository.findOvertimeApplicationById(Integer.parseInt(idList));
        if(overtimeApplicationCurrent == null){
            return ResponseResult.error("未找到该条加班记录");
        }
        if(!overtimeApplicationCurrent.getCreatedUserId().equals(this.getService().getUsers().getId())){
            return ResponseResult.error("不是本人创建无法删除");
        }
        ResponseResult deleteResult = super.logicDelete(idList);
        if(deleteResult.getCode()==200){
            String[] idArray = idList.split(",");
            for(String id:idArray){
                if(Audit.deleteByResourceId(loadBalancerClient,id,ApprovalTypeEnum.OVERTIME_TYPE.getCode())){
                    return ResponseResult.success();
                }
                return ResponseResult.error("删除加班审批失败");
            }

        }
        return ResponseResult.error("删除加班失败");
    }

    @Override
    @GetMapping(value = "selectById")
    public ResponseResult selectById(Integer id){
        return  super.selectById(id);
    }

    /**
     * 分页查询本月已经通过申请，以及未核销的加班列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "select_overTime_withStatus")
    public ResponseResult selectOverTimeWithStatus(@RequestParam(value = "pageNum",defaultValue = "0") Integer pageNum,
                                                   @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
       return overtimeApplicationService.selectOverTimeWithStatus(pageNum, pageSize);
    }

    @PostMapping(value = "update_state_byId")
    public ResponseResult updateStateById(@RequestBody updateStateInput updateStateInput) throws Exception {
        if(updateStateInput.getId()==null|| updateStateInput.getState()==null){
            return ResponseResult.error("请传入审核的对象");
        }
        OvertimeApplication overtimeApplication = new OvertimeApplication();
        overtimeApplication.setId(updateStateInput.getId());
        overtimeApplication.setStatus(updateStateInput.getState());
        int update = overtimeApplicationService.update(updateStateInput.getId(), overtimeApplication);
        if(update>0){
            PageData pageData = new PageData();
            pageData.put("resourceId",updateStateInput.getId());
            pageData.put("type", ApprovalTypeEnum.OVERTIME_TYPE.getCode());
            if(updateStateInput.getState()==1){
                attendanceDailyService.updateDaily(update,3,overtimeApplicationService.selectById(updateStateInput.getId()).getOrganizationId());
            }

            return ResponseResult.success();
        }else{
            return ResponseResult.error(SYS_EORRO);
        }
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的加班信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="organizationName",value="组织机构名字",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="employeesName",value="申请人名字",required=false,dataType="string", paramType = "query")
            })
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        pageData.put("userId",overtimeApplicationService.getUsers().getId().toString());
        if(overtimeApplicationService.getUsers().getAdministratorLevel()!=9){
            if(overtimeApplicationService.getUsers().getUserType()==0){
                pageData.put("employeeId",overtimeApplicationService.getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",overtimeApplicationService.getUsers().getOrganizationId().toString());
            }
        }
        return super.selectPageList(pageData);
    }

    @GetMapping(value = "export")
    public ResponseResult excel(HttpServletRequest request, HttpServletResponse response){
        try {

            String str = overtimeApplicationService.export(request,response);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.error("导出失败");

    }
}
