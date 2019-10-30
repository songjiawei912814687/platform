package com.feedback.controller;


import com.feedback.config.RedisComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(value = "test")
public class TestController {

    @Autowired
    private RedisComponent redisComponent;




    @GetMapping(value = "test")
    public void test(){
        redisComponent.set("test:10","",20L);
    }
}
