package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.input.updateStateInput;
import com.attendance.domian.output.OffApplicationOutput;
import com.attendance.domian.output.OvertimeApplicationOutput;
import com.attendance.enums.VerificationEnum;
import com.attendance.mapper.jpa.OffApplicationRepository;
import com.attendance.mapper.mybatis.OffApplicationMapper;
import com.attendance.mapper.mybatis.OvertimeApplicationMapper;
import com.attendance.model.OffApplication;
import com.attendance.model.OvertimeApplication;
import com.common.Enum.ApprovalTypeEnum;
import com.common.Enum.CheckStatusEnum;
import com.common.model.PageData;
import com.common.request.Audit;
import com.common.response.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.attendance.core.base.BaseController.SYS_EORRO;

/**
 * @author: Young
 * @description:
 * @date: Created in 23:20 2018/9/11
 * @modified by:
 */
@Service("offApplicationService")
public class OffApplicationService extends BaseService<OffApplicationOutput, OffApplication,Integer> {

    @Autowired
    private OffApplicationRepository offApplicationRepository;
    @Autowired
    private OffApplicationMapper offApplicationMapper;
    @Autowired
    private OvertimeApplicationMapper overtimeApplicationMapper;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private OvertimeApplicationService overtimeApplicationService;
    @Autowired
    private AttendanceDataService attendanceDataService;
    @Autowired
    private AttendanceDailyService attendanceDailyService;

    @Override
    public BaseMapper<OffApplication, Integer> getMapper() {
        return offApplicationRepository;
    }

    @Override
    public MybatisBaseMapper<OffApplicationOutput> getMybatisBaseMapper() {
        return offApplicationMapper;
    }

    /**
     * 新增/跟新调休信息
     * @param id
     * @param offApplication
     * @return
     * @throws InvocationTargetException
     * @throws IntrospectionException
     * @throws MethodArgumentNotValidException
     * @throws IllegalAccessException
     */
    public ResponseResult formPost(Integer id, OffApplication offApplication) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        var result = "";
        if(id == null){
            OvertimeApplicationOutput overtimeApplicationOutput = findByOverTimeId(offApplication.getOverTimeId());
            if(overtimeApplicationOutput == null){
                return ResponseResult.error("无此加班记录");
            }

            PageData pageData = new PageData();
            Integer currentId = this.getUsers().getId();
            pageData.put("currentId",currentId);
            //查询该员工所有的加班记录
            List<OvertimeApplicationOutput> overtimeApplicationOutputList= overtimeApplicationMapper.selectAllByOrganizationAndEmployees(pageData);
            //获取加班日期列表
            List<Date>overTimeDateLists =
                    overtimeApplicationOutputList.stream().map(OvertimeApplicationOutput::getOverTimeDate).collect(Collectors.toList());

            //如果选择的调休日期小于加班日期
            if(overtimeApplicationOutput.getOverTimeDate().compareTo(offApplication.getRestTime())>=0||
                    overTimeDateLists.contains(offApplication.getRestTime())){
                return ResponseResult.error("请选择其他调休日期");
            }
            //如果用户选择的调休日期大于当月给用户返回一个错误,只能选择当月
            Calendar calendar = Calendar.getInstance();
            Integer restMonth = offApplication.getRestTime().getMonth()+1;
            if(restMonth > calendar.get(Calendar.MONTH)+1){
                return ResponseResult.error("调休日期不能大于当前月份,请重新选择调休日期");
            }
            overtimeApplicationOutput.setId(offApplication.getOverTimeId());
            //设置加班信息中已经核销的字段设置为使用中
            overtimeApplicationOutput.setVerification(VerificationEnum.IS_USE.getCode());
            OvertimeApplication overtimeApplication = new OvertimeApplication();
            BeanUtils.copyProperties(overtimeApplicationOutput,overtimeApplication);
            Integer rowCount = overtimeApplicationService.update(offApplication.getOverTimeId(),overtimeApplication);
            if(rowCount == 0){
                return ResponseResult.error("跟新加班核销状态失败");
            }
            var oId = this.add(offApplication);
            if(oId <= 0){
                return ResponseResult.error(SYS_EORRO);
            }

            if(Audit.apply(loadBalancerClient,oId,this.getUsers().getId(), ApprovalTypeEnum.ADJUST_TYPE.getCode())){
                result = "提交审批成功";
                return ResponseResult.success(result);
            }else {
                this.deleteById(String.valueOf(oId));
                result = "提交审批失败";
                return ResponseResult.error(result);
            }

        }else {

            //根据主键查询出调休信息
            OffApplication offApplicationCur = offApplicationMapper.selectByPrimaryKey(id);
            if(offApplicationCur == null){
                return ResponseResult.error("未找到该条调休信息");
            }
            if(!offApplicationCur.getCreatedUserId().equals(this.getUsers().getId())){
                return ResponseResult.error("非本人创建无法编辑");
            }

            //如果用户选择的调休日期大于当月给用户返回一个错误,只能选择当月
            Calendar calendar = Calendar.getInstance();

            Integer restMonth = offApplication.getRestTime().getMonth()+1;
            if(restMonth > calendar.get(Calendar.MONTH)+1){
                return ResponseResult.error("调休日期不能大于当前月份,请重新选择调休日期");
            }
            int rowCount = this.update(id,offApplication);
            if(rowCount == 0){
                return ResponseResult.error("跟新失败");
            }
            return ResponseResult.success("跟新成功");
        }
    }

    /**
     * 跟新审核状态
     * @param updateStateInput
     * @return
     */
    public ResponseResult updateStateById( updateStateInput updateStateInput) throws Exception {
        if(updateStateInput.getId()==null|| updateStateInput.getState()==null){
            return ResponseResult.error("请传入审核的对象");
        }
        //根据主键查询出调休信息
        OffApplication offApplication = offApplicationMapper.selectByPrimaryKey(updateStateInput.getId());
        if(offApplication == null){
            return ResponseResult.error("未查询到该条调休信息");
        }
        offApplication.setStatus(updateStateInput.getState());
        OvertimeApplication overtimeApplication = new OvertimeApplication();
        //根据调休信息中的加班id查询出一条加班信息
        OvertimeApplicationOutput overtimeApplicationOutput = overtimeApplicationMapper.selectByPrimaryKey(offApplication.getOverTimeId());
        if(overtimeApplicationOutput==null){
            return ResponseResult.error("未查询到该条加班信息");
        }
        BeanUtils.copyProperties(overtimeApplicationOutput,overtimeApplication);
        //如果是审批未通过或者删除状态
        if(CheckStatusEnum.CHECK_UN_PASS.getCode().equals(updateStateInput.getState())||
            CheckStatusEnum.CANCEL.getCode().equals(updateStateInput.getState())){
            //设置加班信息中已经核销的字段设置为未核销状态
            overtimeApplication.setVerification(VerificationEnum.UN_VERIFICATION.getCode());
        }else {
            //否则设置成已核销状态
            overtimeApplication.setVerification(VerificationEnum.IS_VERIFICATION.getCode());
        }

        //跟新数据库中这个字段
        Integer rowCount = overtimeApplicationService.update(overtimeApplication.getId(),overtimeApplication);
        if(rowCount==0){
            return ResponseResult.error("跟新加班核销状态失败");
        }
        //跟新调休信息
        offApplication = new OffApplication();
        offApplication.setId(updateStateInput.getId());
        offApplication.setStatus(updateStateInput.getState());
        rowCount = this.update(updateStateInput.getId(),offApplication);
        if(rowCount <0){
            return ResponseResult.error("跟新调休申请审核状态失败");
        }else{
            PageData pageData = new PageData();
            pageData.put("resourceId",updateStateInput.getId());
            pageData.put("type", ApprovalTypeEnum.LEAVE_TYPE.getCode());
            if(updateStateInput.getState()==1){
                attendanceDailyService.updateDaily(updateStateInput.getId(),4,offApplication.getOrganizationId());
            }
        }
        return ResponseResult.success("跟新调休申请的审核状态成功");
    }

    /**
     * 根据加班id查询出加班信息
     */
    public OvertimeApplicationOutput findByOverTimeId(Integer overTimeId){
        if(overTimeId==null){
            return null;
        }
        OvertimeApplicationOutput overtimeApplicationOutput = overtimeApplicationMapper.selectOverTimeApplicationByOverTimeId(overTimeId);
        if(overtimeApplicationOutput == null){
           return null;
        }
        return overtimeApplicationOutput;
    }

    @Override
    public OffApplicationOutput selectById(Integer id ){
       OffApplicationOutput offApplicationOutput =  offApplicationMapper.selectByPrimaryKey(id);
       if(offApplicationOutput==null){
           return null;
       }
       return offApplicationOutput;
    }
}
