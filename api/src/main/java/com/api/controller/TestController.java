package com.api.controller;

import com.api.config.RedisComponent;
import com.api.service.TimeService;
import com.common.response.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(value = "test")
public class TestController {


    @Autowired
    private TimeService TimeService;
    @Autowired
    private RedisComponent redisComponent;

    @GetMapping(value = "getToken")
    public ResponseResult getToken(){
        var token = UUID.randomUUID().toString();
        try{
            redisComponent.set("leee","",50L);
        }catch (Exception e){
            return ResponseResult.error("生成token失败");
        }
        return ResponseResult.success(token);
    }

    @GetMapping(value = "getDays")
    @ApiImplicitParams({
            @ApiImplicitParam(name="days",value="时间",required=false,dataType="string", paramType = "query")
    })
    public ResponseResult getDays(String date,int days){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = sdf.parse(date);
            return ResponseResult.success(TimeService.getWorkDays(parse,days));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "test")
    public void test(){
        redisComponent.set("apiTest:10","",20L);
    }
}
