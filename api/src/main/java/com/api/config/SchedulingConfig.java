package com.api.config;

import com.api.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
@EnableScheduling
public class SchedulingConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WindowEmployeesService windowEmployeesService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private StationPeopleService stationPeopleService;
    @Autowired
    private AppointmentNumberService appointmentNumberService;
    @Autowired
    private EvaluationResultService evaluationResultService;


    public final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


//    @Scheduled(cron = "0 0 0 * * ? ") // 每天凌晨0点跑
    public void updateWindowEmployees(){
        try {
            windowEmployeesService.addWindowEmployees();
            windowEmployeesService.updateWindowEmployee();
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

    // 每30秒执行一次
//    @Scheduled(cron="0/30 * *  * * ? ")
    public void updateQue(){
        organizationService.getMList();
        logger.info("跟新排队数据开始");
    }

    //每天18点 10分 定时执行一次
//    @Scheduled(cron="0 10 18 * * ?")
    public void getStationPeople() throws Exception {
        logger.info("从sqlServer取出取号源数据开始");
        Calendar calendar = Calendar.getInstance();
        String endTime = sdf.format(calendar.getTime());

        calendar.add(Calendar.HOUR,-12);
        String startTime = sdf.format(calendar.getTime());

        stationPeopleService.getStationPeople(startTime,endTime);
        logger.info("从sqlServer取出取号源数据结束");
    }
    //每天18点 10分 定时执行一次
//    @Scheduled(cron="0 12 18 * * ?")
    public void getEvalResult() throws Exception {
        logger.info("从sqlServer取出评价源数据开始");
        Calendar calendar = Calendar.getInstance();
        String endTime = sdf.format(calendar.getTime());

        calendar.add(Calendar.HOUR,-12);
        String startTime = sdf.format(calendar.getTime());
        stationPeopleService.getEvalResult(startTime,endTime);
        logger.info("从sqlServer取出评价源数据结束");
    }

    //每天定时18:15点执行
//    @Scheduled(cron="0 15 18 * * ?")
    public void sysStationPeople(){
        logger.info("同步取号源表数据到取号中间表开始");
        stationPeopleService.sysStationPeople();
        logger.info("同步取号源表数据到取号中间表结束");
    }
    //每天定时18:15点执行
//    @Scheduled(cron="0 17 18 * * ?")
    public void sysEvalResult(){
        logger.info("同步评价结果源数据到评价中间表开始");
        stationPeopleService.sysEvalResult();
        logger.info("同步评价结果源数据到评价中间表结束");
    }

    //每天定时18:20点执行
//    @Scheduled(cron="0 20 18 * * ?")
    public void synFeedBack(){
        logger.info("同步评价结果源表到反馈信息开始");
        stationPeopleService.synFeedBack();
        logger.info("同步评价结果源表到反馈信息结束");
    }

    //8点到19点每5分钟执行一次
//    @Scheduled(cron = "0 0/5 8-19 * * ?")
    public void sysSourceCounter() throws Exception {
        logger.info("8点到19点每5分钟同步窗口登录信息开始");
        stationPeopleService.sysSourceCounter();
        logger.info("8点到19点每5分钟同步窗口登录信息结束");
    }

    //
//    @Scheduled(cron = "0 0 1 * * ? ") // 每天凌晨1点跑
    public void syscDepts() throws Exception {
        logger.info("每天凌晨1点同步部门表数据到154上开始");
        appointmentNumberService.syscDepts();
        logger.info("每天凌晨1点同步部门表数据到154上结束");
    }

//    @Scheduled(cron = "0 0 1 * * ? ") // 每天凌晨1点跑
    public void syscWindows() throws Exception {
        logger.info("每天凌晨1点同步业务(队列)表数据到154上开始");
        appointmentNumberService.syscWindows();
        logger.info("每天凌晨1点同步业务(队列)表数据到154上结束");
    }

    @Scheduled(cron = "0 0 1 * * ? ") // 每天凌晨1点跑
    public void syscWorkingDays() throws Exception {
        logger.info("每天凌晨1点同步节假日表数据到154上开始");
        appointmentNumberService.syscWorkingDays();
        logger.info("每天凌晨1点同步节假日表数据到154上结束");
    }

//    @Scheduled(cron = "0 0 12 * * ? ") // 每天12点跑
    public void syscDepts1() throws Exception {
        logger.info("每天12点同步部门表数据到154上开始");
        appointmentNumberService.syscDepts();
        logger.info("每天12点同步部门表数据到154上结束");
    }


//    @Scheduled(cron = "0 0 12 * * ? ") // 每天12点跑
    public void syscWindows1() throws Exception {
        logger.info("每天12点同步业务(队列)表数据到154上开始");
        appointmentNumberService.syscWindows();
        logger.info("每天12点同步业务(队列)表数据到154上结束");
    }

//    @Scheduled(cron = "0 0 12 * * ? ") // 每天12点跑
    public void syscWorkingDays1() throws Exception {
        logger.info("每天12点同步节假日表数据到154上开始");
        appointmentNumberService.syscWorkingDays();
        logger.info("每天12点同步节假日表数据到154上结束");
    }

//    @Scheduled(cron = "0 0 2 * * ? ")
    public void synAppointment(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = sdf.format(new Date())+" 00:00:00";
        String endTime = sdf.format(new Date())+" 23:59:59";
        logger.info("每天2点同步预约信息开始");
        appointmentNumberService.synAppointment(startTime,endTime);
        logger.info("每天2点同步预约信息开始");
    }



//    @Scheduled(cron = "0 0 20 * * ? ")
    public void saveBiz(){
        logger.info("每天20点业务类型信息更新上传到税务系统开始");
        try {
            stationPeopleService.saveBiz();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        logger.info("每天20点业务类型信息更新上传到税务系统结束");
    }

//    @Scheduled(cron = "0 30 20 * * ? ")
    public void saveEva() throws SQLException,ClassNotFoundException{
        logger.info("每天20点30分评价代码数据更新上传到税务系统开始");
        evaluationResultService.saveeva();
        logger.info("每天20点30分评价代码数据更新上传到税务系统结束");
    }

}
