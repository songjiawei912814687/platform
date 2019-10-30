package com.common.request;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.model.PageData;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class GetConfig {

    public static Map<String,String> getByKey(LoadBalancerClient loadBalancerClient, PageData pageData){
        RestTemplate restTemplate = new RestTemplate();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var config = ServiceCall.getResult(loadBalancerClient,restTemplate,"/config/getByKey","bigdata",pageData,request).getData();
        Map<String,String> map = new HashMap<>();
        var j = JSONUtils.toJSONString(config);
        JSONObject jsonObject = JSONObject.parseObject(j);
        map.put(jsonObject.getString("configKey"),jsonObject.getString("configValue"));
        return map;
    }


    public static Map<String,String> getListByKey(LoadBalancerClient loadBalancerClient, PageData pageData){
        RestTemplate restTemplate = new RestTemplate();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var config = ServiceCall.getResult(loadBalancerClient,restTemplate,"/config/getListByKey","bigdata",pageData,request).getData();
        Map<String,String> map = new HashMap<>();

        var j = JSONUtils.toJSONString(config);
        JSONArray jsonArray = JSONObject.parseArray(j);
        for(var str : jsonArray){
            var s = JSONUtils.toJSONString(str);
            JSONObject jsonObject = JSONObject.parseObject(s);
            map.put(jsonObject.getString("configKey"),jsonObject.getString("configValue"));
        }
        return map;
    }



}
