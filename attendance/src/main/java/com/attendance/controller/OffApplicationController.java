package com.attendance.controller;

import com.attendance.core.base.BaseController;
import com.attendance.core.base.BaseService;
import com.attendance.domian.input.updateStateInput;
import com.attendance.domian.output.OffApplicationOutput;
import com.attendance.domian.output.OvertimeApplicationOutput;
import com.attendance.enums.VerificationEnum;
import com.attendance.mapper.mybatis.OffApplicationMapper;
import com.attendance.model.OffApplication;
import com.attendance.model.OvertimeApplication;
import com.attendance.service.OffApplicationService;
import com.attendance.service.OvertimeApplicationService;
import com.common.Enum.ApprovalTypeEnum;
import com.common.model.PageData;
import com.common.request.Audit;
import com.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: Young
 * @description:
 * @date: Created in 23:24 2018/9/11
 * @modified by:
 */
@RestController
@RequestMapping("/off_application")
@Api(value = "调休controller",description = "调休操作",tags = {"调休操作接口"})
public class OffApplicationController extends BaseController<OffApplicationOutput, OffApplication,Integer> {

    @Autowired
    private OffApplicationService offApplicationService;
    @Autowired
    private OvertimeApplicationController overtimeApplicationController;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private OvertimeApplicationService overtimeApplicationService;
    @Autowired
    private OffApplicationMapper offApplicationMapper;

    @Override
    public BaseService<OffApplicationOutput,OffApplication, Integer> getService() {
        return offApplicationService;
    }


    @Override
    @PostMapping(value = "formPost")
    @ApiOperation("新增/跟新调休信息")
    public ResponseResult formPost(Integer id,@Validated @RequestBody OffApplication offApplication) throws Exception {
        if(this.getService().getUsers().getUserType()!=0 || this.getService().getUsers().getAdministratorLevel() == 9){
            return ResponseResult.error("不是人员账号，不能发起调休申请");
        }
        return offApplicationService.formPost(id,offApplication);
    }

    @Override
    @GetMapping(value = "logicDelete")
    @ApiOperation("删除调休信息")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        if(idList==null){
            return ResponseResult.error(PARAM_EORRO);
        }
        OffApplication offApplicationCur = offApplicationMapper.selectByPrimaryKey(Integer.parseInt(idList));
        if(offApplicationCur == null){
            return ResponseResult.error("未找到该条调休信息");
        }
        if(!offApplicationCur.getCreatedUserId().equals(this.getService().getUsers().getId())){
            return ResponseResult.error("非本人创建无法删除");
        }

        String[] idArray = idList.split(",");
        for(String id:idArray){
            if(Audit.deleteByResourceId(loadBalancerClient,id, ApprovalTypeEnum.ADJUST_TYPE.getCode())){
                //如果删除成功

                OffApplicationOutput offApplicationOutput = offApplicationService.selectById(Integer.parseInt(id));
                OvertimeApplicationOutput overtimeApplicationOutput = offApplicationService.findByOverTimeId(offApplicationOutput.getOverTimeId());
                overtimeApplicationOutput.setVerification(VerificationEnum.UN_VERIFICATION.getCode());
                OvertimeApplication overtimeApplication = new OvertimeApplication();
                BeanUtils.copyProperties(overtimeApplicationOutput,overtimeApplication);
                Integer result = overtimeApplicationService.updateOverTime(overtimeApplication);
                if(result==0){
                    ResponseResult deleteResult = super.logicDelete(idList);
                    if(deleteResult.getCode()==200){
                        return ResponseResult.success("删除调休审批成功");
                    }

                }
               return ResponseResult.error("跟新加班核销信息失败");
            }
            return ResponseResult.error("删除调休审批失败");
        }
        return ResponseResult.error("删除调休失败");
    }

    @Override
    @GetMapping(value = "selectById")
    public ResponseResult selectById(Integer id){
        return  super.selectById(id);
    }

    @PostMapping(value = "update_state_byId")
    public ResponseResult updateStateById(@RequestBody updateStateInput updateStateInput) throws Exception {
        return offApplicationService.updateStateById(updateStateInput);
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的调休信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="organizationName",value="组织机构名字",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="employeesName",value="申请人名字",required=false,dataType="string", paramType = "query")
            })
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        pageData.put("userId",offApplicationService.getUsers().getId().toString());
        if(offApplicationService.getUsers().getAdministratorLevel()!=9){
            if(offApplicationService.getUsers().getUserType()==0){
                pageData.put("employeeId",offApplicationService.getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",offApplicationService.getUsers().getOrganizationId().toString());
            }
        }
        return super.selectPageList(pageData);
    }

//    /**
//     * 点击选择按钮跳出未核销以及使用中的加班信息
//     * @param pageNum
//     * @param pageSize
//     * @return
//     */
//    @GetMapping(value = "findOverTimeList")
//    public ResponseResult findOverTimeList(@RequestParam(value = "pageNum",defaultValue = "0") Integer pageNum,
//                                           @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
//        return  overtimeApplicationController.selectOverTimeWithStatus(pageNum, pageSize);
//    }
}
