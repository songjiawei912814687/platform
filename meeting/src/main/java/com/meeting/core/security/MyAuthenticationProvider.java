package com.meeting.core.security;

import com.meeting.config.RedisComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    FuryUserDetailService furyUserDetailService;

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String username = authentication.getName();
        UserDetails user = null;
        String password = (String) authentication.getCredentials();
        var flag = false;
        if(password == null || "".equals(password)){
            Cookie[] cks = request.getCookies();
            if (cks != null) {
                for (int i = 0; i < cks.length; i++) {
                    Cookie ck = cks[i];

                    System.out.println(ck.getName());

                    if (ck.getName().equals("tbdkso")) {
                        var account = redisComponent.get(ck.getValue());
                        var strs = account.split("-");
                        username = authentication.getName();
                        password = (String) authentication.getCredentials();
                        if (password == null || "".equals(password)) {
                            password = strs[1];
                            flag = true;
                        } else {
                            throw new BadCredentialsException("错误密码");
                        }
                        user = furyUserDetailService.loadUserByUsername(username);

                    }
                }
            }
        }else {
            user = furyUserDetailService.loadUserByUsername(username);
        }


        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        if(flag){
            flag = password.equals(user.getPassword());
        }else {
            flag = passwordEncoder.matches(password,user.getPassword());
        }

        if(!flag){
            throw new BadCredentialsException("密码错误");
        }
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
