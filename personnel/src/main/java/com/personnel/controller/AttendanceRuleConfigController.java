package com.personnel.controller;

import com.common.model.PageData;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
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
@Api(value = "考勤规则配置controller",description = "选择考勤规则配置操作",tags = {"选择考勤规则配置操作接口"})
@RequestMapping("attendanceRuleConfig")
public class AttendanceRuleConfigController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @ApiOperation("获得所有考勤规则配置")
    @GetMapping(value = "/getRuleConfig")
    public ResponseResult getRuleConfig(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        PageData pageData = new PageData(request);
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/attendanceRuleConfig/selectAll","attendance",pageData,request);
        return response;
    }
}
