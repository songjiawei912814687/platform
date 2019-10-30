package com.knowledge.config;


import com.knowledge.service.MaterialListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


/**
 * @author admin
 */
@Configuration
@EnableScheduling
@Slf4j
public class SchedulingConfig {

    @Autowired
    private MaterialListService materialListService;


    /**凌晨3点执行*/
    @Scheduled(cron = "0 0 3 * * ?")
    public void updatematerList() throws Exception {
        materialListService.updatematerList();
    }
}
