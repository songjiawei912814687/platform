package com.selfservice.controller;

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
@Api(value = "咨询问题controller",description = "咨询问题操作",tags = {"选择咨询问题接口"})
@RequestMapping("question")
public class QuestionController {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @ApiOperation("获得所有咨询问题")
    @GetMapping(value = "/getQuestions")
    public ResponseResult getQuestions(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        PageData pageData = new PageData(request);
        pageData.put("isOpen","1");
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/question/findPageList","knowledge",pageData,request);
        return response;
    }

    @ApiOperation("获得咨询问题")
    @GetMapping(value = "/get")
    public ResponseResult get(Integer id){
        PageData pageData=new PageData();
        pageData.put("id",id.toString());
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,"/question/findPageList","knowledge",pageData);
        return response;
    }
}
