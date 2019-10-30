package com.stamp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@ServletComponentScan
@EntityScan("com.stamp.**")
@MapperScan(value = {"com.stamp.mapper.mybatis"})
@EnableJpaRepositories({"com.stamp.mapper.jpa","com.stamp.core.base"})
public class StampApplication {

    public static void main(String[] args) {
        SpringApplication.run(StampApplication.class, args);
    }
}
