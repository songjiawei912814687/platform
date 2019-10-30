package com.message.config;

import com.message.service.QlsxService;
import com.message.service.SMSReceiveService;
import com.message.service.SMSSendService;
import com.message.service.SynchronizeqlsxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.Calendar;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SMSReceiveService smsReceiveService;

    @Autowired
    private SMSSendService smsSendService;

    @Autowired
    private QlsxService qlsxService;

    @Autowired
    private SynchronizeqlsxService synchronizeqlsxService;

//    @Scheduled(initialDelay = 1000, fixedDelay = 200000) // 每10分钟执行一次
//    public void getToken() {
//        meipaiProccessor.login();
//        logger.info("抓去数据定时任务启动");
//    }
//
//    //第一次延迟1秒执行，当执行完后2秒再执行
//    @Scheduled(initialDelay = 1000, fixedDelay = 2000)
//    public void timerInit() {
//        System.out.println("init : "+1);
//    }


//    @Scheduled(cron = "0 */5 * * * ?") // 每5分钟执行一次
//    @Scheduled(cron = "0 3 * * * ?") // 每5分钟执行一次

    @Scheduled(cron="0/30 * *  * * ? ") // 每30秒执行一次
    public void result_timing() throws Exception {
        smsReceiveService.saveMessageReceive();
        logger.info("result_timing定时保存到收件箱中任务开始...");
    }

    /**20秒执行一次**/
    @Scheduled(cron="0/20 * *  * * ? ")
    public void timingSendFeedbackInfo()  {
        smsSendService.sendFeedbackInfo();
        logger.info("result_timing定时每20秒发送回馈短信任务开始...");
    }

    /**
     * 每天21点更新
     */
//    @Scheduled(cron = "1/1 * * * * ? ")
    @Scheduled(cron = "0 0 21 * * ?")
    public void updateOrAdd(){
        logger.info("新增或者跟新前置机数据开始...");
        try {
            synchronizeqlsxService.synchronizeDate();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("新增或者更新前置机数据开始..."+e.getMessage());
        }
        logger.info("新增或者跟新前置机数据开始...");
    }

    /**
     * 每天23点更新
     */
//    @Scheduled(cron = "1/1 * * * * ? ")
    @Scheduled(cron = "0 0 23 * * ?")
    public void sysQlsxSource(){
        try {
            logger.info("新增或者跟新权力事项源数据开始");
            qlsxService.sysQlsxSource();
        } catch (Exception e) {
            logger.error("新增或者跟新权力事项源数据发生错误..."+e.getMessage());
        }
        logger.info("新增或者跟新权力事项源数据结束");
    }
//
//    @Scheduled(cron = "0 7  * * * ?") // 每5分钟执行一次
//    public void sys_score_timing() throws Exception {
//        leanTimingService.sys_score_timing();
//        logger.info("sys_score_timing定时任务启动");
//    }
//    @Scheduled(cron = "0 0/3 * * * ?") // 每5分钟执行一次
//    //@Scheduled(cron = "0 0 0 * * ?") // 每天的0时执行
//    public void deleteTempFiles_timing() throws Exception {
//        deleteTempFilesService.DeleteTempFiles();
//        logger.info("删除临时文件定时任务启动");
//    }

    // 每天9点跑
    @Scheduled(cron = "0 30 9 * * ? ")
        public void addPerson() {
        Calendar cal = Calendar.getInstance();
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        //0为周日，6为周六
        if(week == 0 || week == 6){
            return;
        }
        //否则发送
        smsSendService.sendMeesages();
    }
}
