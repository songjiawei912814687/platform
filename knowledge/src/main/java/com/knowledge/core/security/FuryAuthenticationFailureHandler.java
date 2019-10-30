package com.knowledge.core.security;

import com.common.response.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class FuryAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        StringBuffer sb = new StringBuffer();
        sb.append("{\"status\":\"error\",\"msg\":\"");
        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
            sb.append("用户名或密码输入错误，登录失败!");
        } else if (exception instanceof DisabledException) {
            sb.append("账户被禁用，登录失败，请联系管理员!");
        } else {
            sb.append("登录失败!");
        }
        sb.append("\"}");

//        response.getWriter().write(objectMapper.writeValueAsString(exception));
        response.getWriter().write(objectMapper.writeValueAsString(ResponseResult.error(sb.toString())));

    }
}
