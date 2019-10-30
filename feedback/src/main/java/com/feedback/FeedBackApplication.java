package com.feedback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@ServletComponentScan
@EnableScheduling
@MapperScan(value = {"com.feedback.mapper.mybatis"})
@EnableJpaRepositories({"com.feedback.mapper.jpa","com.feedback.core.base"})
@EntityScan("com.feedback.**")
@EnableDiscoveryClient
public class FeedBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedBackApplication.class, args);
    }

}
