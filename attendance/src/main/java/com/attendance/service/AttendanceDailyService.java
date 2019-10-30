package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.*;
import com.attendance.mapper.jpa.AttendanceRuleNewRepository;
import com.attendance.mapper.jpa.AttendanceRuleRepository;
import com.attendance.mapper.jpa.AttendanceStatisticsRepository;
import com.attendance.mapper.mybatis.*;
import com.attendance.model.AttendanceRule;
import com.attendance.model.AttendanceStatistics;
import com.common.model.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class AttendanceDailyService extends BaseService<AttendanceStatistics,AttendanceStatistics,Integer>{
    @Autowired
    private AttendanceDataMapper attendanceDataMapper;

    @Autowired
    private AttendanceStatisticsRepository attendanceStatisticsRepository;

    @Autowired
    private AttendanceStatisticsMapper attendanceStatisticsMapper;

    @Autowired
    private HolidayMapper holidayMapper;
    @Autowired
    private AttendanceRuleNewMapper attendanceRuleNewMapper;
    @Autowired
    private AttendanceRuleNewRepository attendanceRuleNewRepository;
    @Autowired
    private AttendanceStatisticsNewMapper attendanceStatisticsNewMapper;
    @Autowired
    private AttendanceRuleRepository attendanceRuleRepository;

    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;

    @Autowired
    private OvertimeApplicationMapper overtimeApplicationMapper;

    @Autowired
    private OffApplicationMapper offApplicationMapper;
    @Autowired
    private OrganizationMapper organizationMapper;


    @Override
    public BaseMapper<AttendanceStatistics, Integer> getMapper() {
        return attendanceStatisticsRepository;
    }

    @Override
    public MybatisBaseMapper<AttendanceStatistics> getMybatisBaseMapper() {
        return attendanceStatisticsMapper;
    }


    @Transactional
    public void createAttendanceDailyDate(Date day) throws Exception {
        PageData pageData = new PageData();
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String days = "";
        if(day ==null){
            //获得前一天的时间
            instance.add(Calendar.DATE,-1);
            days =sdf.format(instance.getTime());
        }else{
            instance.setTime(day);
            days =sdf.format(day);
        }
        pageData.put("days",days);
        //判断是否已经生成前天的考勤日报表数据，若已经生成则跳出
        List<AttendanceDailyDate> attendanceDailyDate1 = attendanceStatisticsMapper.findAttendanceDailyDate(pageData);
        if(day==null){
            if(attendanceDailyDate1!=null&&attendanceDailyDate1.size()>0){
                return;
            }
        }else{
            attendanceStatisticsMapper.deleteByDate(pageData);
        }
        //3.1 获得上午上班时间
        List<AttendanceRule> atruleMonringList = attendanceRuleRepository.findByTypeAndState(1,1);
        //获取下午下班时间
        List<AttendanceRule> atruleAfternoonList  = attendanceRuleRepository.findByTypeAndState(4,1);

        //获取上班的xxx日XXX分XX秒+atruleMonringList.get(0).getValue(),不应该是加上固定的days+8:00:00  杨正武留
        String workTime = days+" "+atruleMonringList.get(0).getValue();
        //获取下班的xxx日XXX分XX秒 下班时间也是如此，不该写成固定的days+16:30:00  杨正武留
        String workOutTime = days+" "+atruleAfternoonList.get(0).getValue();
        pageData.put("workTime",workTime);
        pageData.put("workOutTime",workOutTime);

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
        }else {
            list=updateAttendanceDaily(list,workTime,workOutTime);
        }
        //判断是否请假，生成最终报表
        List<AttendanceStatistics> list1=updateAttendanceDailyList(list,workTime,workOutTime,instance.getTime());
        attendanceStatisticsRepository.saveAll(list1);
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

    //更新请假/迟到/早退/未打卡状态
    public List<AttendanceDailyDate> updateAttendanceDaily(List<AttendanceDailyDate> list,String workTime,String workOutTime) throws ParseException {
        PageData pageData=new PageData();
        pageData.put("workTime",workTime);
        pageData.put("workOutTime",workOutTime);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1=df.parse(workTime);
        Date date2=df.parse(workOutTime);
        List<AttendanceDailyDate> dailyDateList=new ArrayList<AttendanceDailyDate>();
        for(AttendanceDailyDate dailyDate:list){
            dailyDate=updateState(date1,date2,dailyDate);
            dailyDateList.add(dailyDate);
        }
        return dailyDateList;
    }



    //更新迟到/早退/未打卡状态
    public AttendanceDailyDate updateState(Date workTime,Date workOutTime,AttendanceDailyDate dailyDate){
        dailyDate.setStatusName("正常");
        dailyDate.setIsLeave("否");
        if(dailyDate.getSignInTime()==null&&dailyDate.getSignOutTime()==null){
            dailyDate.setLeaveEarly("否");
            dailyDate.setBeLate("否");
            dailyDate.setPunch("否");
            dailyDate.setIsLeave("否");
            dailyDate.setStatusName("异常");
        }else if(dailyDate.getSignInTime()!=null&&dailyDate.getSignOutTime()!=null){
            dailyDate.setLeaveEarly("否");
            dailyDate.setBeLate("否");
            dailyDate.setPunch("是");
            if(dailyDate.getSignInTime().after(workTime)){
                dailyDate.setBeLate("是");
                dailyDate.setStatusName("异常");
            }
            if(dailyDate.getSignOutTime().before(workOutTime)){
                dailyDate.setLeaveEarly("是");
                dailyDate.setStatusName("异常");
            }
        }else if(dailyDate.getSignInTime()!=null&&dailyDate.getSignOutTime()==null) {
            dailyDate.setLeaveEarly("是");
            dailyDate.setBeLate("否");
            if(dailyDate.getSignInTime().after(workTime)){
                dailyDate.setBeLate("是");
            }
            dailyDate.setPunch("是");
            dailyDate.setStatusName("异常");
        }else if(dailyDate.getSignInTime()!=null&&dailyDate.getSignOutTime()!=null){
            dailyDate.setLeaveEarly("否");
            dailyDate.setBeLate("是");
            if(dailyDate.getSignOutTime().before(workOutTime)){
                dailyDate.setLeaveEarly("是");

            }
            dailyDate.setPunch("是");
            dailyDate.setStatusName("异常");
        }
        return dailyDate;
    }



    //更新请假/迟到/早退/未打卡状态
    public List<AttendanceStatistics> updateAttendanceDailyList(List<AttendanceDailyDate> list, String workTime, String workOutTime,Date date) throws ParseException {
        PageData pageData=new PageData();

        //设置开始时间
        pageData.put("workTime",workTime);
        //设置结束日期
        pageData.put("workOutTime",workOutTime);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat famatter = new SimpleDateFormat("yyyy-MM-dd");
        String d=famatter.format(date);
        pageData.put("date",d);
        Date date1=df.parse(workTime);
        Date date2=df.parse(workOutTime);
        String workTime1=null;
        String workOutTime1=null;
        List<AttendanceRule> workingHours = attendanceRuleRepository.findByName("上午下班时间");
        if(workingHours!=null&&workingHours.size()==1){
            workTime1 =d+" "+workingHours.get(0).getValue();
        }
        List<AttendanceRule> workingOutHours = attendanceRuleRepository.findByName("下午上班时间");
        if(workingOutHours!=null&&workingOutHours.size()==1){
            workOutTime1 =d+" "+workingOutHours.get(0).getValue() ;
        }
        //上午下班时间
        Date date5=df.parse(workTime1);
        //下午上班时间
        Date date6=df.parse(workOutTime1);
        List<AttendanceStatistics> dailyDateList=new ArrayList<>();
        for(AttendanceDailyDate dailyDate:list){
            AttendanceStatistics statistics=new AttendanceStatistics();
            statistics.setEmployeeId(dailyDate.getEmployeeId());
            statistics.setEmployeeName(dailyDate.getEmployeeName());
            statistics.setEmployeeNo(dailyDate.getEmployeeNo());
            statistics.setJobsName(dailyDate.getJobsName());
            statistics.setOrganizationName(dailyDate.getOrganizationName());
            statistics.setOrganizationId(dailyDate.getOrganizationId());
            statistics.setStatusName(dailyDate.getStatusName());
            statistics.setIsLeave(dailyDate.getIsLeave());
            statistics.setAbsenceHours(BigDecimal.ZERO);
            statistics.setBeLate(dailyDate.getBeLate());
            statistics.setLeaveEarly(dailyDate.getLeaveEarly());
            statistics.setPunch(dailyDate.getPunch());
            statistics.setSignInTime(dailyDate.getSignInTime());
            statistics.setSignOutTime(dailyDate.getSignOutTime());
            if(dailyDate.getSignInTime()!=null&&dailyDate.getSignOutTime()!=null){
                if(dailyDate.getSignInTime().compareTo(date6)>=0){
                    statistics.setSignInTime(null);
                }
                if(dailyDate.getSignOutTime().compareTo(date5)<=0){
                    statistics.setSignOutTime(null);
                }
            }
            statistics.setAttendanceDate(date);
            pageData.put("employeeId",dailyDate.getEmployeeId()+"");
            //请假开始时间和请假结束时间都不在上班时间范围内的请假记录
            List<LeaveApplicationOutput> list1=leaveApplicationMapper.findByEmployeeId(pageData);
            //请假开始时间或请假结束时间是今天的请假记录
            List<LeaveApplicationOutput> list2=leaveApplicationMapper.findTodayRecord(pageData);
            if(list2!=null&&list2.size()>0){
                boolean a=true;
                for(LeaveApplicationOutput output:list2){
                    if(list1!=null&&list1.size()>0){
                        if(list1.get(0).getId().equals(output.getId())){
                            a=false;
                        }
                    }
                }
                if(list1!=null&&list1.size()>0){
                    if(a){
                        list2.addAll(list1);
                    }
                }
            }else {
                if (list1!=null&&list1.size()>0){
                    list2=list1;
                }
            }
            int hours=0;
            if(list2!=null&&list2.size()>0){
                statistics.setIsLeave("是");
                boolean e=false;
                boolean f=false;
                for(LeaveApplicationOutput output:list2){
                    String a=d+" "+output.getStartTime();
                    String b=d+" "+output.getEndTime();
                    //请假开始时间
                    Date date3=df.parse(a);
                    //请假结束时间
                    Date date4=df.parse(b);
                    //早上半天请假
                    if(date3.compareTo(date1)<=0&&date4.compareTo(date5)>=0){
                        e=true;
                    }
                    //下午半天请假
                    if(date3.compareTo(date6)<=0&&date4.compareTo(date2)>=0){
                        f=true;
                    }
                   if(date1.compareTo(output.getReportStartDate())>=0&&date2.compareTo(output.getReportEndDate())<=0&&output.getApplicationType()!=2){
                       statistics.setLeaveEarly("否");
                       statistics.setBeLate("否");
                       statistics.setStatusName("正常");
                       if(output.getApplicationType()==2||output.getApplicationType()==5|output.getApplicationType()==6||output.getApplicationType()==1||(output.getApplicationType()>9&&output.getApplicationType()!=10)){
                           hours=0;
                       }else {
                           hours=8;
                       }
                       continue;
                       //如果是哺乳假
                   }else if(output.getApplicationType()== 2){
                       if(statistics.getSignInTime()!=null){
                           //请假开始时间小于等于上班时间，请假结束时间小于于等于下班时间
                           if(date1.compareTo(date3)>=0){
                               //早上半天请假，在下午上班时间之前打卡不算迟到
                               if(e){
                                   if(statistics.getSignInTime().compareTo(date6)<=0){
                                       statistics.setBeLate("否");
                                   }
                               }
                               //在请假结束时间之前打卡不算早退
                               if(statistics.getSignInTime().compareTo(date4)<=0){
                                   statistics.setBeLate("否");
                               }
                           }
                       }
                       if(statistics.getSignOutTime()!=null){
                           //请假开始时间大于等于上班时间，请假结束时间大于等于下班时间
                           if(date2.compareTo(date4)<=0){
                               //下午半天请假，在上午下班时间之后打卡不算早退
                               if(f){
                                   if(statistics.getSignOutTime().compareTo(date5)>=0){
                                       statistics.setLeaveEarly("否");
                                   }
                               }
                               //在请假开始时间之后打卡不算早退
                               if(statistics.getSignOutTime().compareTo(date3)>=0){
                                   statistics.setLeaveEarly("否");
                               }
                           }
                       }
                   }else {
                       if(statistics.getSignInTime()!=null){
                           //请假开始时间小于等于上班时间，请假结束时间小于下班时间
                           if(date1.compareTo(date3)>=0){
                               //早上半天请假，在下午上班时间之前打卡不算迟到
                              if(e){
                                  if(statistics.getSignInTime().compareTo(date6)<=0){
                                      statistics.setBeLate("否");
                                  }
                              }
                               //在请假结束时间之前打卡不算迟到
                               if(statistics.getSignInTime().compareTo(date4)<=0){
                                   statistics.setBeLate("否");
                               }
                           }
                       }
                       if(statistics.getSignOutTime()!=null){
                           //请假开始时间大于上班时间，请假结束时间大于等于下班时间
                           if(date2.compareTo(date4)<=0){
                               //下午半天请假，在上午下班时间之后打卡不算早退
                               if(f){
                                   if(statistics.getSignOutTime().compareTo(date5)>=0){
                                       statistics.setLeaveEarly("否");
                                   }
                               }
                               //在请假开始时间之后打卡不算早退
                               if(statistics.getSignOutTime().compareTo(date3)>=0){
                                   statistics.setLeaveEarly("否");
                               }
                           }
                       }
                       if(output.getApplicationType()==5||output.getApplicationType()==6||output.getApplicationType()==1
                               ||(output.getApplicationType()>9&&output.getApplicationType()!=10)){
                           hours+=0;
                       }else if(output.getApplicationType()==10){
                           hours+=2;
                       }else{
                           hours+=4;
                       }
                   }
                }
                if(e&&f){
                    statistics.setBeLate("否");
                    statistics.setLeaveEarly("否");
                    statistics.setStatusName("正常");
                }
                if(statistics.getBeLate().equals("否")&&statistics.getLeaveEarly().equals("否")){
                    statistics.setStatusName("正常");
                }
                statistics.setAbsenceHours(new BigDecimal(hours));
            }
            dailyDateList.add(statistics);
        }
        return dailyDateList;
    }

    private Date getDate(int hour){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        Date date = calendar.getTime();
        return date;
    }

    public  boolean updateDaily(Integer id,Integer type,Integer orgId) throws IntrospectionException, MethodArgumentNotValidException, IllegalAccessException, ParseException, InvocationTargetException {
        Integer ruleId = organizationMapper.selectRuleIdByOrgId(orgId);
        //获取上午上班时间
        String workTime =
                    attendanceRuleNewRepository.findAllByAttendanceRuleConfigIdAndAmputatedAndTypeAndState(ruleId,0,1,1).getValue();
        if(workTime==null||workTime==""){
            workTime = "08:30:00";
        }

        //获取下午下班时间
        String workOutTime=
                attendanceRuleNewRepository.findAllByAttendanceRuleConfigIdAndAmputatedAndTypeAndState(ruleId,0,4,1).getValue();
        if(workOutTime==null||workOutTime==""){
            workOutTime = "16:30:00";
        }

        //type 1--考勤数据补录  2--请假申请通过  3--加班声请通过  4--调休申请通过
        if(type==1){
            if(!updateByAttendanceData(id,workTime,workOutTime)){
                return false;
            }
        }else if(type==2){
            if(!updateByLeave(id,workTime,workOutTime)){
                return false;
            }
        }else if(type==3){
            if(!updateByOverTime(id,workTime,workOutTime)){
                return false;
            }
        }else if(type==4){
            if(!updateByOff(id,workTime,workOutTime)){
                return false;
            }
        }
        return true;
    }

    //考勤数据补录更新考勤日报表
    public boolean updateByAttendanceData(Integer id,String workTime,String workOutTime) throws ParseException, InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        AttendanceDataOutput attendanceData=attendanceDataMapper.selectByPrimaryKey(id);
        if(attendanceData!=null){
            String date=format.format(attendanceData.getPunchTime());
            PageData pageData=new PageData();
            pageData.put("date",date);
            pageData.put("employeeId",attendanceData.getEmployeeId().toString());
            List<AttendanceStatistics> list=attendanceStatisticsNewMapper.selectByEmployeeId(pageData);
            AttendanceStatistics attendanceStatistics;
            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1=format1.parse(date+" "+workTime);
            Date date2=format1.parse(date+" "+workOutTime);
//            String workTime1=null;
//            String workOutTime1=null;
//            List<AttendanceRule> workingHours = attendanceRuleRepository.findByName("上午下班时间");
//            if(workingHours!=null&&workingHours.size()==1){
//                workTime1 =date+" "+workingHours.get(0).getValue();
//
//            List<AttendanceRule> workingOutHours = attendanceRuleRepository.findByName("下午上班时间");
//            if(workingOutHours!=null&&workingOutHours.size()==1){
//                workOutTime1 =date+" "+workingOutHours.get(0).getValue() ;
//            }
            //上午下班时间
            Date date3=format1.parse(date+" "+workTime);
            //下午上班时间
            Date date4=format1.parse(date+" "+workOutTime);
            if(list!=null&&list.size()>0){
                attendanceStatistics=list.get(0);
                if(attendanceStatistics!=null){
                    if(attendanceStatistics.getSignInTime()==null||attendanceStatistics.getSignInTime().after(attendanceData.getPunchTime())){
                        if(date3.after(attendanceData.getPunchTime())){
                            attendanceStatistics.setSignInTime(attendanceData.getPunchTime());
                        }
                    }else if(attendanceStatistics.getSignOutTime()==null||attendanceStatistics.getSignOutTime().before(attendanceData.getPunchTime())){
                        if(date4.before(attendanceData.getPunchTime())){
                            attendanceStatistics.setSignInTime(attendanceData.getPunchTime());
                        }
                    }
                    attendanceStatistics.setPunch("否");
                    if(attendanceStatistics.getSignInTime()!=null&&attendanceStatistics.getSignInTime().before(date1)){
                        attendanceStatistics.setBeLate("否");
                    }
                    if(attendanceStatistics.getSignOutTime()!=null&&attendanceStatistics.getSignOutTime().after(date2)){
                        attendanceStatistics.setLeaveEarly("否");
                    }
                    if(attendanceStatistics.getBeLate().equals("否")&&attendanceStatistics.getLeaveEarly().equals("否")){
                        attendanceStatistics.setStatusName("正常");
                    }

                    if(update(attendanceStatistics.getId(),attendanceStatistics)<0){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //请假更新考勤日报表
    public boolean updateByLeave(Integer id,String workTime,String workOutTime) throws ParseException, InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        LeaveApplicationOutput output=leaveApplicationMapper.selectByPrimaryKey(id);
        String startTime=format.format(output.getStartDate());
        String endTime=format.format(output.getEndDate());
        PageData pageData=new PageData();
        pageData.put("startTime",startTime);
        pageData.put("endTime",endTime);
        pageData.put("employeeId",""+output.getEmployeesId());
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        int hours=0;
        List<AttendanceStatistics> list=attendanceStatisticsNewMapper.selectByEmployeeId(pageData);
        if(output!=null&&list!=null&&list.size()>0){
            for(AttendanceStatistics statistics:list){
                String date=format.format(statistics.getAttendanceDate());
                Date date1=format1.parse(date+" "+workTime);
                Date date2=format1.parse(date+" "+workOutTime);
                String workTime1=null;
                String workOutTime1=null;
                pageData.put("type",3);
                String workingHours = attendanceRuleNewMapper.findByTypeAndEmployeeId(pageData);
                workTime1 =date+" "+workingHours;
                pageData.put("type",2);
                String workingOutHours = attendanceRuleNewMapper.findByTypeAndEmployeeId(pageData);
                workOutTime1 =date+" "+workingOutHours ;
                Date date5=format.parse(workTime1);
                Date date6=format.parse(workOutTime1);
                statistics.setIsLeave("是");
                if(date1.compareTo(output.getReportStartDate())>=0&&date2.compareTo(output.getReportEndDate())<=0){
                    statistics.setLeaveEarly("否");
                    statistics.setBeLate("否");
                    statistics.setPunch("否");
                    statistics.setStatusName("正常");
                    if(output.getApplicationType()==2||output.getApplicationType()==5|output.getApplicationType()==6||output.getApplicationType()==1||(output.getApplicationType()>9&&output.getApplicationType()!=10)){
                        hours=0;
                    }else {
                        hours=8;
                    }
                }else if(output.getApplicationType()==2){
                    String a=date+" "+output.getStartTime();
                    String b=date+" "+output.getEndTime();
                    Date date3=format1.parse(a);
                    Date date4=format1.parse(b);
                    if(statistics.getSignInTime()!=null){
                        //请假开始时间小于等于上班时间，请假结束时间小于于等于下班时间
                        if(date1.compareTo(date3)>=0){
                            //早上半天请假，在下午上班时间之前打卡不算迟到
                            if(date4.compareTo(date5)>=0){
                                if(statistics.getSignInTime().compareTo(date6)<=0){
                                    statistics.setBeLate("否");
                                }
                            }
                            //在请假结束时间之前打卡不算早退
                            if(statistics.getSignInTime().compareTo(date4)<=0){
                                statistics.setBeLate("否");
                            }
                        }
                    }
                    if(statistics.getSignOutTime()!=null){
                        //请假开始时间大于上班时间，请假结束时间大于等于下班时间
                        if(date2.compareTo(date4)<=0){
                            //下午半天请假，在上午下班时间之后打卡不算早退
                            if(date3.compareTo(date6)<=0){
                                if(statistics.getSignOutTime().compareTo(date5)>=0){
                                    statistics.setLeaveEarly("否");
                                }
                            }
                            //在请假开始时间之后打卡不算早退
                            if(statistics.getSignOutTime().compareTo(date3)>=0){
                                statistics.setLeaveEarly("否");
                            }
                        }
                    }
                    if(statistics.getSignInTime()!=null||statistics.getSignOutTime()!=null){
                        if(statistics.getBeLate().equals("否")&&statistics.getLeaveEarly().equals("否")){
                            statistics.setStatusName("正常");
                        }
                    }
                }else {
                    if(statistics.getSignInTime()!=null){
                        //请假开始时间小于等于上班时间，请假结束时间小于下班时间
                        if(date1.compareTo(output.getReportStartDate())>=0){
                            //在请假结束时间之前打卡不算早退
                            if(statistics.getSignInTime().compareTo(output.getEndDate())<=0){
                                statistics.setBeLate("否");
                            }
                        }
                    }
                    if(statistics.getSignOutTime()!=null){
                        //请假开始时间大于上班时间，请假结束时间大于等于下班时间
                        if(date2.compareTo(output.getReportEndDate())<=0){
                            //在请假开始时间之后打卡不算早退
                            if(statistics.getSignOutTime().compareTo(output.getStartDate())>=0){
                                statistics.setLeaveEarly("否");
                            }
                        }
                    }
                    if(output.getApplicationType()==2||output.getApplicationType()==5|output.getApplicationType()==6||output.getApplicationType()==1||(output.getApplicationType()>9&&output.getApplicationType()!=10)){
                        hours+=0;
                    }else if(output.getApplicationType()==10){
                        hours+=2;
                    }else{
                        hours+=4;
                    }
                    if(statistics.getSignInTime()!=null||statistics.getSignOutTime()!=null){
                        if(statistics.getBeLate().equals("否")&&statistics.getLeaveEarly().equals("否")){
                            statistics.setStatusName("正常");
                        }
                    }
                }
                statistics.setAbsenceHours(statistics.getAbsenceHours().add(new BigDecimal(hours)));
                if(update(statistics.getId(),statistics)<0){
                    return false;
                }
            }

        }
        return true;
    }

    //加班申请通过更新考勤日报表
    public boolean updateByOverTime(Integer id,String workTime,String workOutTime) throws ParseException, InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        OvertimeApplicationOutput output=overtimeApplicationMapper.selectByPrimaryKey(id);
        if(output!=null){
            String date=format.format(output.getOverTimeDate());
            PageData pageData=new PageData();
            pageData.put("date",date);
            pageData.put("employeeId",""+output.getEmployeesId());
            pageData.put("days",date);
            List<AttendanceStatistics> list=attendanceStatisticsNewMapper.selectByEmployeeId(pageData);
            AttendanceDailyDate attendanceDailyDate=attendanceDataMapper.selectByEmployeeId(pageData);
            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1=format1.parse(date+" "+workTime);
            Date date2=format1.parse(date+" "+workOutTime);
            if(list==null||list.size()<=0){
              if(output.getOverTimeDate().before(new Date())){
                  AttendanceStatistics statistics=new AttendanceStatistics();
                  statistics.setEmployeeId(output.getEmployeesId());
                  statistics.setEmployeeName(output.getEmployeesName());
                  statistics.setEmployeeNo(output.getEmployeeNo());
                  statistics.setOrganizationName(output.getOrganizationName());
                  statistics.setOrganizationId(output.getOrganizationId());
                  statistics.setAbsenceHours(BigDecimal.ZERO);
                  statistics.setAttendanceDate(output.getOverTimeDate());
                  statistics.setStatusName("异常");
                  statistics.setBeLate("否");
                  statistics.setLeaveEarly("否");
                  statistics.setPunch("否");
                  statistics.setIsLeave("否");
                  if(attendanceDailyDate!=null){
                      statistics.setPunch("是");
                      statistics.setSignInTime(attendanceDailyDate.getSignInTime());
                      statistics.setSignOutTime(attendanceDailyDate.getSignOutTime());
                      if(statistics.getSignInTime().before(date1)){
                          statistics.setBeLate("否");
                      }
                      if(statistics.getSignOutTime().after(date2)){
                          statistics.setLeaveEarly("否");
                      }
                      if(statistics.getBeLate().equals("否")&&statistics.getLeaveEarly().equals("否")){
                          statistics.setStatusName("正常");
                      }
                  }
                  if(add(statistics)<0){
                      return false;
                  }
              }
            }
        }
        return true;
    }

    //调休申请通过更新考勤日报表
    public boolean updateByOff(Integer id,String workTime,String workOutTime){
        OffApplicationOutput output=offApplicationMapper.selectByPrimaryKey(id);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String date=format.format(output.getRestTime());
        if(output!=null){
            PageData pageData=new PageData();
            pageData.put("date",date);
            pageData.put("employeeId",""+output.getEmployeesId());
            AttendanceStatistics attendanceStatistics;
            List<AttendanceStatistics> list=attendanceStatisticsNewMapper.selectByEmployeeId(pageData);
            if(list!=null&&list.size()>0){
                attendanceStatistics=list.get(0);
                if(deleteById(attendanceStatistics.getId()+"")<0){
                    return false;
                }
            }
        }
        return true;
    }



}
