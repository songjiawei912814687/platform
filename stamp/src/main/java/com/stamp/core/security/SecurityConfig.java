package com.stamp.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsUtils;

/**
 * @author: young
 * @project_name: platform
 * @description: security第一步总配置
 * @date: Created in 2019-04-12  09:36
 * @modified by:
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(-1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private FuryAuthenticationFailureHandler furyAuthenticationFailureHandler;
    @Autowired
    private FuryAuthenticationSuccessHandler furyAuthenticationSuccessHandler;
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginProcessingUrl("/users/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(furyAuthenticationSuccessHandler)
                .failureHandler(furyAuthenticationFailureHandler)
                .and()
                .logout()
                .permitAll()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/users/login","/users/sendVerificationCode","/users/vCodeAndChangePwd")
                .permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .cors()
                .and()
                .csrf()
                .ignoringAntMatchers("/users/login")
                .disable()
                .headers()
                .frameOptions()
                .sameOrigin();
    }
}
