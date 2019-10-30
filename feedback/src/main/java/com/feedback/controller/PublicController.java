package com.feedback.controller;

import com.common.model.PageData;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Api(value = "公用controller",description = "公用操作",tags = {"公用操作接口"})
@RequestMapping("public")
public class PublicController {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @ApiOperation("获得组织树")
    @GetMapping(value = "/getOrganizationTree")
    public ResponseResult getTree(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/organization/getZtree","personnel",null,request);
        return response;
    }

    @ApiOperation("获得部门下窗口")
    @GetMapping(value = "/getWindows")
    @ApiImplicitParams({
            @ApiImplicitParam(name="organizationId",value="部门id",required=false,dataType="int", paramType = "query")})
    public ResponseResult getWindows(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        PageData pageData = new PageData(request);
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/window/selectAll","personnel",pageData,request);
        return response;
    }

    @ApiOperation("获得部门下员工")
    @GetMapping(value = "/getEmployeesByWindowsId")
    @ApiImplicitParams({
            @ApiImplicitParam(name="windowsId",value="窗口id",dataType="int", paramType = "query")})
    public ResponseResult getEmployeesByWindowsId(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        PageData pageData = new PageData(request);
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/employees/selectByWindowId","personnel",pageData,request);
        return response;
    }

    @ApiOperation("获得组织树")
    @GetMapping(value = "/getZtreeWithinAuthority")
    public ResponseResult getZtreeWithinAuthority(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/organization/getZtreeWithinAuthority","personnel",null,request);
        return response;
    }

    @ApiOperation("获得所有员工")
    @GetMapping(value = "/getEmployees")
    public ResponseResult getEmployees(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        PageData pageData = new PageData(request);
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/employees/selectPageListWithinAuthority","personnel",pageData,request);
        return response;
    }

    @ApiOperation("获取所有指标")
    @GetMapping(value = "/selectAllAppraisalIndex")
    public ResponseResult selectAllAppraisalIndex(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        PageData pageData = new PageData(request);
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/appraisalIndex/selectAll","ASSESSMENT",pageData,request);
        return response;
    }

    @ApiOperation("获取所有指标规则")
    @GetMapping(value = "/selectAllAppraisalRule")
    public ResponseResult selectAllAppraisalRule(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        PageData pageData = new PageData(request);
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/appraisalRule/selectAll","ASSESSMENT",pageData,request);
        return response;
    }

    @GetMapping(value = "getRuleByIndexId")
    public ResponseResult getRuleByIndexId(HttpServletRequest request){
        PageData pageData = new PageData(request);
        ResponseResult result = ServiceCall.getResult(loadBalancerClient,"/appraisalRule/getRuleByIndexId","ASSESSMENT",pageData);
        return result;
    }

    @GetMapping(value = "findDepartmentPlan")
    public ResponseResult findDepartmentPlan(HttpServletRequest request){
        PageData pageData = new PageData(request);
        ResponseResult result = ServiceCall.getResult(loadBalancerClient,"/appraisalplan/findDepartmentPlanWithNoPage","ASSESSMENT",pageData);
        return result;
    }
}
