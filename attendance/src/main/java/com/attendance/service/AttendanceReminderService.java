package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttendanceDailyDate;
import com.attendance.domian.output.HolidayOutput;
import com.attendance.domian.output.LeaveApplicationOutput;
import com.attendance.mapper.jpa.AttendanceRuleRepository;
import com.attendance.mapper.jpa.AttendanceStatisticsRepository;
import com.attendance.mapper.mybatis.*;
import com.attendance.model.AttendanceRule;
import com.attendance.model.AttendanceStatistics;
import com.common.Enum.MessageTypeEnum;
import com.common.model.PageData;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import com.common.utils.GetMessageTemplate;
import com.common.utils.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AttendanceReminderService extends BaseService<AttendanceStatistics,AttendanceStatistics,Integer> {
    @Autowired
    private AttendanceDataMapper attendanceDataMapper;

    @Autowired
    private AttendanceStatisticsRepository attendanceStatisticsRepository;

    @Autowired
    private AttendanceStatisticsMapper attendanceStatisticsMapper;

    @Autowired
    private HolidayMapper holidayMapper;

    @Autowired
    private AttendanceRuleRepository attendanceRuleRepository;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;


    @Override
    public BaseMapper<AttendanceStatistics, Integer> getMapper() {
        return attendanceStatisticsRepository;
    }

    @Override
    public MybatisBaseMapper<AttendanceStatistics> getMybatisBaseMapper() {
        return attendanceStatisticsMapper;
    }

    private boolean isNotWorkDays(String days) {
        //若是节假日管理中的非工作日，则为非工作日
        List<HolidayOutput> list = holidayMapper.isHoliday(days);
        if(list!=null&&list.size()>0){
            return  true;
        }
        //若不是节假日管理中的非工作日，判断是否为节假日管理中的工作日
        List<HolidayOutput> workDayList = holidayMapper.isWorkDay(days);
        if(workDayList!=null&&workDayList.size()>0){
            return  false;
        }
        //若非节假日管理中的非工作日和工作日则判断是否为周六周天，若为周六或者周天则为非工作日
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format1.parse(days);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    public  void  sendMessage(Date day) throws Exception {
        PageData pageData = new PageData();
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String days = "";
        if(day ==null){
            //获得前一天的时间
            instance.setTime(new Date());
            days =sdf.format(instance.getTime());
        }else{
            instance.setTime(day);
            days =sdf.format(day);
        }
        pageData.put("days",days);
        pageData.put("date",days);
        //3.1 获得上下班时间
        String workTime = days+" 8:00:00";
        String workOutTime = days+" 16:30:00";
        List<AttendanceRule> workingHours = attendanceRuleRepository.findByName("上午上班时间");
        if(workingHours!=null&&workingHours.size()==1){
            workTime =days +" "+workingHours.get(0).getValue();
        }
        List<AttendanceRule> workingOutHours = attendanceRuleRepository.findByName("下午下班时间");
        if(workingOutHours!=null&&workingOutHours.size()==1){
            workOutTime =days +" "+workingOutHours.get(0).getValue() ;
        }
        pageData.put("workTime",workTime);
        pageData.put("workOutTime",workOutTime);
        pageData.put("organizationId","161");
        //1、先判断查询的日期是否为工作日
        boolean isNotWorkDay = this.isNotWorkDays(days);
        List<AttendanceDailyDate> list = null;
        if(!isNotWorkDay){
            //1.1 工作日，以员工为主表,调休申请中跨天的部分，获得需要打卡的人员信息
            list = attendanceDataMapper.attendanceDailyData(pageData);
        }else{
            //1.2 非工作日 ，以加班表为主表，获得需要打卡的人员信息
            list = attendanceDataMapper.overtimeDailyData(pageData);
        }
        if(list==null||list.size()==0){
            return;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(AttendanceDailyDate dailyDate:list){

            //是否要发送早上打卡提醒
            boolean flag=true;
            pageData.put("employeeId",dailyDate.getEmployeeId()+"");
            //请假开始时间和请假结束时间都不在上班时间范围内的请假记录
            List<LeaveApplicationOutput> list1=leaveApplicationMapper.findByEmployeeId(pageData);
            //请假开始时间或请假结束时间是今天的请假记录
            List<LeaveApplicationOutput> list2=leaveApplicationMapper.findTodayRecord(pageData);
            if(dailyDate.getSignInTime()!=null){
                continue;
            }
            if(list1!=null&&list1.size()>0){
                continue;
            }
            if(list2!=null&&list2.size()>0){
                for(LeaveApplicationOutput output:list2){
                    if(sdf1.format(output.getReportStartDate()).compareTo(workTime)<=0){
                        flag=false;
                    }
                }
            }
            if(flag){
                List<String> list3 = new ArrayList<>();
                list3.add(dailyDate.getPhoneNumber()+ "/" +dailyDate.getEmployeeName());
                String description=getContent("kqzsdktx",dailyDate.getEmployeeName());
                if(description==null||description.equals("")){
                    continue;
                }
                Map<String, Object>  map = new HashMap<>();
                map.put("description", description);
                map.put("isTiming", 0);
                map.put("type", MessageTypeEnum.APPROVAL.getCode());
                map.put("sendList",list3);
                try {
                    ResponseResult result = HttpRequestUtil.sendPostRequest("http://127.0.0.1:8775/smsSend/form",map);
                } catch (Exception e) {

                }
            }
        }
    }


    //短信模板
    public String findTemplateByKey(String type) {
        Map<String, String>  map = new HashMap<>();
//        map.put("type", type);
        ResponseResult result = HttpRequestUtil.sendGetRequest("http://127.0.0.1:8775/smstemplate/findByType?type="+type, null);
        if (result.getCode() != 200) {
            return null;
        }
        return (String) result.getData();
    }


    public String getContent(String type,String name) {
        String content = findTemplateByKey(type);
        if (content != null) {
            var map = GetMessageTemplate.getKey(content);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                switch (entry.getKey()) {
                    case "sqr"://申请人
                        entry.setValue(name);
                        break;
                }
            }
            return GetMessageTemplate.getContent(content, map);
        }
        return null;
    }


    public  void  sendMessages(Date day) throws Exception {
        PageData pageData = new PageData();
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String days = "";
        if(day ==null){
            //获得前一天的时间
            instance.setTime(new Date());
            days =sdf.format(instance.getTime());
        }else{
            instance.setTime(day);
            days =sdf.format(day);
        }
        pageData.put("days",days);
        pageData.put("date",days);
        //3.1 获得上下班时间
        String workTime = days+" 8:00:00";
        String workOutTime = days+" 16:30:00";
        List<AttendanceRule> workingHours = attendanceRuleRepository.findByName("上午上班时间");
        if(workingHours!=null&&workingHours.size()==1){
            workTime =days +" "+workingHours.get(0).getValue();
        }
        List<AttendanceRule> workingOutHours = attendanceRuleRepository.findByName("下午下班时间");
        if(workingOutHours!=null&&workingOutHours.size()==1){
            workOutTime =days +" "+workingOutHours.get(0).getValue() ;
        }
        pageData.put("workTime",workTime);
        pageData.put("workOutTime",workOutTime);
        pageData.put("organizationId","161");
        //1、先判断查询的日期是否为工作日
        boolean isNotWorkDay = this.isNotWorkDays(days);
        List<AttendanceDailyDate> list = null;
        if(!isNotWorkDay){
            //1.1 工作日，以员工为主表,调休申请中跨天的部分，获得需要打卡的人员信息
            list = attendanceDataMapper.attendanceDailyData(pageData);
        }else{
            //1.2 非工作日 ，以加班表为主表，获得需要打卡的人员信息
            list = attendanceDataMapper.overtimeDailyData(pageData);
        }
        if(list==null||list.size()==0){
            return;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(AttendanceDailyDate dailyDate:list){

            //是否要发送早上打卡提醒
            boolean flag=true;
            pageData.put("employeeId",dailyDate.getEmployeeId()+"");
            //请假开始时间和请假结束时间都不在上班时间范围内的请假记录
            List<LeaveApplicationOutput> list1=leaveApplicationMapper.findByEmployeeId(pageData);
            //请假开始时间或请假结束时间是今天的请假记录
            List<LeaveApplicationOutput> list2=leaveApplicationMapper.findTodayRecord(pageData);
            if(dailyDate.getSignOutTime()!=null){
                if(sdf1.format(dailyDate.getSignOutTime()).compareTo(workOutTime)>=0){
                    continue;
                }
            }
            if(list1!=null&&list1.size()>0){
                continue;
            }
            if(list2!=null&&list2.size()>0){
                for(LeaveApplicationOutput output:list2){
                    if(sdf1.format(output.getReportEndDate()).compareTo(workOutTime)>=0){
                        flag=false;
                    }
                }
            }
            if(flag){
                List<String> list3 = new ArrayList<>();
                list3.add(dailyDate.getPhoneNumber()+ "/" +dailyDate.getEmployeeName());
                String description=getContent("kqxwdktx",dailyDate.getEmployeeName());
                if(description==null||description.equals("")){
                    continue;
                }
                Map<String, Object>  map = new HashMap<>();
                map.put("description", description);
                map.put("isTiming", 0);
                map.put("type", MessageTypeEnum.APPROVAL.getCode());
                map.put("sendList",list3);
                try {
                    ResponseResult result = HttpRequestUtil.sendPostRequest("http://127.0.0.1:8775/smsSend/form",map);
                } catch (Exception e) {

                }
            }
        }
    }







}
