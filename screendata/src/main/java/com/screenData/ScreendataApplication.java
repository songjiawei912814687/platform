package com.screenData;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@MapperScan(value = {"com.screenData.mapper.mybatis"})
@EnableCaching
@EnableJpaRepositories({"com.screenData.mapper.jpa","com.screenData.core.base"})
@EntityScan("com.screenData.**")
@EnableEurekaClient
@EnableDiscoveryClient
public class ScreendataApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScreendataApplication.class, args);
    }

}
