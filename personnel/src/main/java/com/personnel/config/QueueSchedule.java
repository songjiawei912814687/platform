package com.personnel.config;

import com.personnel.service.EmployeesService;
import com.personnel.service.FoodSystemService;
import com.personnel.service.WindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-05  10:54
 * @modified by:
 */
@Configuration
@EnableScheduling
public class QueueSchedule {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WindowService windowService;
    @Autowired
    private EmployeesService employeesService;
    @Autowired
    private FoodSystemService foodSystemService;

    // 每1分钟执行一次
     @Scheduled(cron = "0 0/1 * * * ?")
   public void createOrUpdateQueueWindow() {
       windowService.createOrUpdateCounter();
       logger.info("开始下发窗口到排队叫号系统");
   }

    //**********************取号叫号系统定时*************************//*
    // 每20秒执行一次
    @Scheduled(cron = "0/20 * *  * * ?")
    public void createOrUpdateQueueEmp() {
        employeesService.createOrUpdateEmployee();
        logger.info("每20秒开始下发人员到排队叫号系统");
    }
    //**********************餐盘系统定时******************************//*
    // 每5分钟执行一次
    @Scheduled(cron = "0 0/1 * * * ?")
    public void addFoodOrga() throws IOException {
        foodSystemService.updateAccDep();
        logger.info("每5分钟开始下发组织到餐盘系统");
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void delFoodOrga() throws IOException {
        foodSystemService.deleteAccDep();
        logger.info("每5分钟开始删除餐盘系统中的组织");
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void addEmp() throws IOException {
        foodSystemService.uploadAccInfo();
        logger.info("每5分钟开始下发人员到餐盘系统");
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void del() throws IOException {
        foodSystemService.updateStateAcc();
        logger.info("每5分钟开始删除人员到餐盘系统");

    }
}
