package com.personnel.config;
import com.personnel.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@Component
@Configuration
@EnableScheduling
public class SchedulingConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DepartmentNumbersService departmentNumbersService;

    @Autowired
    private PersonService personService;

    @Autowired
    private CityCardService cityCardService;

    @Autowired
    private PlateService plateService;

    @Autowired
    StationEmployeesService stationEmployeesService;


//    @Scheduled(cron = "0 0 0 1 * ?") // 每个月1号1点执行
//    @Scheduled(cron = "0 0/1 * * * ?") // 每1分钟执行一次
    public void setDepartmentNumbers() {
        logger.info("-----------------------------------同步部门人数开始-----------------------------------");
        int i = departmentNumbersService.setDepartmentNumbers();
        if(i<0){
            logger.info("------------------------------同步部门人数失败------------------------------");
        }
        logger.info("-----------------------------------同步部门人数结束-----------------------------------");
        logger.info("抓去数据定时任务启动");
    }
//    @Scheduled(cron = "0 0/1 * * * ?") // 每1分钟执行一次
    public void addPerson() throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        logger.info("-----------------------------------人员下发海康平台开始--------------------------------");
        personService.addPerson();
    }


    @Scheduled(cron = "0 0/1 * * * ?") // 每1分钟执行一次
    public void deletePerson() throws IntrospectionException, MethodArgumentNotValidException, IllegalAccessException, InvocationTargetException {
        logger.info("-----------------------------------人员从海康平台删除开始------------------------------");
        personService.deletePerson();
    }

//    @Scheduled(cron = "0 0/1 * * * ?") // 每1分钟执行一次
    public void addCarInfo() throws Exception {
        logger.info("-----------------------------------从海康平台下发车牌开始------------------------------");
        plateService.addCarInfo();
    }

//    @Scheduled(cron = "0 0/1 * * * ?") // 每2分钟执行一次
//    public void addCard() throws Exception {
//        logger.info("-----------------------------------从海康平台下发车牌开始------------------------------");
//        plateService.addcard();
//    }

//    @Scheduled(cron = "0 0/1 * * * ?") // 每2分钟执行一次
    public void deleteCarInfo() throws Exception {
        logger.info("-----------------------------------从海康平台删除车牌开始------------------------------");
        plateService.deleteCarInfo();
    }


//    @Scheduled(cron = "0 0/1 * * * ?") // 每1分钟执行一次
    public void addCityCard() throws IntrospectionException, MethodArgumentNotValidException, IllegalAccessException, InvocationTargetException {
        logger.info("-----------------------------------市民卡下发海康平台开始-----------------------------------");
        cityCardService.addCityCard();
    }

//    @Scheduled(cron = "0 0/1 * * * ?") // 每1分钟执行一次
    public void addCityCards() {
        logger.info("-----------------------------------市民卡下发海康平台开始-----------------------------------");
        cityCardService.addCityCards();
    }

//    @Scheduled(cron = "0 0 21 * * ? ")
    public void postStationPeople(){
        logger.info("------------------------将窗口人员信息更新上传到税务系统开始----------------------------------");
        stationEmployeesService.saveAcc();
    }

//    @Scheduled(cron = "0 30 17 * * ? ") // 每天下午4点跑
//    public void sendMessage(){
//        logger.info("-----------------------------------平台开始-----------------------------------");
//        employeesService.sendMessage();
//    }

//    @Scheduled(cron = "0 0/3 * * * ?") // 每3分钟执行一次
//    public void addcard() throws Exception {
//        logger.info("-----------------------------------新增车牌开始------------------------------");
//        plateService.addcard();
//    }
}
