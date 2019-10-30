package com.assistdecision;

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
@EntityScan("com.assistdecision.**")
@MapperScan(value = {"com.assistdecision.mapper.mybatis"})
@EnableJpaRepositories({"com.assistdecision.mapper.jpa","com.assistdecision.core.base"})
@EnableScheduling
public class AssistdecisionApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssistdecisionApplication.class, args);
    }

}
