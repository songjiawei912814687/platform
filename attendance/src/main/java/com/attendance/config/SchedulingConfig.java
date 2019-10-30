package com.attendance.config;



import com.attendance.service.*;
//import com.attendance.service.AttendanceStatisticsNewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AttendanceSourceService attendanceSourceService;

    @Autowired
    private AttendanceDailyService attendanceDailyService;
    @Autowired
    private AttendanceStatisticsNewService attendanceStatisticsNewService;
    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @Autowired
    private com.attendance.service.AttendanceDataService AttendanceDataService;

    @Autowired
    private AttendanceReminderService attendanceReminderService;


    /**
     * 每天早上6点
     * @throws Exception
     */
//    @Scheduled(cron = "0 0 6 * * ?")
    public void addAttendance() throws Exception {
        logger.info("6点定时拉取考勤源数据任务开始...");
        Calendar calendar = Calendar.getInstance();
        //获取当前时间为结束时间
        String endTimeString = sdf.format(calendar.getTime());
        calendar.add(Calendar.HOUR,-24);
        //获取前24个小时为开始时间
        String startTimeString = sdf.format(calendar.getTime());

        attendanceSourceService.addData(startTimeString,endTimeString);
        logger.info("6点定时拉取考勤源数据任务结束...");
    }

    /**
     * 每天早上8点40
     * @throws Exception
     */
//    @Scheduled(cron = "0 40 8 * * ?")
    public void addAttendance1() throws Exception {
        logger.info("8点40定时拉取考勤源数据任务开始...");
        Calendar calendar = Calendar.getInstance();
        //获取当前时间为结束时间
        String endTimeString = sdf.format(calendar.getTime());
        calendar.add(Calendar.HOUR,-24);
        //获取前24个小时为开始时间
        String startTimeString = sdf.format(calendar.getTime());

        attendanceSourceService.addData(startTimeString,endTimeString);
        logger.info("8点40定时拉取考勤源数据任务结束...");
    }

    /**
     * 每天晚上11点
     * @throws Exception
     */
//    @Scheduled(cron = "0 0 23 * * ?")
    public void addAttendance2() throws Exception {
        logger.info("23点定时拉取考勤源数据任务开始...");
        Calendar calendar = Calendar.getInstance();
        //获取当前时间为结束时间
        String endTimeString = sdf.format(calendar.getTime());
        calendar.add(Calendar.HOUR,-24);
        //获取前24个小时为开始时间
        String startTimeString = sdf.format(calendar.getTime());

        attendanceSourceService.addData(startTimeString,endTimeString);
        logger.info("23点定时拉取考勤源数据任务结束...");
    }
    /**
     * 每天早上5点半拉取落地式考勤机数据
     */
//    @Scheduled(cron = "0 30 5 * * ?")
    public void getMorningLDKDate() {

        logger.info("5点半定时拉取落地式考勤机考勤源数据任务开始...");
        Calendar calendar = Calendar.getInstance();
        //获取当前时间为结束时间
        String endTime = sdf.format(calendar.getTime());
        calendar.add(Calendar.HOUR,-12);
        //获取前5个小时为开始时间
        String startTime = sdf.format(calendar.getTime());
        attendanceSourceService.getLDKDate(startTime,endTime);
        logger.info("5点半定时拉取拉取落地式考勤机考勤源数据任务结束...");
    }

    /**
     * 每天早上8点30拉取落地式考勤机数据
     */
//    @Scheduled(cron = "0 30 8 * * ?")
    public void getMorningLDKDate1() {

        logger.info("8点30定时拉取落地式考勤机考勤源数据任务开始...");
        Calendar calendar = Calendar.getInstance();
        //获取当前时间为结束时间
        String endTime = sdf.format(calendar.getTime());
        calendar.add(Calendar.HOUR,-12);
        //获取前5个小时为开始时间
        String startTime = sdf.format(calendar.getTime());
        attendanceSourceService.getLDKDate(startTime,endTime);
        logger.info("8点30定时拉取拉取落地式考勤机考勤源数据任务结束...");
    }

    /***晚上22点拉取*/
//    @Scheduled(cron = "0 0 22 * * ?")
    public void getAfternoonLDKDate(){
        logger.info("22点定时拉取落地式考勤机考勤源数据任务开始...");
        Calendar calendar = Calendar.getInstance();
        //获取当前时间为结束时间
        String endTime = sdf.format(calendar.getTime());
        calendar.add(Calendar.HOUR,-12);
        //获取前12个小时为开始时间
        String startTime = sdf.format(calendar.getTime());
        attendanceSourceService.getLDKDate(startTime,endTime);
        logger.info("22点拉取拉取落地式考勤机考勤源数据任务结束");
    }

//    @Scheduled(cron = "0 0/5 * * * ?")
    public void addLeaveWechat() throws Exception {
        logger.info("-----------每5分钟拉取微信请假信息开始----------");
        leaveApplicationService.addLeaveWechat();
        logger.info("-----------每5分钟拉取微信请假信息结束----------");
    }

//    //定时凌晨两点开始生成前台的考勤日报表
////    @Scheduled(cron = "0 0/1 * * * ?")  //每隔一分钟运行
////    @Scheduled(cron = "0 0 2 * * ?")  //每天凌晨两点运行运行
//    public void attendanceDailyReport() throws Exception {
//        logger.info("---------------------------生成考勤日报表开始------------------------------------------");
//        AttendanceDataService.createAttendanceDailyDate(null);
//        logger.info("---------------------------生成考勤日报表结束------------------------------------------");
//    }
//
//
//    //定时凌晨两点开始生成前台的考勤日报表
//    //    @Scheduled(cron = "0 0 2 * * ?")  //每天凌晨两点运行运行
//    public void createattendanceDailyReport() throws Exception {
//        logger.info("---------------------------生成考勤日报表开始------------------------------------------");
//        attendanceDailyService.createAttendanceDailyDate(null);
//        logger.info("---------------------------生成考勤日报表结束------------------------------------------");
//    }

    //定时凌晨2点开始生成前台的考勤日报表
//    @Scheduled(cron = "0 0 2 * * ?")  //每天凌晨两点运行运行
    public void creatAttendanceDailyReport() throws Exception {
        logger.info("---------------------------生成考勤日报表开始------------------------------------------");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //获取上一天的日期
        cal.add(Calendar.DATE,-1);

        String reportDate = sdf.format(cal.getTime());
        attendanceStatisticsNewService.createAttendanceDailyReport(reportDate);
        logger.info("---------------------------生成考勤日报表结束------------------------------------------");
    }

    //定时凌晨7点开始生成前一天的考勤日报表
//    @Scheduled(cron = "0 0 7 * * ?")  //每天凌晨两点运行运行
    public void creatAttendanceDailyReport1() throws Exception {
        logger.info("---------------------------生成考勤日报表开始------------------------------------------");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //获取上一天的日期
        cal.add(Calendar.DATE,-1);

        String reportDate = sdf.format(cal.getTime());
        attendanceStatisticsNewService.createAttendanceDailyReport(reportDate);
        logger.info("---------------------------生成考勤日报表结束------------------------------------------");
    }


    //定时9点10开始生成前一天的考勤日报表
//    @Scheduled(cron = "0 10 9 * * ?")  //每天凌晨两点运行运行
    public void creatAttendanceDailyReport2() throws Exception {
        logger.info("---------------------------生成考勤日报表开始------------------------------------------");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //获取上一天的日期
        cal.add(Calendar.DATE,-1);

        String reportDate = sdf.format(cal.getTime());
        attendanceStatisticsNewService.createAttendanceDailyReport(reportDate);
        logger.info("---------------------------生成考勤日报表结束------------------------------------------");
    }



//    /**
//     * 每天早上7点40
//     * @throws Exception
//     */
////    @Scheduled(cron = "0 20 8 * * ?")
//    public void addData()  throws Exception {
//        attendanceSourceService.addData();
//        logger.info("8点20点定时拉取考勤源数据任务开始...");
//        logger.info("---------------------------考勤早上打卡提醒开始------------------------------------------");
//        attendanceReminderService.sendMessage(null);
//        logger.info("---------------------------考勤早上下卡提醒结束------------------------------------------");
//    }
//
//    /**
//     * 每天下午4点40
//     * @throws Exception
//     */
////    @Scheduled(cron = "0 45 16 * * ?")
//    public void addDatas() throws Exception {
//        attendanceSourceService.addData();
//        logger.info("16点40点定时拉取考勤源数据任务开始...");
//        logger.info("---------------------------考勤下午打卡提醒开始------------------------------------------");
//        attendanceReminderService.sendMessages(null);
//        logger.info("---------------------------考勤下午下卡提醒结束------------------------------------------");
//    }
}
