package com.api.controller;

import com.api.config.RedisComponent;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description: 传进来一个部门编号，返回该部门下员工编号的list集合
 * @date: Created in 2018-12-17  11:28
 * @modified by:
 */
@RestController
@RequestMapping("public")
public class PublicController {

    @Autowired
    private RedisComponent redisComponent;

    @GetMapping("getEmpNoList")
    public List<String> getRedis(@RequestParam String deptNo){
        String longValue = redisComponent.get(deptNo);
        String[]valueArray = longValue.split(";");
        List<String>valueList = Arrays.asList(valueArray);
        List<String> empNoList = Lists.newArrayList();
        for(String str:valueList){
            String[] value = str.split(",");
            String empNo = String.valueOf(value[0]);
            empNoList.add(empNo);
        }
        return empNoList;
    }
}
