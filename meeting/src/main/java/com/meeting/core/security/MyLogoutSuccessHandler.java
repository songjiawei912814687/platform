package com.meeting.core.security;

import com.common.response.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeting.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {


    @Autowired
    private ObjectMapper objectMapper; // Json转化工具
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Users user = (Users) authentication.getPrincipal();
        response.setContentType("application/json;charset=UTF-8"); // 响应类型
        response.getWriter().write(objectMapper.writeValueAsString(ResponseResult.success(null)));
    }
}
