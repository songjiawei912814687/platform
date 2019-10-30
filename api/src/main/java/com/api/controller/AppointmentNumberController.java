package com.api.controller;

import com.api.domain.output.*;
import com.api.model.AppointmentNumber;
import com.api.service.AppointmentNumberService;
import com.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-29  08:57
 * @modified by:
 */
@RestController
@RequestMapping("appointmentNumber")
public class AppointmentNumberController {

    @Autowired
    private AppointmentNumberService appointmentNumberService;

    @GetMapping("getDepts")
    public ResponseResult getDepts(){

        List<GetDeptsOutput> getDeptsOutputList = appointmentNumberService.getDepts();
        if(getDeptsOutputList == null ){
            return ResponseResult.success();
        }
        return ResponseResult.success(getDeptsOutputList);
    }

    /**获取指定部门的可预约并启用的排队队列列表*/
    @GetMapping("getBookableGroups")
    public ResponseResult GetBookableGroups(String deptCode){
        List<GetBookableGroupsOutput> getBookableGroupsOutputList = appointmentNumberService.getBookableGroups(deptCode);
        if(getBookableGroupsOutputList == null){
            return ResponseResult.success();
        }
        return ResponseResult.success(getBookableGroupsOutputList);
    }

    /**获取指定部门的可预约工作日列表信息**/
    @GetMapping("getBookableDateByDept")
    public ResponseResult getBookableDateByDept(String deptCode){
        List<GetBookableDateByDeptOutput> getBookableDateByDeptOutputs = appointmentNumberService.getBookableDateByDept(deptCode);
        if(getBookableDateByDeptOutputs == null){
            return ResponseResult.success();
        }
        return ResponseResult.success(getBookableDateByDeptOutputs);
    }

    /**查询可以预约的情况**/
    @GetMapping("getAppointmentSummary")
    public ResponseResult getAppointmentSummary(String appointDateTime,String deptCode,String groupCode) throws ParseException {
        List<AppointmentSummaryOutput> appointmentSummaryOutputs = appointmentNumberService.getAppointmentSummary(appointDateTime,deptCode,groupCode);
        if(appointmentSummaryOutputs == null){
            return ResponseResult.success();
        }
        return ResponseResult.success(appointmentSummaryOutputs);
    }


    @PostMapping("appointment")
    public ResponseResult appointment(@Valid  @RequestBody AppointmentNumber appointmentNumber){
        return appointmentNumberService.appointment(appointmentNumber);
    }

    @GetMapping("getAppointmentList")
    public ResponseResult getAppointmentList(String code,String phone){
        List<AppointmentNumberOutput> appointmentNumberOutputList = appointmentNumberService.getAppointmentList(code,phone);
        if(appointmentNumberOutputList == null){
            return ResponseResult.error("");
        }
        return ResponseResult.success(appointmentNumberOutputList);
    }

    @GetMapping("cancelAppointment")
    public ResponseResult cancelAppointment(Integer id){
        return appointmentNumberService.cancelAppointment(id);
    }


    //同步参与排队的部门列表
    @GetMapping("syscDepts")
    public ResponseResult syscDepts() throws Exception {
         appointmentNumberService.syscDepts();
         return ResponseResult.success();
    }

    //同步用户选择部门完成后获取部门下的队列列表
    @GetMapping("syscWindows")
    public ResponseResult syscWindows() throws Exception {
        appointmentNumberService.syscWindows();
        return ResponseResult.success();
    }

    //同步节假日表
    @GetMapping("syscWorkingDays")
    public ResponseResult syscWorkingDays() throws Exception {
        appointmentNumberService.syscWorkingDays();
        return ResponseResult.success();
    }

    //同步同步放号设置
    @GetMapping("syscSettingNum")
    public ResponseResult syscSettingNum() throws Exception {
        appointmentNumberService.syscSettingNum();
        return ResponseResult.success();
    }

    //同步预约信息
    @GetMapping("synAppointment")
    public ResponseResult synAppointment(String startTime,String endTime){
        appointmentNumberService.synAppointment(startTime,endTime);
        return ResponseResult.success();
    }

}
