package com.feedback.config;

import com.feedback.service.FeedbackInfoService;
import com.feedback.service.SuggesstionsService;
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
    private SuggesstionsService suggesstionsService;
    @Autowired
    private FeedbackInfoService feedbackInfoService;

//    @Scheduled(cron="0/60 * *  * * ? ") // 每60秒执行一次
    public void ting(){
        suggesstionsService.selectUnStatisReason();
        logger.info("查询群众回馈的不满意原因的短信任务开始");
    }


//    @Scheduled(cron="0/60 * *  * * ? ") // 每60秒执行一次
    public void re_ting(){
        suggesstionsService.selectUnStatisReasonTwo();
        logger.info("查询群众第二次回馈的不满意原因的短信任务开始");
    }

    //每天23点拉取199服务器上的投诉建议
//    @Scheduled(cron = "0 0 23 * * ?")
    public void sysnSuggesstion(){
        suggesstionsService.sync();
        logger.info("开始同步199服务器上的投诉建议");
    }

//    @Scheduled(cron = "0 30 20 * * ?")
    public void sysFeedback(){
        feedbackInfoService.sysFeedback();
        logger.info("开始同步反馈信息");
    }
}
