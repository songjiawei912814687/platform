package com.knowledge.controller;

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
@Api(value = "组织controller",description = "选择组织操作",tags = {"选择组织操作接口"})
@RequestMapping("organization")
public class OrganizationController {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @ApiOperation("获得组织树")
    @GetMapping(value = "/getTree")
    public ResponseResult getTree(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        PageData pageData = new PageData(request);
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/organization/getZtree","personnel",pageData,request);
        return response;
    }
}
