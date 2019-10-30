package com.selfservice.config;

import com.selfservice.service.SynicService;
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
    private SynicService synicService;

//    /**
//     * 每小时启动一次，每小时执行一次，
//     * @throws SQLException
//     */
//    @Scheduled(cron = "0 0 * * * ?")
//    public void updateOrAdd() throws SQLException {
//        synchronizeqlsxService.addOrUpdate();
//        logger.info("新增或者跟新前置机数据开始...");
//    }
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

//    @Scheduled(cron = "0 0/60 * * * ?") // 每10分钟执行一次
    @Scheduled(cron = "0 0 22 * * ?") //前置机每天21点同步到正式库，正式库每天22点同步到199微信端
    public void synicWechat() throws Exception {
        logger.info("--------------插入微信端用户指南和咨询问题数据表_开始--------------");
        synicService.synic();
        logger.info("--------------插入微信端用户指南和咨询问题数据表_结束--------------");
    }

    //    @Scheduled(cron = "0 0/5 * * * ?") // 每5分钟执行一次
    @Scheduled(cron = "0 0 22 * * ?") //前置机每天21点同步到正式库，正式库每天22点同步到199微信端
    public void synicWechat2() throws Exception {
        logger.info("--------------插入权力事项库表数据_开始--------------");
        synicService.synicQLSX();
        logger.info("--------------插入权利事项库表数据_结束--------------");
    }
}
