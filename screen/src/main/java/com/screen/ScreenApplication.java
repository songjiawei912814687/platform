package com.screen;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@MapperScan(value = {"com.screen.mapper.mybatis"})
@EnableCaching
@EnableJpaRepositories({"com.screen.mapper.jpa","com.screen.core.base"})
@EntityScan("com.screen.**")
@EnableEurekaClient
@EnableDiscoveryClient
public class ScreenApplication {
    public static void main(String[] args) {
        {
            SpringApplication.run(ScreenApplication.class);
        }
    }
}
