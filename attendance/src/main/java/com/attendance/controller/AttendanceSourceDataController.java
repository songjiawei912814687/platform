package com.attendance.controller;


import com.attendance.core.base.BaseController;
import com.attendance.core.base.BaseService;
import com.attendance.domian.output.AttendanceSourceDataOutput;
import com.attendance.model.AttendanceSourceData;
import com.attendance.service.AttendanceSourceService;
import com.common.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;


/**
 * @author: Young
 * @description:
 * @date: Created in 17:08 2018/10/9
 * @modified by:
 */
@RestController
@RequestMapping("attendance_source_data")
@Slf4j
public class AttendanceSourceDataController extends BaseController<AttendanceSourceDataOutput, AttendanceSourceData, Integer> {

    @Autowired
    private AttendanceSourceService attendanceSourceService;


    @Override
    public BaseService<AttendanceSourceDataOutput, AttendanceSourceData, Integer> getService() {
        return attendanceSourceService;
    }


    @PostMapping("formPost")
    public ResponseResult formPost(String startTimeString,String endTimeString) throws ParseException {
       attendanceSourceService.addData(startTimeString,endTimeString);
       return ResponseResult.success();
    }
}
