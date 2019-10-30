package com.selfservice.controller;

import com.common.response.ResponseResult;
import com.common.utils.HttpRequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "员工controller",description = "选择员工操作",tags = {"选择员工操作接口"})
@RequestMapping("employees")
public class EmployeesController {


    @ApiOperation("获得所有员工")
    @GetMapping(value = "/getEmployees")
    public ResponseResult getEmployees(){
        ResponseResult result = HttpRequestUtil.sendGetRequest("http://localhost:8771/employees/findEmployeesList",null);
        return result;
    }
}
