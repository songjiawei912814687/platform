package com.crawler.config;

import com.crawler.core.MeipaiProccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MeipaiProccessor meipaiProccessor;

//    @Scheduled(initialDelay = 1000, fixedDelay = 200000) // 每10分钟执行一次
//    public void getToken() {
//        meipaiProccessor.login();
//        logger.info("拉取鸿程数据定时任务开始");
//    }

        /**每月的1日的凌晨2点调度任务*/
        @Scheduled(cron = "0 0 2 1 * ?")
        public void getData(){
            logger.info("拉取鸿程数据定时任务开始");
            meipaiProccessor.login();
            logger.info("拉取鸿程数据定时任务结束");
        }
}
