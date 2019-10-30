package com.wechatsug;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ServletComponentScan
@Configuration
@MapperScan(value = {"com.wechatsug.mapper.mybatis"})
@EntityScan("com.wechatsug.**")
@EnableEurekaClient
@EnableDiscoveryClient
public class WechatsugApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatsugApplication.class, args);
    }
}
