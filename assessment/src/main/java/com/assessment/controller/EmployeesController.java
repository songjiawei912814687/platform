package com.assessment.controller;

import com.common.model.PageData;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
@RestController
@Api(value = "员工controller",description = "选择员工操作",tags = {"选择员工操作接口"})
@RequestMapping("employees")
public class EmployeesController {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @ApiOperation("获得所有员工")
    @GetMapping(value = "/getEmployees")
    public ResponseResult getEmployees(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        PageData pageData = new PageData(request);
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/employees/selectPageListWithinAuthority","personnel",pageData,request);
        return response;
    }
}
