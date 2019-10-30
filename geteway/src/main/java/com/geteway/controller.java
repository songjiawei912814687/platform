package com.geteway;

import com.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class controller {


    @Autowired
    private RedisComponent redisComponent;

    @GetMapping(value = "getToken")
    public ResponseResult getToken(){
        var token = UUID.randomUUID().toString();
        try{
            redisComponent.set(token,"",50L);
        }catch (Exception e){
            return ResponseResult.error("生成token失败");
        }
        return ResponseResult.success(token);
    }
}
