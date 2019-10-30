package com.assessment.config;
import com.assessment.model.Users;
import com.assessment.service.AppraisalPlanService;
import com.assessment.service.AppraisalTemplateService;
import com.assessment.service.SynchronizeBjsbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Configuration
@EnableScheduling
public class SchedulingConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AppraisalPlanService appraisalPlanService;

    @Autowired
    AppraisalTemplateService appraisalTemplateService;

    @Autowired
    SynchronizeBjsbService synchronizeBjsbService;

    //每月的1号的1点生成考核计划
//    @Scheduled(cron = "1/1 * * * * ? ")  //每隔一秒运行
    @Scheduled(cron = "0 0 1 1 * ?")  //每天凌晨一点运行
    public void attendanceDailyReport() {
        logger.info("---------------------------生成月度考核计划表开始------------------------------------------");
        appraisalTemplateService.timingCreatePlanAndPlanDetail(null,null,null);
        logger.info("---------------------------生成月度考核计划表结束------------------------------------------");
    }

//        @Scheduled(cron = "1/1 * * * * ? ")  //每隔一秒运行
    @Scheduled(cron = "0 0 1 2 * ? ") // 每月2号凌晨1点跑
    public void synchronizeBjsb(){
        logger.info("-----------------------------------同步办件库开始-----------------------------------");
        try {
            synchronizeBjsbService.synchronizeDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("---------------------------同步办件库结束------------------------------------------");
    }


    @Scheduled(cron = "0 0 2 2 * ? ") // 每月2号凌晨2点跑
    public void setDepartmentPlanScore() {
        logger.info("-----------------------------------部门考核计划打分开始-----------------------------------");
        Users users = new Users();
        users.setUsername("admin");
        users.setId(4997);
        appraisalPlanService.setDepartmentPlanScore(null,users,null);
        logger.info("---------------------------生成月度考核计划表结束------------------------------------------");
    }
//
    @Scheduled(cron = "0 0 3 * * ? ") // 每天凌晨3点跑
//    @Scheduled(cron = "0 0/5 * * * ?") // 每5分钟执行一次
    public void updateFinalScore() {
        logger.info("-----------------------------------员工考核计划打分开始-----------------------------------");
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String tp = simpleDateFormat.format(date);
        String[] strings=tp.split("-");
        Integer year=Integer.parseInt(strings[0]);
        Integer month=Integer.parseInt(strings[1]);
        Integer day=Integer.parseInt(strings[2]);
        if(day==1){
            if(month==1){
                year=year-1;
                month=12;
            }else {
                month=month-1;
            }
        }
        String dates=year+"-"+month;
        if(month<10){
            dates=year+"-0"+month;
        }
        try {
            appraisalPlanService.updateFinalScore(dates);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (MethodArgumentNotValidException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
