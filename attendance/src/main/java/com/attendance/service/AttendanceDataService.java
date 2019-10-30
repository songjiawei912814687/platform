package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.*;
import com.attendance.mapper.jpa.AttendanceDataRepository;
import com.attendance.mapper.jpa.AttendanceRuleRepository;
import com.attendance.mapper.jpa.AttendanceStatisticsRepository;
import com.attendance.mapper.mybatis.*;
import com.attendance.model.AttendanceData;
import com.attendance.model.AttendanceRule;
import com.attendance.model.AttendanceStatistics;
import com.attendance.model.Users;
import com.common.Enum.ApprovalTypeEnum;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.BigDecimalUtil;
import com.common.utils.DateUtils;
import com.common.utils.ExportExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.attendance.core.base.BaseController.SYS_EORRO;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
public class AttendanceDataService extends BaseService<AttendanceDataOutput, AttendanceData, Integer> {

    @Autowired
    private AttendanceDataRepository attendanceDataRepository;
    @Autowired
    private AttendanceDataMapper attendanceDataMapper;
    @Autowired
    private AttendanceStatisticsRepository attendanceStatisticsRepository;
    @Autowired
    private AttendanceStatisticsMapper attendanceStatisticsMapper;
    @Autowired
    private AttendanceStatisticsNewService attendanceStatisticsNewService;
    @Autowired
    private HolidayMapper holidayMapper;
    @Autowired
    private AttendanceRuleRepository attendanceRuleRepository;
    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;
    @Autowired
    private OvertimeApplicationMapper overtimeApplicationMapper;
    @Autowired
    private OffApplicationMapper offApplicationMapper;
    @Autowired
    private AttendanceStatisticsNewMapper attendanceStatisticsNewMapper;

    @Override
    public BaseMapper<AttendanceData, Integer> getMapper() {
        return attendanceDataRepository;
    }

    @Override
    public MybatisBaseMapper<AttendanceDataOutput> getMybatisBaseMapper() {
        return attendanceDataMapper;
    }

    public ResponseResult selectAttendancePage(PageData pageData){
        Integer pageSize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pageSize);
        PageInfo pageInfo = new PageInfo(attendanceDataMapper.selectAttendancePage(pageData));
        return ResponseResult.success(pageInfo) ;
    }

    public String export(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "考勤日报表";
        String excelName = "考勤日报表";
        String[] rowsName = new String[]{"序号","姓名","工号","组织机构","职务","上班签到时间","下班签退时间","迟到","早退","未打卡","是否请假","状态"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        Integer a=0;
        if(pageData.getMap().get("stateType")!=null&&!pageData.getMap().get("stateType").equals("")){
            a=Integer.parseInt(pageData.getMap().get("stateType"));
        }
        if(a!=0){
            switch (a){
                case 1:
                    pageData.put("stateName","正常");
                    break;
                case 2:
                    pageData.put("stateName","异常");
                    break;
            }
        }
        pageData.put("userId",getUsers().getId().toString());
        if(getUsers().getAdministratorLevel()!=9){
            if(getUsers().getUserType()==0){
                pageData.put("employeeId",getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",getUsers().getOrganizationId().toString());
            }
        }
        List<AttendanceDailyDate> jobs=findAttendanceDailyDate(pageData);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(jobs!=null&&jobs.size()>0){
            int i=1;
            for(AttendanceDailyDate jobsOutput:jobs){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=jobsOutput.getEmployeeName();
                objs[2]=jobsOutput.getEmployeeNo();
                objs[3]=jobsOutput.getOrganizationName();
                objs[4]=jobsOutput.getJobsName();
                if(jobsOutput.getSignInTime()!=null){
                    objs[5]=simpleDateFormat.format(jobsOutput.getSignInTime());
                }else {
                    objs[5]=null;
                }
                if(jobsOutput.getSignOutTime()!=null){
                    objs[6]=simpleDateFormat.format(jobsOutput.getSignOutTime());
                }else {
                    objs[6]=null;
                }
                objs[7]=jobsOutput.getBeLate();
                objs[8]=jobsOutput.getLeaveEarly();
                objs[9]=jobsOutput.getPunch();
                objs[10]=jobsOutput.getIsLeave();
                objs[11]=jobsOutput.getStatusName();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

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
        //1、先判断查询的日期是否为工作日
        boolean isNotWorkDay = this.isNotWorkDays(days);
        List<AttendanceDailyDate> list = null;
        if(!isNotWorkDay){
            //1.1 工作日，以员工为主表，结合请假申请、请假补录、调休申请中跨天的部分，获得需要打卡的人员信息
            list = attendanceDataMapper.attendanceObjData(pageData);
        }else{
            //1.2 非工作日 ，以加班表为主表，获得需要打卡的人员信息
            list = attendanceDataMapper.overtimeObjData(pageData);
        }
        //2、set签到时间和签退时间，签到时间最早时间 签退时间为最晚时间
        if(list==null||list.size()==0){
            return;
        }
        List<AttendanceDailyDate> setValList = this.setData(list);
        //3、根据考勤规则结合请假申请和请假申请和请假补录获得员工的最迟上班打卡时间和最早下班打卡时间
        //获得最迟上班打卡时间和最早下班打卡时间
          //获得上班最迟打卡时间
            //3.2.1 若未跨天 start<=上班时间<=end<下班时间 最迟打卡时间为end中最大的时间
            //3.2.2 若跨天   结束时间的时间部分和上班时间比较若>=上班时间取最大的时间
            //3.2.3 取上面两者的最大时间
        List<AttendancePunch> latestWorkingList = leaveApplicationMapper.latestWorkingTime(pageData);
          //获得最早打卡时间
             //3.2.1 若未跨天 上班时间<start<=下班时间<=end 最早上班时间为start中最小的时间
             //3.2.2 若跨天   开始时间的时间部分和小班时间比较若>=上班时间取最大的时间
             //3.2.3 取上面两者的最大时间
        List<AttendancePunch> firstOffWorkList = leaveApplicationMapper.firstOffWorkTime(pageData);
        //上班最迟打卡时间，最迟打卡时间 set setValList值中的迟到、早退，未打卡、状态
        setValList = this.setStateVal(setValList,latestWorkingList,firstOffWorkList,workTime,workOutTime);
        //4、统计员工请假的累计小时数
        //4.1 请假类型涉及到产假、事假、病假、其他类型的请假申请或者请假补录
        List<LeaveApplicationOutput> absenceList = leaveApplicationMapper.absence(pageData);
        //4.2 请假类型涉及到因私临时外出两小时的请假申请或者请假补录
        List<LeaveApplicationOutput> absenceList1 = leaveApplicationMapper.temporaryAbsence(pageData);
        Map<Integer, BigDecimal> maps = this.getAbsenceHours(absenceList,absenceList1,workTime,workOutTime,days);
        //存储考勤日报表数据到考勤日报表的静态表中
        List<AttendanceStatistics> addList = new ArrayList<>();
        for (AttendanceDailyDate attendanceDailyDate:setValList) {
            AttendanceStatistics attendanceStatistics = new AttendanceStatistics();
            attendanceStatistics.setIsLeave(attendanceDailyDate.getIsLeave());
            attendanceStatistics.setAttendanceDate(instance.getTime());
            attendanceStatistics.setEmployeeId(attendanceDailyDate.getEmployeeId());
            attendanceStatistics.setEmployeeName(attendanceDailyDate.getEmployeeName());
            attendanceStatistics.setEmployeeNo(attendanceDailyDate.getEmployeeNo());
            attendanceStatistics.setOrganizationId(attendanceDailyDate.getOrganizationId());
            attendanceStatistics.setBeLate(attendanceDailyDate.getBeLate());
            attendanceStatistics.setJobsName(attendanceDailyDate.getJobsName());
            attendanceStatistics.setLeaveEarly(attendanceDailyDate.getLeaveEarly());
            attendanceStatistics.setOrganizationName(attendanceDailyDate.getOrganizationName());
            attendanceStatistics.setPunch(attendanceDailyDate.getPunch());
            attendanceStatistics.setSignInTime(attendanceDailyDate.getSignInTime());
            attendanceStatistics.setSignOutTime(attendanceDailyDate.getSignOutTime());
            attendanceStatistics.setStatusName(attendanceDailyDate.getStatusName());
            attendanceStatistics.setAbsenceHours(maps.containsKey(attendanceDailyDate.getEmployeeId())?maps.get(attendanceDailyDate.getEmployeeId()):null);
            addList.add(attendanceStatistics);
        }
        attendanceStatisticsRepository.saveAll(addList);
    }

    private Map<Integer, BigDecimal> getAbsenceHours(List<LeaveApplicationOutput> absenceList,List<LeaveApplicationOutput> absenceList1,String workTime,String workOutTime,String days) throws Exception{
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        Date work = simpleDateFormat.parse(workTime);
        Date workOut = simpleDateFormat.parse(workOutTime);
        HashMap<Integer, String> stringStringHashMap = new HashMap<>();
        for(LeaveApplicationOutput leaveApplicationOutput:absenceList){
            if(work.after(leaveApplicationOutput.getStartDate())&&leaveApplicationOutput.getEndDate().after(workOut)){
                stringStringHashMap.put(leaveApplicationOutput.getEmployeesId(),"全");
            }
        }
        //获得上午下班时间和下午上班时间
        AttendanceRule attendanceRule =  attendanceRuleRepository.getByDescription("上午下班时间");
        AttendanceRule attendanceRule2 =  attendanceRuleRepository.getByDescription("下午上班时间");
        Date upEndDate = simpleDateFormat.parse(days+" "+attendanceRule.getValue());
        Date downStartDate = simpleDateFormat.parse(days+" "+attendanceRule2.getValue());
        for(LeaveApplicationOutput leaveApplicationOutput:absenceList){
            if(work.after(leaveApplicationOutput.getStartDate())&&leaveApplicationOutput.getEndDate().after(workOut)){
                stringStringHashMap.put(leaveApplicationOutput.getEmployeesId(),"全");
            }
        }
        for (LeaveApplicationOutput leaveApplicationOutput:absenceList) {
            if(stringStringHashMap.containsKey(leaveApplicationOutput.getEmployeesId())){
                break;
            }else{
                if(work.after(leaveApplicationOutput.getStartDate())&&leaveApplicationOutput.getEndDate().after(upEndDate)&&downStartDate.after(leaveApplicationOutput.getEndDate())){
                    stringStringHashMap.put(leaveApplicationOutput.getEmployeesId(),"上");
                }
            }
        }
        for (LeaveApplicationOutput leaveApplicationOutput:absenceList) {
            if(stringStringHashMap.containsKey(leaveApplicationOutput.getEmployeesId())&&stringStringHashMap.get(leaveApplicationOutput.getEmployeesId()).equals("全")){
                break;
            }else{
                if(work.after(leaveApplicationOutput.getStartDate())&&leaveApplicationOutput.getEndDate().after(upEndDate)&&downStartDate.after(leaveApplicationOutput.getEndDate())){
                    if(stringStringHashMap.containsKey(leaveApplicationOutput.getEmployeesId())&&stringStringHashMap.get(leaveApplicationOutput.getEmployeesId()).equals("上")){
                        stringStringHashMap.put(leaveApplicationOutput.getEmployeesId(),"全");
                    }else{
                        stringStringHashMap.put(leaveApplicationOutput.getEmployeesId(),"下");
                    }
                }
            }
        }
        for (LeaveApplicationOutput leaveApplicationOutput:absenceList1) {
            if(downStartDate.after(leaveApplicationOutput.getEndDate())){
                if(stringStringHashMap.containsKey(leaveApplicationOutput.getEmployeesId())){
                    if(stringStringHashMap.get(leaveApplicationOutput.getEmployeesId()).equals("全")){
                        break;
                    }else if(stringStringHashMap.get(leaveApplicationOutput.getEmployeesId()).equals("下")){
                        stringStringHashMap.put(leaveApplicationOutput.getEmployeesId(),"2下");
                    }
                }else{
                    stringStringHashMap.put(leaveApplicationOutput.getEmployeesId(),"2");
                }
            }else if(leaveApplicationOutput.getStartDate().after(upEndDate)){
                if(stringStringHashMap.containsKey(leaveApplicationOutput.getEmployeesId())){
                    if(stringStringHashMap.get(leaveApplicationOutput.getEmployeesId()).equals("全")){
                        break;
                    }else if(stringStringHashMap.get(leaveApplicationOutput.getEmployeesId()).equals("上")){
                        stringStringHashMap.put(leaveApplicationOutput.getEmployeesId(),"上2");
                    }
                }else{
                    stringStringHashMap.put(leaveApplicationOutput.getEmployeesId(),"2");
                }
            }
        }
        HashMap<Integer, BigDecimal> integerIntegerHashMap = new HashMap<>();
        for (Map.Entry<Integer, String> entry : stringStringHashMap.entrySet()) {
            if(entry.getValue().equals("全")){
                integerIntegerHashMap.put(entry.getKey(),new BigDecimal(8));
            }else if(entry.getValue().equals("上")){
                integerIntegerHashMap.put(entry.getKey(),new BigDecimal(4));
            }else if(entry.getValue().equals("上2")){
                integerIntegerHashMap.put(entry.getKey(),new BigDecimal(6));
            }else if(entry.getValue().equals("下")){
                integerIntegerHashMap.put(entry.getKey(),new BigDecimal(4));
            }else if(entry.getValue().equals("下2")){
                integerIntegerHashMap.put(entry.getKey(),new BigDecimal(6));
            }else if(entry.getValue().equals("2")){
                integerIntegerHashMap.put(entry.getKey(),new BigDecimal(2));
            }
        }
        return  integerIntegerHashMap;
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

    private List<AttendanceDailyDate> setData(List<AttendanceDailyDate> list) {
        ArrayList<AttendanceDailyDate> attendanceDailyDates = new ArrayList<>();
        if(list==null||list.size()==0){
            return attendanceDailyDates;
        }
        //用于存储返回数据
        AttendanceDailyDate before = null;
        AttendanceDailyDate after = null;
        Date signInTime = null;
        Date signOutTime = null;
        for (int i = 0 ;i<list.size();i++) {
            before = list.get(i);
            if(before.getPunchTime()!=null&&signInTime==null&&signOutTime==null){
                signInTime =before.getPunchTime();
                signOutTime = before.getPunchTime();
            }else{
                if(before.getPunchTime()!=null&&signInTime!=null&&before.getPunchTime().before(signInTime)){
                    signInTime = before.getPunchTime();
                }
                if(before.getPunchTime()!=null&&signInTime!=null&&before.getPunchTime().after(signOutTime)){
                    signOutTime = before.getPunchTime();
                }
            }
            if(i+1<list.size()){
                after = list.get(i+1);
                if(!after.getEmployeeNo().equals(before.getEmployeeNo())){
                    before.setSignInTime(signInTime);
                    before.setSignOutTime(signOutTime);
                    signInTime = null;
                    signOutTime = null;
                    attendanceDailyDates.add(before);

                }
            }else{
                before.setSignInTime(signInTime);
                before.setSignOutTime(signOutTime);
                signInTime = null;
                signOutTime = null;
                attendanceDailyDates.add(before);
            }
        }
        return  attendanceDailyDates;
    }


    private List<AttendanceDailyDate> setStateVal(List<AttendanceDailyDate> setValList,  List<AttendancePunch> latestWorkingList, List<AttendancePunch> firstOffWorkList,String workTime,String workOutTime) {
        //1 整理列表notPunchList   latestWorkingList  firstOffWorkList，获得员工该天的最迟上班时间，最早下班时间
        HashMap<Integer, AttendancePunch> attendancePunchListHashMap = (HashMap<Integer, AttendancePunch>)this.sortAttendancePunch(latestWorkingList,firstOffWorkList);
        for (AttendanceDailyDate attendanceDailyDate:setValList) {
            //若请假中有该人员的内容则需要处理，否则和考勤规则中的上下班时间进行比较
            if(attendanceDailyDate.getSignInTime()==null&&attendanceDailyDate.getSignOutTime()==null){
                attendanceDailyDate.setPunch("否");
            }else{
                attendanceDailyDate.setPunch("是");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date workTimeDate = null;
            Date workOutTimeDate = null;
            try {
                workTimeDate = sdf.parse(workTime);
                workOutTimeDate = sdf.parse(workOutTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(attendancePunchListHashMap.containsKey(attendanceDailyDate.getEmployeeId())){
                attendanceDailyDate.setIsLeave("是");
                AttendancePunch attendancePunch = attendancePunchListHashMap.get(attendanceDailyDate.getEmployeeId());
                    if(attendanceDailyDate.getSignInTime()==null){
                        attendanceDailyDate.setBeLate("是");
                    }else{
                        if(attendancePunch.getLatestWorkingTime()!=null){
                            attendanceDailyDate.setBeLate(attendanceDailyDate.getSignInTime().after(attendancePunch.getLatestWorkingTime())?"是":"否");
                        }else{
                            attendanceDailyDate.setBeLate(attendanceDailyDate.getSignInTime().after(workTimeDate)?"是":"否");
                        }
                    }
                    if(attendanceDailyDate.getSignOutTime()==null){
                        attendanceDailyDate.setLeaveEarly("是");
                    }else{
                        if(attendancePunch.getFirstOffWorkTime()!=null){
                            attendanceDailyDate.setLeaveEarly(attendanceDailyDate.getSignOutTime().before(attendancePunch.getFirstOffWorkTime())?"是":"否");
                        }else{
                            attendanceDailyDate.setLeaveEarly(attendanceDailyDate.getSignOutTime().before(workOutTimeDate)?"是":"否");
                        }
                    }
            }else{
                attendanceDailyDate.setIsLeave("否");
                //判断是否迟到
                if(attendanceDailyDate.getSignInTime()==null){
                    attendanceDailyDate.setBeLate("是");
                }else{
                    attendanceDailyDate.setBeLate(attendanceDailyDate.getSignInTime().after(workTimeDate)?"是":"否");
                }
                //判断是否早退
                if(attendanceDailyDate.getSignOutTime()==null){
                    attendanceDailyDate.setLeaveEarly("是");
                }else{
                    attendanceDailyDate.setLeaveEarly(attendanceDailyDate.getSignOutTime().before(workOutTimeDate)?"是":"否");
                }
            }
            if("否".equals(attendanceDailyDate.getLeaveEarly())&&"否".equals(attendanceDailyDate.getBeLate())){
                attendanceDailyDate.setStatusName("正常");
            }else{
                attendanceDailyDate.setStatusName("异常");
            }
            if(attendanceDailyDate.getPunch().equals("否")){
                attendanceDailyDate.setBeLate("否");
                attendanceDailyDate.setLeaveEarly("否");
            }
        }
        return setValList;
    }

    private HashMap<Integer, AttendancePunch> sortAttendancePunch(List<AttendancePunch> latestWorkingList, List<AttendancePunch> firstOffWorkList) {
        List<AttendancePunch> attendancePunchList = new ArrayList<>();
        HashSet<Integer> employeeIds = new HashSet<>();

        for (AttendancePunch attendancePunch:latestWorkingList) {
            employeeIds.add(attendancePunch.getEmployeeId());
        }
        for (AttendancePunch attendancePunch:firstOffWorkList) {
            employeeIds.add(attendancePunch.getEmployeeId());
        }
        for (Integer id:employeeIds) {
            AttendancePunch attendancePunch = new AttendancePunch();
            attendancePunch.setEmployeeId(id);
            attendancePunchList.add(attendancePunch);
        }
        for (AttendancePunch attendancePunch:attendancePunchList) {
            for (AttendancePunch attendancePunch2:latestWorkingList) {
                if(attendancePunch.getEmployeeId().equals(attendancePunch2.getEmployeeId())){
                    attendancePunch.setLatestWorkingTime(attendancePunch2.getLatestWorkingTime());
                    break;
                }
            }
            for (AttendancePunch attendancePunch3:firstOffWorkList) {
                if(attendancePunch.getEmployeeId().equals(attendancePunch3.getEmployeeId())){
                    attendancePunch.setFirstOffWorkTime(attendancePunch3.getFirstOffWorkTime());
                    break;
                }
            }
        }
        HashMap<Integer, AttendancePunch> integerAttendancePunchHashMap = new HashMap<>();
        for (AttendancePunch att:attendancePunchList) {
            integerAttendancePunchHashMap.put(att.getEmployeeId(),att);
        }
        return integerAttendancePunchHashMap;
    }

    public ResponseResult addTemporaryAttendanceData(PageData pageData) throws Exception{
        //1、根据人员的id获得员工的组织id和职务id
        AttendanceData employeeInfo = attendanceDataMapper.getInfoByEmployId(pageData);
        AttendanceData attendanceData = new AttendanceData();
        attendanceData.setOrganizationId(employeeInfo.getOrganizationId());
        attendanceData.setEmployeeId(Integer.valueOf(pageData.getString("employeeId")));
        attendanceData.setJobsId(employeeInfo.getJobsId());
        attendanceData.setAttendanceDeviceName((pageData.getString("attendanceDeviceName")));
        attendanceData.setState(1);
        int result = this.add(attendanceData);
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

//    public List<AttendanceDailyDate> findAttendanceDailyDate(PageData pageData) {
//        List<AttendanceDailyDate>  list = attendanceStatisticsMapper.findAttendanceDailyDate(pageData);
//        return  list;
//    }

    public List<AttendanceDailyDate> findAttendanceDailyDate(PageData pageData) {
        List<AttendanceDailyDate>  list = attendanceStatisticsNewMapper.findAttendanceDailyDate(pageData);
        return  list;
    }

    @Async
    public void adjustAttendanceDailyReport(PageData pageData) throws Exception{
        Integer resourceId = Integer.valueOf(pageData.get("resourceId").toString());
        Integer type = pageData.get("type")==null?null :Integer.valueOf(pageData.get("type").toString());
        //1、调整考勤日报表静态数据
            //1.1 根据请假申请、请假补录、加班申请、调休申请涉及到的考勤日的数据重新生成（前天及前天之前的时间部分）
        this.adjustAttendance(resourceId, type);
    }

    private void adjustAttendance(Integer resourceId, Integer type) throws Exception{
        Date begin = null;
        Date end = null;
        if(type==null){//考勤补录
            AttendanceDataOutput attendanceDataOutput = attendanceDataMapper.selectByPrimaryKey(Integer.valueOf(resourceId));
            begin = attendanceDataOutput.getPunchTime();
        }else if(type == ApprovalTypeEnum.LEAVE_TYPE.getCode()){//请假申请
            LeaveApplicationOutput leaveApplicationOutput = leaveApplicationMapper.selectByPrimaryKey(Integer.valueOf(resourceId));
            begin = leaveApplicationOutput.getStartDate();
            end = leaveApplicationOutput.getEndDate();
        }else if(type==ApprovalTypeEnum.OVERTIME_TYPE.getCode()){//加班
            OvertimeApplicationOutput overtimeApplicationOutput = overtimeApplicationMapper.selectByPrimaryKey(Integer.valueOf(resourceId));
            begin = overtimeApplicationOutput.getOverTimeDate();
        }else if(type==ApprovalTypeEnum.ADJUST_TYPE.getCode()){//调休申请
            OffApplicationOutput offApplicationOutput = offApplicationMapper.selectByPrimaryKey(Integer.valueOf(resourceId));
            begin = offApplicationOutput.getRestTime();
        }
        //将begin和end和系统时间前一天比较获得有效时间段
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,-1);
        Date time = instance.getTime();
        if(begin!=null){
            Integer val = DateUtils.compareData(time, begin);
            if(val<0){
                begin = null;
            }else{
                begin = DateUtils.reMoveExceptDate(begin);
            }

        }
        if(end!=null){
            Integer val = DateUtils.compareData(time, end);
            if(val<0){
                end = null;
            }else{
                end = DateUtils.reMoveExceptDate(end);
            }
        }
        List<Date> Dates = findDates(begin, end);
//        for (Date date:Dates) {
//            this.createAttendanceDailyDate(date);
//        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Date date:Dates) {
            attendanceStatisticsNewService.createAttendanceDailyReport(sdf.format(date));
        }
    }

    private List<Date> findDates(Date begin, Date end) {
        List<Date> objects = new ArrayList<>();
        if(begin==null&&end==null){
            return objects;
        }
        if(begin==null||end==null){
            if(begin !=null){
                objects.add(begin);
            }
            if(end!=null){
                objects.add(end);
            }
            return objects;
        }
        objects.add(begin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(begin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(end);
        // 测试此日期是否在指定日期之后
        while (end.after(calBegin.getTime())){
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            objects.add(calBegin.getTime());
        }
        return objects;
    }


    /**
     * 考勤月报表导出
     *
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    public String attendanceMonthExport(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "考勤月报表";
        String excelName = "考勤月报表";
        String[] rowsName = new String[]{"序号","姓名","工号","组织机构","职务","迟到次数","早退次数","未打卡次数","请假天数"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        pageData.put("userId",getUsers().getId().toString());
        if(getUsers().getAdministratorLevel()!=9){
            if(getUsers().getUserType()==0){
                pageData.put("employeeId",getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",getUsers().getOrganizationId().toString());
            }
        }
        List<AttendanceMonthReportOutput> attendanceMonthReportOutputList=findAttendanceMonthReport(pageData);
        if(attendanceMonthReportOutputList != null && attendanceMonthReportOutputList.size() > 0){
            int i=1;
            for(AttendanceMonthReportOutput output :attendanceMonthReportOutputList){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=output.getEmployeeName();
                objs[2]=output.getEmployeeNo();
                objs[3]=output.getOrganizationName();
                objs[4]=output.getJobsName();
                objs[5]=output.getLateness();
                objs[6]=output.getLeaveEarlyTimes();
                objs[7]=output.getNotPunchTimes();
                objs[8]=output.getDays();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    //考勤月报表
    public List<AttendanceMonthReportOutput> findAttendanceMonthReport(PageData pageData){
        List<AttendanceDailyDate> list = attendanceStatisticsMapper.selectSum(pageData);
//        List<AttendanceDailyDate> list = attendanceStatisticsNewMapper.selectSum(pageData);
        List<AttendanceMonthReportOutput> attendanceMonthReportOutputList = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(list)){
            List<AttendanceDataOutput> attendanceDataOutputs = attendanceDataMapper.selectBuluCont(pageData);
            for(AttendanceDailyDate attendanceDailyDate : list){
                AttendanceMonthReportOutput attendanceMonthReportOutput = new AttendanceMonthReportOutput();
                attendanceMonthReportOutput.setEmployeeName(attendanceDailyDate.getEmployeeName());
                attendanceMonthReportOutput.setEmployeeNo(attendanceDailyDate.getEmployeeNo());
                attendanceMonthReportOutput.setJobsName(attendanceDailyDate.getJobsName());
                attendanceMonthReportOutput.setOrganizationId(attendanceDailyDate.getOrganizationId());
                attendanceMonthReportOutput.setOrganizationName(attendanceDailyDate.getOrganizationName());
                pageData.put("no",attendanceDailyDate.getEmployeeNo());
                pageData.put("name",attendanceDailyDate.getEmployeeName());
                pageData.put("organizationId",attendanceDailyDate.getOrganizationId());
                pageData.put("empId",attendanceDailyDate.getEmployeeId());
                //迟到次数
                var sumLate = attendanceDailyDate.getBeLateTimes();
                if(sumLate == null){
                    attendanceMonthReportOutput.setLateness(0+"次");
                }else{
                    attendanceMonthReportOutput.setLateness(sumLate+"次");
                }
                //早退次数
                var sumLeave = attendanceDailyDate.getLeaveEarlyTimes();
                if(sumLeave == null){
                    attendanceMonthReportOutput.setLeaveEarlyTimes(0+"次");
                }else{
                    attendanceMonthReportOutput.setLeaveEarlyTimes(sumLeave+"次");
                }
                //未打卡次数
                var sumNotPunch = attendanceDailyDate.getPunchTimes();
                if(sumNotPunch == null){
                    attendanceMonthReportOutput.setNotPunchTimes(0+"次");
                }else{
                    attendanceMonthReportOutput.setNotPunchTimes(sumNotPunch+"次");
                }
                //请假小时数
                var hours = attendanceDailyDate.getHours();
                if(hours == null){
                    attendanceMonthReportOutput.setDays(0.0+"天");
                }else{
                    Double days ;
                    days = (double) hours / 8;
                    attendanceMonthReportOutput.setDays(days + "天");
                }

                attendanceDataOutputs = attendanceDataOutputs.stream()
                        .filter(e->e.getEmployeeId().equals(attendanceDailyDate.getEmployeeId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isEmpty(attendanceDataOutputs)){
                    attendanceMonthReportOutput.setSupplementTimes(0+"次");
                }else{
                    attendanceMonthReportOutput.setSupplementTimes(String.valueOf(attendanceDataOutputs.size()));
                }

                attendanceMonthReportOutputList.add(attendanceMonthReportOutput);
            }
        }

        return attendanceMonthReportOutputList.stream().
           collect(collectingAndThen(toCollection(() ->
              new TreeSet<>(comparing(AttendanceMonthReportOutput::getEmployeeNo))), ArrayList::new));
    }

    public List<AttendanceDailyDate> findAttendanceDailyDate1(PageData pageData) {
        List<AttendanceDailyDate>  list = attendanceStatisticsMapper.findAttendanceDailyDate1(pageData);
        return  list;
    }

    public List<AttendanceStatistics> getUnusualDetail(PageData pageData) {
        List<AttendanceStatistics> list =   attendanceStatisticsMapper.getUnusualDetail(pageData);
        return list;
    }
}
