package com.geteway.filter;

import com.geteway.RedisComponent;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class MyFilter extends ZuulFilter {
    @Autowired
    private RedisComponent redisComponent;

    private static Logger log = LoggerFactory.getLogger(MyFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if(request.getRequestURI().indexOf("api-meeting")>0){
            return false;
        }
        if(request.getRequestURI().indexOf("api-personnel")>0){
            return false;
        }
        if(request.getRequestURI().indexOf("api-attendance")>0){
            return false;
        }
        if(request.getRequestURI().indexOf("api-system")>0){
            return false;
        }
        if(request.getRequestURI().indexOf("api-api")>0){
            return false;
        }
        if(request.getRequestURI().indexOf("api-assessment")>0){
            return false;
        }
        if (request.getRequestURI().indexOf("api-message") > 0) {
            return false;
        }
        if (request.getRequestURI().indexOf("api-feedback") > 0) {
            return false;
        }
        if (request.getRequestURI().indexOf("api-knowledge") > 0) {
            return false;
        }
        if (request.getRequestURI().indexOf("api-selfservice") > 0) {
            return false;
        }
        if (request.getRequestURI().indexOf("api-wechatsug") > 0) {
            return false;
        }
        if (request.getRequestURI().indexOf("api-assistdecision") > 0) {
            return false;
        }
        if (request.getRequestURI().indexOf("api-stamp") > 0) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("token");
        if (accessToken == null) {
            log.warn("token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("token is empty");
            } catch (Exception e) {
            }

            return null;
        } else {
            var a= accessToken.toString();
            var str = redisComponent.hasKey(accessToken.toString());
            if (!str) {
                log.warn("token is failure");
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(403);
                try {
                    ctx.getResponse().getWriter().write("token is failure");
                } catch (Exception e) {
                }
                return null;
            }
        }
        log.info("ok");
        return null;
    }
}
