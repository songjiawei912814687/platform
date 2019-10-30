package com.common.request;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public  class ServiceCall {

    public static List<String> getCookies(LoadBalancerClient loadBalancerClient, RestTemplate restTemplate, HttpServletRequest requests,String serverName){

        ServiceInstance serviceInstance = loadBalancerClient.choose(serverName);
        String urls = String.format("http://%s:%s",serviceInstance.getHost(),serviceInstance.getPort()+"/users/login");
        HttpHeaders headers = new HttpHeaders();
        Cookie[] cks=requests.getCookies();
        List<String> cookie = new ArrayList<>();
        if(cks!=null){
            for(Cookie cookie1 : cks){
                String str = cookie1.getName()+"="+cookie1.getValue();
                cookie.add(str);
            }
            headers.put(HttpHeaders.COOKIE,cookie);
        }
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String,String> paranMap = new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(
                paranMap, headers);
        ResponseEntity<String> responseResult = restTemplate.postForEntity(urls,request,String.class);
        var a = responseResult.getHeaders();
        var cookies = a.get("Set-Cookie");
        return cookies;
    }

    public static ResponseResult getResult(LoadBalancerClient loadBalancerClient, RestTemplate restTemplate, String url, String serverName, PageData pageData,HttpServletRequest request1){
        var cookies = getCookies(loadBalancerClient,restTemplate,request1,serverName);
        if(cookies.size()<=0){
            return ResponseResult.error("无权限");
        }
        ServiceInstance serviceInstance = loadBalancerClient.choose(serverName);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE,cookies);
        MultiValueMap<String,String> paranMap = new LinkedMultiValueMap<>();
        var urls = new StringBuilder(url);
        urls.append("?r="+Math.random());
        if(pageData != null && pageData.getMap().size()>0){
            for(Map.Entry<String, String> entry : pageData.getMap().entrySet()){
                paranMap.add(entry.getKey(),entry.getValue());
                urls.append("&"+entry.getKey()+"="+entry.getValue());
            }
        }
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
                paranMap, headers);
        String url1 = String.format("http://%s:%s",serviceInstance.getHost(),serviceInstance.getPort()+urls.toString());
        request = new HttpEntity<>(null,headers);
        ResponseEntity<ResponseResult> response = restTemplate.exchange( url1, HttpMethod.GET,request, ResponseResult.class);
        return response.getBody();
    }

    public static ResponseResult getResult(LoadBalancerClient loadBalancerClient, String url, String serverName, PageData pageData){
        RestTemplate restTemplate = new RestTemplate();
        HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var cookies = getCookies(loadBalancerClient,restTemplate,request1,serverName);
        if(cookies==null){
            return ResponseResult.error("无权限");
        }
        ServiceInstance serviceInstance = loadBalancerClient.choose(serverName);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE,cookies);
        MultiValueMap<String,String> paranMap = new LinkedMultiValueMap<>();
        var urls = new StringBuilder(url);
        urls.append("?r="+Math.random());
        if(pageData != null && pageData.getMap().size()>0){
            for(Map.Entry<String, String> entry : pageData.getMap().entrySet()){
                paranMap.add(entry.getKey(),entry.getValue());
                urls.append("&"+entry.getKey()+"="+entry.getValue());
            }
        }
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
                paranMap, headers);
        String url1 = String.format("http://%s:%s",serviceInstance.getHost(),serviceInstance.getPort()+urls.toString());
        ResponseEntity<ResponseResult> response = restTemplate.exchange( url1, HttpMethod.GET,request, ResponseResult.class);
        return response.getBody();
    }

    public static ResponseResult postResult(LoadBalancerClient loadBalancerClient, String url, String serverName, PageData pageData) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        var cookies = getCookies(loadBalancerClient,restTemplate,request1,serverName);
        if(cookies.size()<=0){
            return ResponseResult.error("无权限");
        }
        ServiceInstance serviceInstance = loadBalancerClient.choose(serverName);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE,cookies);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ObjectMapper mapper = new ObjectMapper();
        var str = mapper.writeValueAsString(pageData);
        HttpEntity<String> request = new HttpEntity<String>(str, headers);
        String url1 = String.format("http://%s:%s",serviceInstance.getHost(),serviceInstance.getPort()+url);
        ResponseEntity<ResponseResult> response = restTemplate.postForEntity( url1,request, ResponseResult.class);
        return response.getBody();
    }

}
