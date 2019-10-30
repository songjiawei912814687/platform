package com.api.controller;

import com.api.domain.output.MeetingApply;
import com.api.service.MeetingService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.WebUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("meeting")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @GetMapping("/nextMeeting")
    @ApiOperation("获取下一个会议")
    public ResponseResult meetingList(HttpServletRequest request){
        String ip = request.getHeader("X-real-ip");
        var ip1 = getIpAddr(request);
        var ip2 = getWebIp(request);
        System.out.println("ip1----------------------------------------------"+ip1);
        System.out.println("ip2----------------------------------------------"+ip2);
        System.out.println(ip);
        if(ip == null){
            ip = "127.0.0.1";
        }
        MeetingApply meetingApply=meetingService.getNextByIp(ip);
        if(meetingApply==null){
            return ResponseResult.success("暂无会议");
        }
        return ResponseResult.success(meetingApply);
    }

    @GetMapping(value = "meetingRoom")
    public ResponseResult meetingRoom(HttpServletRequest request){
        PageData pageData = new PageData(request);
        String ip = request.getHeader("X-real-ip");
        System.out.println(ip);
        if(ip == null){
            ip = "127.0.0.1";
        }
        return ResponseResult.success(meetingService.getByIp(ip));
    }

    @GetMapping(value = "meeting")
    public ResponseResult meeting(HttpServletRequest request){
        String ip = request.getHeader("X-real-ip");
        if(ip ==  null){
            ip = "127.0.0.1";
        }
        System.out.println(ip);
        MeetingApply meetingApply=meetingService.getByTimeAndIp(ip);
        if(meetingApply==null){
            return ResponseResult.success("暂无会议");
        }
        return ResponseResult.success(meetingApply);
    }

    public static String getWebIp(HttpServletRequest request){
        var ip = "";
        try{
            ip = request.getHeader("Http-x-forwarded-for");
            var ip1 = request.getHeader("Remote-addr");
            System.out.println(ip1);
        }catch (Exception e){

        }
        return ip;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            System.out.println("ipaddrtess---------------------------------------------------------"+ipAddress);
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }


}
