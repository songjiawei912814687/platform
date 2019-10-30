package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.*;
import com.attendance.mapper.jpa.*;
import com.attendance.mapper.mybatis.*;
import com.attendance.model.*;
import com.common.Enum.LeaveApplicationStatusEnum;
import com.common.model.PageData;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: young
 * @project_name: assist-decision
 * @description:
 * @date: Created in 2019-03-16  19:55
 * @modified by:
 */
@Service
public class AttendanceStatisticsNewService extends BaseService<AttendanceDailyDate, AttendanceStatistics, Integer> {

    @Autowired
    private AttendanceStatisticsRepository attendanceStatisticsNewRepository;
    @Autowired
    private AttendanceStatisticsNewMapper attendanceStatisticsNewMapper;
    @Autowired
    private AttendanceDataMapper attendanceDataMapper;
    @Autowired
    private HolidayMapper holidayMapper;
    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;
    @Autowired
    private OffApplicationMapper offApplicationMapper;
    @Autowired
    private AttendanceRuleNewRepository attendanceRuleNewRepository;
    @Autowired
    private AttendanceRuleConfigRepository attendanceRuleConfigRepository;
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired
    private VerificationRepository verificationRepository;
    @Autowired
    private VerificationMapper verificationMapper;

    private List<OffApplicationOutput> offApplicationOutputList;

    private List<AttendanceDataOutput> attendanceDataOutputList;

    private Map<Integer, List<AttendanceRuleNew>> attendanceConfigRules;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public BaseMapper<AttendanceStatistics, Integer> getMapper() {
        return attendanceStatisticsNewRepository;
    }

    @Override
    public MybatisBaseMapper<AttendanceDailyDate> getMybatisBaseMapper() {
        return attendanceStatisticsNewMapper;
    }

    /**
     * 生成考勤日报表的接口
     **/
    public void createAttendanceDailyReport(String reportDate) throws ParseException {
        PageData pageData = new PageData();
        pageData.put("days", reportDate);

        //获取需要拉取考勤日报表的员工列表,根据今天是否是工作日来判断
        var empList = getEmp(pageData);
        if (empList.size() == 0) {
            return;
        }
        //查看当天所有的调休列表
        offApplicationOutputList = offApplicationMapper.selectAllByToday(pageData);
        //拉取当天所有考勤数据
        attendanceDataOutputList = attendanceDataMapper.selectByDate(pageData);
        //所有考勤配置的map
        attendanceConfigRules = this.getRule();

        //生成的考勤日报表
        List<AttendanceStatistics> attendanceStatisticsList = new ArrayList<>();
        for(AttendanceDailyDate attendanceDailyDate : empList){
            //null 无需做null判断,add null也无妨
            AttendanceStatistics attendanceStatistics = getEmpAttendanceStatisticsNew(attendanceDailyDate,pageData);
            if(attendanceStatistics==null){
                continue;
            }
            attendanceStatisticsList.add(attendanceStatistics);
        }
        Integer size = attendanceStatisticsNewRepository.saveAll(attendanceStatisticsList).size();
        if (size == 0) {
            return;
        }
        return;
    }
    private AttendanceStatistics getEmpAttendanceStatisticsNew(AttendanceDailyDate attendanceDailyDate, PageData pageData) throws ParseException {
        //1.处理调休的逻辑
        if (resolveOffTime(attendanceDailyDate.getEmployeeId())) {
            return null;
        }
        //初始化考勤日报表对象设置人员属性，已打卡，日报表正常
        AttendanceStatistics attendanceStatistics =
                this.initAttendanceStatistics(attendanceDailyDate, pageData);
        //该员工当天所有打卡记录
        List<AttendanceDataOutput> attendanceDataListByEmpId = attendanceDataOutputList.stream().
                filter(AttendanceDataOutput -> AttendanceDataOutput.getEmployeeId().equals(attendanceDailyDate.getEmployeeId())).
                collect(Collectors.toList());
        boolean flag = false;
        //上午上班时间
        var amSingTime = getAttendanceRuleValue(pageData.getString("days"), attendanceDailyDate, 1);
        //上午下班时间
        var amSingOutTime = getAttendanceRuleValue(pageData.getString("days"),attendanceDailyDate,3);
        //下午上班时间
        var pmSingTime = getAttendanceRuleValue(pageData.getString("days"),attendanceDailyDate,2);
        //下午下班时间
        var pmSingOutTime = getAttendanceRuleValue(pageData.getString("days"), attendanceDailyDate, 4);
        //获取请假开始时间在今天上班时间前，请假结束时间在今天下班时间后的请假记录
        var leave = getEmpLeaveApplicationNotIn(pageData.getString("days"), attendanceDailyDate);
        //获取请假结束时间直到今天的请假记录
        var leave1 = getEmpLeaveApplicationIn(pageData.getString("days"), attendanceDailyDate);
        leave.addAll(leave1);
        //获取今天应该打卡的所有时间段
        Date amShouldSignInTime = sdf.parse(amSingTime);
        Date amShouldSignOutTime = sdf.parse(amSingOutTime);
        Date pmShouldSignInTime = sdf.parse(pmSingTime);
        Date pmShouldSignOutTime = sdf.parse(pmSingOutTime);
        //获取该员工请假列表
        var empLeaveList = new ArrayList<>(new LinkedHashSet<>(leave));
        //根据请假开始时间排序
        Collections.sort(empLeaveList,new Comparator<LeaveApplicationOutput>(){
            @Override
            public int compare(LeaveApplicationOutput l1,LeaveApplicationOutput l2) {
                if(l1.getReportStartDate().getTime()>l2.getReportStartDate().getTime()){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
        //处理打卡逻辑
        //如果没有请假设置核销假状态为正常
        if(empLeaveList.size() == 0){
            //如果打卡记录为空
            if (attendanceDataListByEmpId.size() == 0 ) {
                //这里将考情日报表设置为异常，未打卡设置为是
                attendanceStatistics.setStatusName("异常");
                attendanceStatistics.setPunch("是");
                return attendanceStatistics;
            }else{
                //这里判断上下班打卡情况
                attendanceStatistics = getSignTimeAndSignOutTimeType(attendanceStatistics, amSingTime,pmSingOutTime, attendanceDataListByEmpId);
            }
            attendanceStatistics.setVerification("正常");
        }
        //如果请假记录为一条
        else if(empLeaveList.size() == 1){
            attendanceStatistics = oneEmpLeave(amSingTime ,attendanceStatistics,empLeaveList.get(0),amShouldSignInTime,amShouldSignOutTime,pmShouldSignInTime,pmShouldSignOutTime,attendanceDataListByEmpId);
        }
        //如果请假记录为一条以上
        else{
            attendanceStatistics = solveSize2(amSingTime,attendanceStatistics,empLeaveList,amShouldSignInTime,amShouldSignOutTime,pmShouldSignInTime,pmShouldSignOutTime,attendanceDataListByEmpId);
        }

        //计算并设置请假时长
        int hours = 0 ;
        //1 2  5 6 >10不算请假时间
        var shouldSingTime = sdf.parse(amSingTime);
        var shouldSingOutTime = sdf.parse(pmSingOutTime);
        for (LeaveApplicationOutput leaveApplicationOutput:empLeaveList) {
            if(leaveApplicationOutput.getApplicationType()==1||leaveApplicationOutput.getApplicationType()==2||leaveApplicationOutput.getApplicationType()==5||leaveApplicationOutput.getApplicationType()==6
                    ||(leaveApplicationOutput.getApplicationType()>10)){
                hours+=0;
            }else if(leaveApplicationOutput.getApplicationType()==10){
                hours+=2;
            }else{
                if(shouldSingTime.compareTo(leaveApplicationOutput.getReportStartDate())>=0&&leaveApplicationOutput.getReportEndDate().compareTo(shouldSingOutTime)>=0){
                    hours+=8;
                }else{
                    hours+=4;
                }
            }
        }
        attendanceStatistics.setAbsenceHours(new BigDecimal(hours));
        return attendanceStatistics;
    }

    /**
     * 处理请假逻辑
     * @param leaveList 该员工当天的请假列表
     * @param attendanceStatisticsNew 日报表对象
     * @param attendanceDataListByEmpId 某员共当天所有的打卡数据
     * @return
     * @throws ParseException
     */
    private AttendanceStatistics getEmpLeave(List<LeaveApplicationOutput> leaveList,
                                                AttendanceStatistics attendanceStatisticsNew,
                                                List<AttendanceDataOutput> attendanceDataListByEmpId,
                                                String amSingTime, String pmSingOutTime,
                                                String pmSingTime, String amSingOutTime) throws ParseException {

        //如果没有请假直接返回日报表对象
        if (leaveList == null || leaveList.size() <= 0) {
            //如果没有请假核销假状态返回无
            attendanceStatisticsNew.setVerification("正常");
            return attendanceStatisticsNew;
        }
        //如果请假条数大于2，计算最后一条请假记录之前的逻辑
        if(leaveList.size()>=2){
            for(int i = 0; i< leaveList.size(); i++){
                if((i+1)>=leaveList.size()){
                    continue;
                }
                //如果当前请假的完整结束时间大于下一条请假记录的完整开始时间并且当前请假的完整开始时间小于等于下一条请假记录的完整结束时间，把两条记录合并为一条请假记录
                //修改请假开始时间与结束时间，这里可能会影响到哺乳假的情况
                if(leaveList.get(i).getReportEndDate().compareTo(leaveList.get(i+1).getReportStartDate()) >= 0
                        && leaveList.get(i).getReportStartDate().compareTo(leaveList.get(i+1).getReportEndDate()) <= 0){
                    List<Date> list = Arrays.asList(leaveList.get(i).getReportEndDate(),leaveList.get(i).getReportStartDate(),
                            leaveList.get(i+1).getReportStartDate(),leaveList.get(i+1).getReportEndDate());
                    list =list.stream().sorted().collect(Collectors.toList());
                    leaveList.get(i).setReportStartDate(list.get(0));
                    leaveList.get(i+1).setReportStartDate(list.get(0));
                    leaveList.get(i).setReportEndDate(list.get(list.size()-1));
                    leaveList.get(i+1).setReportEndDate(list.get(list.size()-1));
                }
            }
        }
        //处理请假逻辑
        for(LeaveApplicationOutput leaveApplicationOutput : leaveList){
            attendanceStatisticsNew =
                    getAsnByLeave(attendanceStatisticsNew,leaveApplicationOutput,attendanceDataListByEmpId,amSingTime,pmSingOutTime,pmSingTime,amSingOutTime);
        }
        Date amSingTime1 = sdf.parse(amSingTime);
        Date pmSingOutTime1 = sdf.parse(pmSingOutTime);
        //如果请假开始时间小于等于上午规则上班时间或者请假结束时间大于等于下午规则下班时间的记录大于等于2条
        var list = leaveList.stream().
                filter(e -> e.getReportStartDate().compareTo(amSingTime1) <=0 ||
                        e.getReportEndDate().compareTo(pmSingOutTime1) >= 0).
                collect(Collectors.toList());
        if(list.size()>=2){
            attendanceStatisticsNew.setVerification("正常");
        }
        return attendanceStatisticsNew;
    }

    //处理请假记录为一条的情况
    private AttendanceStatistics oneEmpLeave (String amSingTime ,AttendanceStatistics attendanceStatistics,LeaveApplicationOutput empLeave, Date amShouldSignInTime,Date amShouldSignOutTime,
                                             Date pmShouldSignInTime,Date pmShouldSignOutTime,List<AttendanceDataOutput> attendanceDataListByEmpId) throws ParseException{
        attendanceStatistics.setIsLeave("是");
        //单独处理哺乳假的特殊情况
        if(empLeave.getApplicationType().equals(LeaveApplicationStatusEnum.BREASTFEEDING_LEAVE.getCode())){
            //设置哺乳假的每天开始时间和结束时间,在下面需要重新设置回来
            empLeave = getLeaveApplicationOutput(amSingTime,empLeave);
            attendanceStatistics.setVerification("正常");
        }
        //如果请假开始时间小于上午上班开始时间
        if(empLeave.getReportStartDate().compareTo(amShouldSignInTime)< 0){
            attendanceStatistics=reportDateTimeLittleAmSignInTime(attendanceStatistics,empLeave,amShouldSignInTime,amShouldSignOutTime,pmShouldSignInTime,pmShouldSignOutTime,attendanceDataListByEmpId);
        }else{
            attendanceStatistics=reportDateTimeBiggerAmSignInTime(attendanceStatistics,empLeave,amShouldSignInTime,amShouldSignOutTime,pmShouldSignInTime,pmShouldSignOutTime,attendanceDataListByEmpId);
        }
        return attendanceStatistics;
    }

    //处理请假记录为一条的情况且请假开始时间小于上午上班开始时间的情况
    private AttendanceStatistics reportDateTimeLittleAmSignInTime(AttendanceStatistics attendanceStatistics,LeaveApplicationOutput empLeave,
                                                                  Date amShouldSignInTime,Date amShouldSignOutTime,Date pmShouldSignInTime,
                                                                  Date pmShouldSignOutTime,List<AttendanceDataOutput> attendanceDataListByEmpId){
        var leaveApplicationOutput = empLeave;
        Verification verification= new Verification();
        if(verificationRepository.getByLeaveApplicationId(leaveApplicationOutput.getId()).size()>0){
            int index= verificationRepository.getByLeaveApplicationId(leaveApplicationOutput.getId()).size()-1;
            verification = verificationRepository.getByLeaveApplicationId(leaveApplicationOutput.getId()).get(index);
        }
        verification.setLeaveApplicationId(leaveApplicationOutput.getId());
        verification.setLastUpdateDateTime(new Date());
        verification.setCreatedDateTime(new Date());
        verification.setAmputated(0);
        //如果请假结束时间等于上午下班时间则请假结束时间按下午上班时间计算,先记录

        //如果请假结束时间小于下午下班时间
        if(leaveApplicationOutput.getReportEndDate().compareTo(pmShouldSignOutTime)<0){
            //如果今天没打卡
            if(attendanceDataListByEmpId.size() == 0 ){
                attendanceStatistics.setPunch("是");
                attendanceStatistics.setStatusName("异常");
                attendanceStatistics.setVerification("异常");
                return attendanceStatistics;
            }
            //如果请假结束时间等于上午上班时间则请假结束时间按上午上班时间算
            if(leaveApplicationOutput.getReportEndDate().compareTo(amShouldSignOutTime) == 0){
                leaveApplicationOutput.setReportEndDate(pmShouldSignInTime);
            }
            //获取上午上班时间前到请假结束时间前的所有打卡记录
            List<AttendanceDataOutput>  punchBeforeReportEndDateTime =  attendanceDataListByEmpId.stream()
                    .filter(e -> e.getPunchTime().compareTo(leaveApplicationOutput.getReportEndDate()) <= 0 )
                    .collect(Collectors.toList());
            //获取请假结束时间之后到下午下班前的所有打卡记录
            List<AttendanceDataOutput> punchBetweenReportEndDateAndPmSignOut = attendanceDataListByEmpId.stream()
                    .filter(e ->pmShouldSignOutTime.getTime()-e.getPunchTime().getTime()>5*60*1000)
                    .collect(Collectors.toList());
            //获取核销假期间的打卡记录
            List<AttendanceDataOutput> verifycationList = attendanceDataListByEmpId.stream()
                    .filter(e ->e.getPunchTime().compareTo(leaveApplicationOutput.getReportStartDate())>=0 && e.getPunchTime().compareTo(leaveApplicationOutput.getReportEndDate()) <=0 )
                    .collect(Collectors.toList());
            //获取下午下班后的所有打卡记录
            List<AttendanceDataOutput> punchAfterPmSignOutList = attendanceDataListByEmpId.stream()
                    .filter(e ->e.getPunchTime().getTime()-pmShouldSignOutTime.getTime()>-5*60*1000)
                    .collect(Collectors.toList());
            if(punchBeforeReportEndDateTime.size() == 0){
                attendanceStatistics.setStatusName("异常");
                attendanceStatistics.setVerification("异常");
                //判断是否迟到，还是上班没打卡，如果上班打了卡，判断是不是迟到
                //如果请假结束时间之后到下午下班前打了卡，记录上班时间
                if(punchBetweenReportEndDateAndPmSignOut.size()>0){
                    attendanceStatistics.setSignInTime(punchBetweenReportEndDateAndPmSignOut.get(0).getPunchTime());
                    attendanceStatistics.setBeLate("是");
                    //判断有没有打下班卡，并判断是不是早退
                    if(punchAfterPmSignOutList.size()>0){
                        attendanceStatistics.setSignOutTime(punchAfterPmSignOutList.get(punchAfterPmSignOutList.size()-1).getPunchTime());
                        if(punchAfterPmSignOutList.get(punchAfterPmSignOutList.size()-1).getPunchTime().compareTo(pmShouldSignInTime)<0){
                            attendanceStatistics.setLeaveEarly("是");
                        }
                    }
                }else{
                    attendanceStatistics.setSignOutTime(punchAfterPmSignOutList.get(punchAfterPmSignOutList.size()-1).getPunchTime());
                    if(punchAfterPmSignOutList.get(punchAfterPmSignOutList.size()-1).getPunchTime().compareTo(pmShouldSignInTime)<0){
                        attendanceStatistics.setLeaveEarly("是");
                    }
                }
            }else{
                attendanceStatistics.setSignInTime(punchBeforeReportEndDateTime.get(0).getPunchTime());
                //设置核销假打卡时间
                verification.setVerificationTimeTwo(punchBeforeReportEndDateTime.get(0).getPunchTime());
                //判断有没有打下班卡，并判断是不是早退
                if(punchAfterPmSignOutList.size()>0){
                    attendanceStatistics.setSignOutTime(punchAfterPmSignOutList.get(punchAfterPmSignOutList.size()-1).getPunchTime());
                    if(punchAfterPmSignOutList.get(punchAfterPmSignOutList.size()-1).getPunchTime().compareTo(pmShouldSignInTime)<0){
                        attendanceStatistics.setLeaveEarly("是");
                    }
                }
            }

        }
        verificationRepository.save(verification);
        return  attendanceStatistics;
    }

    //处理请假记录为一条的情况且请假开始时间大于上午上班开始时间的情况
    private AttendanceStatistics reportDateTimeBiggerAmSignInTime(AttendanceStatistics attendanceStatistics,LeaveApplicationOutput empLeave, Date amShouldSignInTime,Date amShouldSignOutTime,Date pmShouldSignInTime,
                                                                  Date pmShouldSignOutTime,List<AttendanceDataOutput> attendanceDataListByEmpId){
        var leaveApplicationOutput = empLeave;
        Verification verification = new Verification();
        if(verificationRepository.getByLeaveApplicationId(leaveApplicationOutput.getId()).size()>0){
            int index= verificationRepository.getByLeaveApplicationId(leaveApplicationOutput.getId()).size()-1;
            verification = verificationRepository.getByLeaveApplicationId(leaveApplicationOutput.getId()).get(index);
        }
        verification.setLeaveApplicationId(leaveApplicationOutput.getId());
        verification.setLastUpdateDateTime(new Date());
        verification.setCreatedDateTime(new Date());
        verification.setAmputated(0);
        //如果请假结束时间为上午下班时间则请假结束时间按下午上班时间算
        if(leaveApplicationOutput.getReportEndDate().compareTo(pmShouldSignInTime) == 0){
            leaveApplicationOutput.setReportEndDate(pmShouldSignInTime);
        }
        //如果请假开始时间为下午上班时间则请假开始时间按上午下班时间算
        if(leaveApplicationOutput.getReportStartDate().compareTo(pmShouldSignInTime) == 0){
            leaveApplicationOutput.setReportStartDate(amShouldSignOutTime);
        }
        //获取上午上班时间的所有打卡记录
        List<AttendanceDataOutput>  punchBeforeAmSignTimeList =  attendanceDataListByEmpId.stream()
                .filter(e -> e.getPunchTime().compareTo(amShouldSignInTime) <= 0 )
                .collect(Collectors.toList());
        //获取上午上班时间后到请假开始时间前的所有打卡记录
        List<AttendanceDataOutput>  punchBetweenAmSignAndReportStartTimeList =  attendanceDataListByEmpId.stream()
                .filter(e -> e.getPunchTime().compareTo(amShouldSignInTime) > 0 && e.getPunchTime().compareTo(leaveApplicationOutput.getReportStartDate()) < 0 )
                .collect(Collectors.toList());
        //获取请假开始时间后的所有打卡记录
        List<AttendanceDataOutput>  punchAfterReportStartTimeList =  attendanceDataListByEmpId.stream()
                .filter(e -> e.getPunchTime().compareTo(leaveApplicationOutput.getReportStartDate()) >= 0 )
                .collect(Collectors.toList());
        //判断今天有没有打卡
        if(attendanceDataListByEmpId.size() == 0){
            attendanceStatistics.setVerification("异常");
            attendanceStatistics.setStatusName("异常");
            attendanceStatistics.setPunch("是");
        }else{
            //如果请假结束时间大于下午下班时间
            if(leaveApplicationOutput.getReportEndDate().compareTo(pmShouldSignOutTime) >= 0){
                 //判断上午上班前有没有打卡
                if(punchBeforeAmSignTimeList.size() == 0){
                    //判断上午上班后到请假开始时间前有没有打卡
                    if(punchBetweenAmSignAndReportStartTimeList.size() == 0){
                        attendanceStatistics.setSignOutTime(punchAfterReportStartTimeList.get(0).getPunchTime());
                        attendanceStatistics.setStatusName("异常");
                        verification.setVerificationTimeOne(punchAfterReportStartTimeList.get(0).getPunchTime());
                    }else{
                        attendanceStatistics.setSignInTime(punchBetweenAmSignAndReportStartTimeList.get(0).getPunchTime());
                        attendanceStatistics.setStatusName("异常");
                        attendanceStatistics.setBeLate("是");
                        //判断请假开始时间后有没有打卡
                        if(punchAfterReportStartTimeList.size() == 0){
                            attendanceStatistics.setVerification("异常");
                        }else{
                            verification.setVerificationTimeOne(punchAfterReportStartTimeList.get(0).getPunchTime());
                        }
                    }
                }else{
                    attendanceStatistics.setSignInTime(punchBeforeAmSignTimeList.get(0).getPunchTime());
                    //判断请假开始时间后有没有打卡
                    if(punchAfterReportStartTimeList.size()>0){
                        attendanceStatistics.setSignOutTime(punchAfterReportStartTimeList.get(punchAfterReportStartTimeList.size()-1).getPunchTime());
                        verification.setVerificationTimeOne(punchAfterReportStartTimeList.get(punchAfterReportStartTimeList.size()-1).getPunchTime());
                    }else{
                        attendanceStatistics.setVerification("异常");
                        attendanceStatistics.setStatusName("异常");
                        //判断上午上班时间后到请假开始时间前有没有打卡
                        if(punchBetweenAmSignAndReportStartTimeList.size() == 0){
                            return attendanceStatistics;
                        }else{
                            //判断上午上班时间后到请假开始时间最后一次打卡时间与上班打卡时间是不是重复打卡
                            if(!isRepeatPunch(punchBeforeAmSignTimeList.get(0).getPunchTime(),punchBetweenAmSignAndReportStartTimeList.get(punchBetweenAmSignAndReportStartTimeList.size()-1).getPunchTime())){
                                attendanceStatistics.setSignOutTime(punchBetweenAmSignAndReportStartTimeList.get(punchBetweenAmSignAndReportStartTimeList.size()-1).getPunchTime());
                                attendanceStatistics.setLeaveEarly("是");
                            }
                        }
                    }
                }
                verificationRepository.save(verification);
            }else{
                //请假结束时间小于下午下班时间
                attendanceStatistics=solveStu(attendanceStatistics,empLeave,amShouldSignInTime,pmShouldSignOutTime,attendanceDataListByEmpId);
            }
        }
        return attendanceStatistics;
    }

    //处理请一次假且请假时间在上班时间之内的情况
    private AttendanceStatistics solveStu(AttendanceStatistics attendanceStatistics,LeaveApplicationOutput empLeave,Date amShouldSignInTime,
                                          Date pmShouldSignOutTime,List<AttendanceDataOutput> attendanceDataListByEmpId){
        var leaveApplicationOutput = empLeave;
        Verification verification = new Verification();
        if(verificationRepository.getByLeaveApplicationId(leaveApplicationOutput.getId()).size()>0){
            int index= verificationRepository.getByLeaveApplicationId(leaveApplicationOutput.getId()).size()-1;
            verification = verificationRepository.getByLeaveApplicationId(leaveApplicationOutput.getId()).get(index);
        }
        verification.setLeaveApplicationId(leaveApplicationOutput.getId());
        verification.setLastUpdateDateTime(new Date());
        verification.setCreatedDateTime(new Date());
        verification.setAmputated(0);
        //获取上午上班时间的所有打卡记录
        List<AttendanceDataOutput>  punchBeforeAmSignTimeList =  attendanceDataListByEmpId.stream()
                .filter(e -> e.getPunchTime().compareTo(amShouldSignInTime) <= 0 )
                .collect(Collectors.toList());
        //获取上午上班时间后到请假开始时间前的所有打卡记录
        List<AttendanceDataOutput>  punchBetweenAmSignAndReportStartTimeList =  attendanceDataListByEmpId.stream()
                .filter(e -> e.getPunchTime().compareTo(amShouldSignInTime) > 0 && e.getPunchTime().compareTo(leaveApplicationOutput.getReportStartDate()) < 0 )
                .collect(Collectors.toList());
        //获取请假开始时间到请假结束时间的所有打卡记录
        List<AttendanceDataOutput>  punchBetweenResAndReeTimeList =  attendanceDataListByEmpId.stream()
                .filter(e -> e.getPunchTime().compareTo(leaveApplicationOutput.getReportStartDate()) >= 0 &&
                        e.getPunchTime().compareTo(leaveApplicationOutput.getReportEndDate()) <= 0 )
                .collect(Collectors.toList());

        //获取请假结束时间后的所有打卡记录
        List<AttendanceDataOutput>  punchAfeterReportEndTimeList =  attendanceDataListByEmpId.stream()
                .filter(e -> e.getPunchTime().compareTo(leaveApplicationOutput.getReportEndDate()) > 0 )
                .collect(Collectors.toList());
        //如果上班前没打卡
        if(punchBeforeAmSignTimeList.size() == 0 ){
            attendanceStatistics.setStatusName("异常");
            //判断上午上班时间后到请假时间前的打卡记录
            if(punchBetweenAmSignAndReportStartTimeList.size() != 0){
                attendanceStatistics.setBeLate("是");
                attendanceStatistics.setSignInTime(punchBetweenAmSignAndReportStartTimeList.get(0).getPunchTime());
            }
        }else{
            attendanceStatistics.setSignInTime(punchBeforeAmSignTimeList.get(0).getPunchTime());
        }
        //判断请假期间的打卡记录
        if(punchBetweenResAndReeTimeList.size() == 0 ){
            attendanceStatistics.setVerification("异常");
        }else if(punchBetweenResAndReeTimeList.size() == 1){
            attendanceStatistics.setVerification("异常");
            if(isFirstVerificationPunch(punchBetweenResAndReeTimeList.get(0).getPunchTime(),leaveApplicationOutput.getReportStartDate(),leaveApplicationOutput.getReportEndDate())){
                verification.setVerificationTimeOne(punchBetweenResAndReeTimeList.get(0).getPunchTime());
            }else{
                verification.setVerificationTimeTwo(punchBetweenResAndReeTimeList.get(0).getPunchTime());
            }
        }else{
            //判断是不是重复打卡，如果是重复打卡以第一次打卡计算
            if(isRepeatPunch(punchBetweenResAndReeTimeList.get(0).getPunchTime(),punchBetweenResAndReeTimeList.get(punchBetweenResAndReeTimeList.size()-1).getPunchTime())){
                attendanceStatistics.setVerification("异常");
                //判断核销假打卡时间属于第一次还是第二次
                if(isFirstVerificationPunch(punchBetweenResAndReeTimeList.get(0).getPunchTime(),leaveApplicationOutput.getReportStartDate(),leaveApplicationOutput.getReportEndDate())){
                    verification.setVerificationTimeOne(punchBetweenResAndReeTimeList.get(0).getPunchTime());
                }else{
                    verification.setVerificationTimeTwo(punchBetweenResAndReeTimeList.get(0).getPunchTime());
                }
            }else{
                verification.setVerificationTimeOne(punchBetweenResAndReeTimeList.get(0).getPunchTime());
                verification.setVerificationTimeTwo(punchBetweenResAndReeTimeList.get(punchBetweenResAndReeTimeList.size()-1).getPunchTime());
            }
        }
        //判断下午下班的打卡情况
        if(punchAfeterReportEndTimeList.size() == 0 ){
            attendanceStatistics.setStatusName("异常");
        }else{
            //判断是否早退，并记录下班时间
            Date d = punchAfeterReportEndTimeList.get(punchAfeterReportEndTimeList.size()-1).getPunchTime();
            attendanceStatistics.setSignOutTime(d);
            if(d.compareTo(pmShouldSignOutTime)<0){
                attendanceStatistics.setStatusName("异常");
                attendanceStatistics.setLeaveEarly("是");
            }
        }
        verificationRepository.save(verification);
        return  attendanceStatistics;
    }

    //处理请假条数大于一条的情况
    private AttendanceStatistics solveSize2(String amSingTime ,AttendanceStatistics attendanceStatistics,List<LeaveApplicationOutput> leaveList, Date amShouldSignInTime,Date amShouldSignOutTime,
                                            Date pmShouldSignInTime,Date pmShouldSignOutTime,List<AttendanceDataOutput> attendanceDataListByEmpId) throws ParseException{
        for(int i = 0; i< leaveList.size(); i++){
            if((i+1)>=leaveList.size()){
                continue;
            }
            //如果当前请假的完整结束时间大于下一条请假记录的完整开始时间并且当前请假的完整开始时间小于等于下一条请假记录的完整结束时间，把两条记录合并为一条请假记录
            if(leaveList.get(i).getReportEndDate().compareTo(leaveList.get(i+1).getReportStartDate()) >= 0
                    && leaveList.get(i).getReportStartDate().compareTo(leaveList.get(i+1).getReportEndDate()) <= 0){
                List<Date> list = Arrays.asList(leaveList.get(i).getReportEndDate(),leaveList.get(i).getReportStartDate(),
                        leaveList.get(i+1).getReportStartDate(),leaveList.get(i+1).getReportEndDate());
                list =list.stream().sorted().collect(Collectors.toList());
                leaveList.get(i).setReportStartDate(list.get(0));
                leaveList.get(i+1).setReportStartDate(list.get(0));
                leaveList.get(i).setReportEndDate(list.get(list.size()-1));
                leaveList.get(i+1).setReportEndDate(list.get(list.size()-1));
            }
            if(leaveList.get(i).getReportEndDate().compareTo(amShouldSignOutTime)>=0 && leaveList.get(i).getReportEndDate().compareTo(pmShouldSignInTime)<=0
            && leaveList.get(i+1).getReportStartDate().compareTo(pmShouldSignInTime)<=0 && leaveList.get(i+1).getReportStartDate().compareTo(amShouldSignInTime)>=0 ){
                leaveList.get(i).setReportStartDate(leaveList.get(i).getReportStartDate());
                leaveList.get(i+1).setReportStartDate(leaveList.get(i).getReportStartDate());
                leaveList.get(i).setReportEndDate(leaveList.get(i+1).getReportEndDate());
                leaveList.get(i+1).setReportEndDate(leaveList.get(i+1).getReportEndDate());
            }
        }
        for(LeaveApplicationOutput leaveApplicationOutput:leaveList){
            attendanceStatistics=oneEmpLeave(amSingTime,attendanceStatistics,leaveApplicationOutput,amShouldSignInTime,amShouldSignOutTime,pmShouldSignInTime,pmShouldSignOutTime,attendanceDataListByEmpId);
        }
        return attendanceStatistics;
    }

    /**
     * 判断是不是重复打卡
     * @param d1    第一次打卡时间
     * @param d2    第二次打卡时间
     * @return
     */
    public boolean isRepeatPunch(Date d1,Date d2){
        if(d2.getTime()-d1.getTime()<5*60*1000){
            return true;
        }
        return false;
    }

    /**
     *判断核销假打卡时间属于第一次核销假打卡还是第二次核销假打卡
     * @param d1 打卡时间
     * @param d2 请假开始时间
     * @param d3 请假结束时间
     * @return
     */
    public boolean isFirstVerificationPunch(Date d1 ,Date d2,Date d3){
        long t = d2.getTime()+d3.getTime() / 2;
        long t1 = d1.getTime();
        if(t1<t){
            return true;
        }
        return false;
    }

    /**
     * 这里遍历每一天请假并返回考勤日报表对象
     * @param attendanceStatisticsNew
     * @param leaveApplicationOutput
     * @return
     */
    private AttendanceStatistics getAsnByLeave(AttendanceStatistics attendanceStatisticsNew,
                                               LeaveApplicationOutput leaveApplicationOutput,
                                               List<AttendanceDataOutput> attendanceDataListByEmpId,
                                               String amSingTime, String pmSingOutTime,String pmSingTime,
                                               String amSingOutTime) throws ParseException {

        //取出哺乳假的日期
        Date breastLeaveReportStartDate = null;
        //取出哺乳假的结束
        Date breastLeaveReportEndDate = null ;

        //单独处理哺乳假的特殊情况
        if(leaveApplicationOutput.getApplicationType().equals(LeaveApplicationStatusEnum.BREASTFEEDING_LEAVE.getCode())){
            //哺乳假的开始时间
            breastLeaveReportStartDate = leaveApplicationOutput.getReportStartDate();
            //哺乳假的结束时间
            breastLeaveReportEndDate = leaveApplicationOutput.getReportEndDate();
            //设置哺乳假的每天开始时间和结束时间,在下面需要重新设置回来
            leaveApplicationOutput = getLeaveApplicationOutput(amSingTime,leaveApplicationOutput);
            attendanceStatisticsNew.setVerification("正常");
        }
        //处理核销假有问题
        //规则上班时间
       var startTime = sdf.parse(amSingTime);
       //规则下班时间
       var endTime = sdf.parse(pmSingOutTime);
       boolean flag = false;
       boolean flag1 = false;
       //取出请假开始时间
       var leaveStart = leaveApplicationOutput.getReportStartDate();
       //取出请假结束时间
       var leaveEnd = leaveApplicationOutput.getReportEndDate();
       //设置是请假状态
       attendanceStatisticsNew.setIsLeave("是");
       //如果请假开始时间小于等于规则上班时间
       if(leaveStart.compareTo(startTime) <= 0) {
           //将考情日报表的迟到改为正常
           attendanceStatisticsNew.setBeLate("否");
           attendanceStatisticsNew.setStatusName("正常");
           flag = true;
       }
       if(leaveEnd.compareTo(endTime) >= 0 ) {
           //将考勤日报表的早退改为正常
           attendanceStatisticsNew.setLeaveEarly("否");
           attendanceStatisticsNew.setStatusName("正常");
           flag1 = true;
       }
       //说明请的是一天以及一天以上的假直接返回
       if(flag && flag1) {
           attendanceStatisticsNew.setBeLate("否");
           attendanceStatisticsNew.setLeaveEarly("否");
           attendanceStatisticsNew.setVerification("正常");
           attendanceStatisticsNew.setStatusName("正常");
           attendanceStatisticsNew.setSignOutTime(null);
           attendanceStatisticsNew.setSignInTime(null);
           return attendanceStatisticsNew;
       }
        //处理一天之内的请假逻辑，一天可能会有多次请假情况出现，这段代码有问题
        //获取两次核销假的打卡时间
        var verificationList = attendanceDataListByEmpId.stream().filter(e -> e.getPunchTime().compareTo(leaveStart) >= 0
        && e.getPunchTime().compareTo(leaveEnd) <=0 ).collect(Collectors.toList());

       if(attendanceStatisticsNew.getEmployeeNo().equals("10927")){
           System.out.println("开始");
       }



        //如果核销假的时间列表小于2条记录
        if(verificationList.size()<2){
            //设置核销假状态
            attendanceStatisticsNew = setLeaveVerification(startTime, endTime,
                    attendanceDataListByEmpId,leaveApplicationOutput,attendanceStatisticsNew,leaveStart,leaveEnd);
            attendanceStatisticsNew =
                    ruleOutAtNoon(attendanceDataListByEmpId,attendanceStatisticsNew,leaveApplicationOutput,leaveStart,
                            leaveEnd,amSingTime,pmSingOutTime,amSingOutTime,pmSingTime);
        }else {

            // 将考勤日报表set 为正常并取 verificationList 第一条和最后一条数据 set 到请假对象中作为核销假
            //将考勤日报表set 为正常并取 verificationList 第一条set到请假对象的和销假的第一次打卡并保存
            attendanceStatisticsNew.setStatusName("正常");
        }
        Verification verification = new Verification();
        resolveVerfication(verification,verificationList,leaveApplicationOutput);
        //重新将哺乳假的日期设置回来
        if(leaveApplicationOutput.getApplicationType().equals(LeaveApplicationStatusEnum.BREASTFEEDING_LEAVE.getCode())){

            leaveApplicationOutput.setReportStartDate(breastLeaveReportStartDate);
            leaveApplicationOutput.setReportEndDate(breastLeaveReportEndDate);

            LeaveApplication leaveApplication = new LeaveApplication();
            BeanUtils.copyProperties(leaveApplicationOutput,leaveApplication);
            leaveApplicationRepository.save(leaveApplication);
        }
        return attendanceStatisticsNew;
    }

    /**
     * 处理哺乳假的逻辑
     * @param leaveApplicationOutput
     * @return
     * @throws ParseException
     */
    private LeaveApplicationOutput getLeaveApplicationOutput(String amSinging,LeaveApplicationOutput leaveApplicationOutput) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Date leaveDate = sdf1.parse(amSinging);
        String day = sdf1.format(leaveDate);
        var end = leaveApplicationOutput.getEndTime();
        var start = leaveApplicationOutput.getStartTime();
        var endTime = sdf.parse(day+" "+ end);
        var startTime = sdf.parse(day + " "+ start);
        leaveApplicationOutput.setReportEndDate(endTime);
        leaveApplicationOutput.setReportStartDate(startTime);
        return leaveApplicationOutput;
    }

    private void resolveVerfication(Verification verification,List<AttendanceDataOutput>verificationList,LeaveApplication leaveApplicationOutput){
        verification.setLeaveApplicationId(leaveApplicationOutput.getId());
        verification.setLastUpdateDateTime(new Date());
        verification.setCreatedDateTime(new Date());
        verification.setAmputated(0);
        if(verificationList.size()==0||verificationList==null){
            return;
        }
        if(verificationList.size()<2){
            verification.setVerificationTimeOne(verificationList.get(0).getPunchTime());
        }else {
            if(verificationList.get(0).getPunchTime().equals(verificationList.get(verificationList.size()-1).getPunchTime())){
                verification.setVerificationTimeOne(verificationList.get(0).getPunchTime());
            }else {
                verification.setVerificationTimeOne(verificationList.get(0).getPunchTime());
                verification.setVerificationTimeTwo(verificationList.get(verificationList.size()-1).getPunchTime());
            }
        }
        verificationRepository.save(verification);
    }

    /**
     * 写一个方法将请假的开始时间与结束时间各向相反的方向加半个小时在拉一遍考勤数据,前提是核销假的打卡记录小于2条
     * @param attendanceDataListByEmpId
     * @param leaveApplicationOutput
     * @param attendanceStatisticsNew
     * @param leaveStart
     * @param leaveEnd
     * @return
     */
    private AttendanceStatistics setLeaveVerification(Date shouldSignTime, Date shouldSignOutTime,
                                                      List<AttendanceDataOutput>attendanceDataListByEmpId,
                                                      LeaveApplicationOutput leaveApplicationOutput,
                                                      AttendanceStatistics attendanceStatisticsNew,Date leaveStart,Date leaveEnd){

        Calendar cal = Calendar.getInstance();
        cal.setTime(leaveStart);
        cal.add(Calendar.MINUTE,-30);
        Date leaveStartSub = cal.getTime();

        cal = Calendar.getInstance();
        cal.setTime(leaveEnd);
        cal.add(Calendar.MINUTE,30);
        Date leaveEndAdd = cal.getTime();
        //获取前后各加半小时两次核销假的打卡时间
        var verificationList = attendanceDataListByEmpId.stream().filter(e -> e.getPunchTime().compareTo(leaveStartSub) >= 0
                && e.getPunchTime().compareTo(leaveEndAdd) <=0 ).collect(Collectors.toList());
        if(verificationList.size()==0||verificationList==null){
            attendanceStatisticsNew = NoonLeave(attendanceStatisticsNew,leaveApplicationOutput,shouldSignOutTime,shouldSignTime);

        }else if(verificationList.size() == 1){
            attendanceStatisticsNew = NoonLeave(attendanceStatisticsNew,leaveApplicationOutput,shouldSignOutTime,shouldSignTime);
        }else {
            attendanceStatisticsNew = NoonLeave(attendanceStatisticsNew,leaveApplicationOutput,shouldSignOutTime,shouldSignTime);

        }

        return attendanceStatisticsNew;
    }

    private AttendanceStatistics NoonLeave(AttendanceStatistics attendanceStatisticsNew, LeaveApplicationOutput leaveApplicationOutput,Date shouldSignOutTime, Date shouldSignTime){
        boolean flag = false;
        if(leaveApplicationOutput.getReportEndDate().compareTo(shouldSignOutTime)>=0){
            //设置核销假状态异常
            attendanceStatisticsNew.setVerification("正常");
            flag=true;
        }else {
            //设置核销假状态异常
            attendanceStatisticsNew.setVerification("异常");
        }

        if(leaveApplicationOutput.getReportStartDate().compareTo(shouldSignTime)<=0 || flag){
            //设置核销假状态异常
            attendanceStatisticsNew.setVerification("正常");
        }else {
            //设置核销假状态异常
            attendanceStatisticsNew.setVerification("异常");
        }
        return attendanceStatisticsNew;
    }

    private AttendanceStatistics ruleOutAtNoon(List<AttendanceDataOutput> attendanceDataListByEmpId,AttendanceStatistics attendanceStatisticsNew,
                                                  LeaveApplicationOutput leaveApplicationOutput,Date leaveStart,Date leaveEnd,
                                                  String amSingTime, String pmSingOutTime,String amSingOutTime, String pmSingTime) throws ParseException {

        //上午上班
        var amT = sdf.parse(amSingTime);
        //下午下班
        var pmT = sdf.parse(pmSingOutTime);
        //上午下班
        var amN = sdf.parse(amSingOutTime);
        //下午上班
        var pmN = sdf.parse(pmSingTime);
        List<AttendanceDataOutput> verificationList = new ArrayList<>();

        //当请假开始时间小于上午上班时间
        if(leaveApplicationOutput.getReportStartDate().compareTo(amT) <= 0){
            verificationList = attendanceDataListByEmpId.stream().
                    filter(AttendanceDataOutput -> AttendanceDataOutput.getPunchTime().compareTo(leaveApplicationOutput.getReportEndDate()) <= 0).
                    collect(Collectors.toList());

            attendanceStatisticsNew.setBeLate("否");
            attendanceStatisticsNew.setLeaveEarly("否");
            attendanceStatisticsNew.setStatusName("正常");

            if(verificationList.size()>0){
                // 将考勤日报表set 为正常 并将 verificationList.get(0)set 上班签到时间
                attendanceStatisticsNew.setSignInTime(verificationList.get(0).getPunchTime());
                attendanceStatisticsNew.setVerification("正常");
            }

            //当我的请假结束时间>我的中午下班时间
            if(leaveApplicationOutput.getReportEndDate().compareTo(amN) >= 0) {
                verificationList = attendanceDataListByEmpId.stream().filter(AttendanceDataOutput -> AttendanceDataOutput.getPunchTime().compareTo(pmN) <= 0
                ).collect(Collectors.toList());

                if(verificationList.size() > 0) {
                    attendanceStatisticsNew.setSignInTime(verificationList.get(0).getPunchTime());
                    attendanceStatisticsNew.setVerification("正常");
                }
            }
        }

        //当我的请假结束时间>我的上午下班时间
        if(leaveApplicationOutput.getReportEndDate().compareTo(amN) >= 0){
            verificationList = attendanceDataListByEmpId.stream().
                    filter(AttendanceDataOutput -> AttendanceDataOutput.getPunchTime().compareTo(leaveApplicationOutput.getReportStartDate()) >= 0
                    && AttendanceDataOutput.getPunchTime().compareTo(pmN) <=0 ).collect(Collectors.toList());
            if(verificationList.size() < 2) {
                //这里将考勤日报表设置为异常 并写一个方法将请假的开始时间与结束时间各向相反的方向加半个小时在拉一遍考勤数据，并将取到的两个数据
                attendanceStatisticsNew = setLeaveVerification(amT,pmT,attendanceDataListByEmpId,leaveApplicationOutput,attendanceStatisticsNew,leaveStart,leaveEnd);
                // set 到请假数据中并直接保存请假数据 后期在调整成list插入 如果没有获取到就不要保存，如果只有一个就设置到第一次打卡的字段中
            }else {
                // 将考勤日报表set 为正常并取 verificationList 第一条set到请假对象的和销假的第一次打卡并保存，
                attendanceStatisticsNew.setStatusName("正常");
                attendanceStatisticsNew.setVerification("正常");
            }
        }
        if(leaveApplicationOutput.getReportEndDate().compareTo(pmT) >= 0){//当我的请假结束时间大于下午下班时间
            verificationList = attendanceDataListByEmpId.stream()
                    .filter(e -> e.getPunchTime().compareTo(leaveApplicationOutput.getReportStartDate()) >= 0)
                    .collect(Collectors.toList());
            if(verificationList.size()>0){
                //将考勤日报表set 为正常并取 verificationList 第一条set到请假对象的和销假的第一次打卡并保存
                attendanceStatisticsNew.setStatusName("正常");
            }else {
                attendanceStatisticsNew.setVerification("异常");
            }
        }

        //当请假开始时间小于等于下午上班时间并且请假结束时间大于等于规则下午下班时间且请假开始时间大于等于上午下班时间
        if((leaveApplicationOutput.getReportStartDate().compareTo(pmN)<=0||
                leaveApplicationOutput.getReportStartDate().compareTo(amN)>=0)&&
            leaveApplicationOutput.getReportEndDate().compareTo(pmT)>=0){
            attendanceStatisticsNew.setVerification("正常");

        }else if(leaveApplicationOutput.getReportStartDate().compareTo(amT)<=0&&
                (leaveApplicationOutput.getReportEndDate().compareTo(amN)>=0||
                 leaveApplicationOutput.getReportEndDate().compareTo(pmN)<=0)){
            attendanceStatisticsNew.setVerification("正常");
        }
        return attendanceStatisticsNew;
    }


    /**
     * 这里判断上下班的打卡情况
     *
     * @param attendanceStatisticsNew
     * @return
     */
    private AttendanceStatistics getSignTimeAndSignOutTimeType(AttendanceStatistics attendanceStatisticsNew,
                                                                  String amTime, String pmTime,
                                                                  List<AttendanceDataOutput> attendanceDataListByEmpId) throws ParseException {


        //如果员工打卡数据没有
        if(attendanceDataListByEmpId == null || attendanceDataListByEmpId.size() <= 0){
            //设置状态为异常
            attendanceStatisticsNew.setStatusName("异常");
            return attendanceStatisticsNew;
        }
        //否则取上班签到时间
        var startSignTime = attendanceDataListByEmpId.get(0).getPunchTime();
        //去下班签退时间
        var endSignTime = attendanceDataListByEmpId.get(attendanceDataListByEmpId.size() - 1).getPunchTime();
        //如果只有一次打卡记录

        if (startSignTime.equals(endSignTime)) {
            String middleTime = amTime.split(" ")[0]+" 12:00:00";
            Date middle = sdf.parse(middleTime);
            //case1 打卡时间为12点之前的打卡时间
            if(startSignTime.compareTo(middle)!=1){
                //这里将考勤日报表设置为早退，并将上班打卡set start 下班打卡set "--"设置异常
                attendanceStatisticsNew.setLeaveEarly("是");
                attendanceStatisticsNew.setBeLate("否");
                attendanceStatisticsNew.setSignInTime(startSignTime);
                attendanceStatisticsNew.setStatusName("异常");
                //获取规则上班时间
                var shouldSingTime = sdf.parse(amTime);
                //如果上班时间大于规则上班时间
                setVal(attendanceStatisticsNew, startSignTime, shouldSingTime);
            }else{
             //case2 打卡时间为12点之后的打卡时间
                //这里将考勤日报表设置为早退，并将上班打卡set end 下班打卡set "--"设置异常
                attendanceStatisticsNew.setBeLate("是");
                attendanceStatisticsNew.setLeaveEarly("否");
                attendanceStatisticsNew.setSignOutTime(startSignTime);
                attendanceStatisticsNew.setStatusName("异常");
                var shouldSingOutTime = sdf.parse(pmTime);
                //如果规则下班时间大于下班签退时间
                setVal2(attendanceStatisticsNew, endSignTime, shouldSingOutTime);
            }
        } else{
            //获取规则上班时间
            var shouldSingTime = sdf.parse(amTime);
            //规则下班时间
            var shouldSingOutTime = sdf.parse(pmTime);
            //如果上班时间大于规则上班时间
            setVal(attendanceStatisticsNew, startSignTime, shouldSingTime);
            //如果规则下班时间大于下班签退时间
            setVal2(attendanceStatisticsNew, endSignTime, shouldSingOutTime);
        }
        return attendanceStatisticsNew;
    }

    private void setVal2(AttendanceStatistics attendanceStatisticsNew, Date endSignTime, Date shouldSingOutTime) {
        if (shouldSingOutTime.compareTo(endSignTime) > 0) {
            //这里将考勤日报表设置为早退 并将下班打卡时间set end
            attendanceStatisticsNew.setLeaveEarly("是");
            attendanceStatisticsNew.setBeLate("否");
            attendanceStatisticsNew.setStatusName("异常");
            attendanceStatisticsNew.setSignOutTime(endSignTime);
        }else {
            attendanceStatisticsNew.setSignOutTime(endSignTime);
        }
    }

    private void setVal(AttendanceStatistics attendanceStatisticsNew, Date startSignTime, Date shouldSingTime) {
        if (startSignTime.compareTo(shouldSingTime)>0) {
            //这里将考勤日报表设置为迟到
            attendanceStatisticsNew.setBeLate("是");
            attendanceStatisticsNew.setLeaveEarly("否");
            attendanceStatisticsNew.setStatusName("异常");
            attendanceStatisticsNew.setSignInTime(startSignTime);
        }else {
            attendanceStatisticsNew.setSignInTime(startSignTime);
        }
    }


    /**
     * 初始化某员工日报表
     *
     * @param attendanceDailyDate
     * @param pageData
     * @return
     * @throws ParseException
     */
    private AttendanceStatistics initAttendanceStatistics(AttendanceDailyDate attendanceDailyDate, PageData pageData) throws ParseException {
        //这里将考勤日报表所有状态设置为正常
        AttendanceStatistics attendanceStatistics = new AttendanceStatistics();
        attendanceStatistics.setEmployeeId(attendanceDailyDate.getEmployeeId());
        attendanceStatistics.setEmployeeNo(attendanceDailyDate.getEmployeeNo());
        attendanceStatistics.setEmployeeName(attendanceDailyDate.getEmployeeName());
        attendanceStatistics.setOrganizationId(attendanceDailyDate.getOrganizationId());
        attendanceStatistics.setOrganizationName(attendanceDailyDate.getOrganizationName());
        //早退为否
        attendanceStatistics.setLeaveEarly("否");
        //迟到为否
        attendanceStatistics.setBeLate("否");
        //状态为正常
        attendanceStatistics.setStatusName("正常");
        //未打卡为否
        attendanceStatistics.setPunch("否");
        //核销假状态正常
        attendanceStatistics.setVerification("正常");
        //设置成未请假
        attendanceStatistics.setIsLeave("否");
        //设置考勤日报表生成的日期
        attendanceStatistics.setAttendanceDate(sdf.parse(pageData.getString("days") + " 02:00:00"));
        return attendanceStatistics;
    }

    /**
     * 请假记录 开始时间在当天工作开始时间前，结束时间在当天工作结束时间之后
     *
     * @return
     */
    private List<LeaveApplicationOutput> getEmpLeaveApplicationNotIn(String reportDate, AttendanceDailyDate attendanceDailyDate) {

        //上班时间
        String workTime = this.getAttendanceRuleValue(reportDate, attendanceDailyDate, 1);
        //下班时间
        String workOutTime = this.getAttendanceRuleValue(reportDate, attendanceDailyDate, 4);
        PageData pageData = new PageData();
        pageData.put("workTime", workTime);
        pageData.put("workOutTime", workOutTime);
        pageData.put("employeeId", attendanceDailyDate.getEmployeeId());
        return leaveApplicationMapper.findByEmployeeId(pageData);
    }

    /**
     * 请假结束时间直到今天的请假记录
     *
     * @return
     */
    private List<LeaveApplicationOutput> getEmpLeaveApplicationIn(String reportDate, AttendanceDailyDate attendanceDailyDate) {

        PageData pageData = new PageData();
        pageData.put("date", reportDate);
        pageData.put("employeeId", attendanceDailyDate.getEmployeeId());
        return leaveApplicationMapper.findTodayRecord(pageData);
    }


    /**
     * 判断是否有调休
     *
     * @param empId 员工id
     * @return
     */
    private boolean hasOffApplication(Integer empId) {

        if (offApplicationOutputList != null) {
            var offTimeByEmpList = this.offApplicationOutputList.stream()
                    .filter(OffApplicationOutput -> OffApplicationOutput.getEmployeesId().equals(empId))
                    .collect(Collectors.toList());
            if (offTimeByEmpList == null || offTimeByEmpList.size() <= 0) {
                return false;
            }
            return true;
        }
        return false;
    }
    /**
     * 根据type获取规则值 yyyy-MM-dd HH:mm:ss
     *
     * @param reportDate
     * @param attendanceDailyDate
     * @param type
     * @return
     */
    private String getAttendanceRuleValue(String reportDate, AttendanceDailyDate attendanceDailyDate, Integer type) {
        var ruleConfigId = attendanceDailyDate.getRuleConfigId();
        String workTime = "";
        if (ruleConfigId == null) {
            //获取上午上班时间
            if (type == 1) {
                workTime = reportDate + " " + "08:30:00";
            } else {
                workTime = reportDate + " " + "16:30:00";
            }
        } else {
            //获取上午上班考勤规则
            workTime = getAttendanceRuleValue(attendanceDailyDate, type);
            //如果考勤规则值为空
            if (workTime == null) {
                //获取上午上班时间
                if (type == 1) {
                    workTime = reportDate + " " + "08:30:00";
                } else {
                    workTime = reportDate + " " + "16:30:00";
                }
            } else {
                workTime = reportDate + " " + workTime;
            }
        }

        return workTime;
    }

    /**
     * 获取考勤规则值HH:mm:ss
     *
     * @param attendanceDailyDate
     * @param type                规则类型
     * @return
     */
    private String getAttendanceRuleValue(AttendanceDailyDate attendanceDailyDate, Integer type) {

        List<AttendanceRuleNew> attendanceRuleNewList = this.attendanceConfigRules.get(attendanceDailyDate.getRuleConfigId());
        var attendanceRuleNews = attendanceRuleNewList.stream().
                filter(AttendanceRuleNew -> AttendanceRuleNew.getType().equals(type)).collect(Collectors.toList());


        if (attendanceRuleNews == null || attendanceRuleNews.size() <= 0) {
            return null;
        }
        if (attendanceRuleNews.size() > 0) {
            var attendanceRuleNew = attendanceRuleNews.get(0);
            return attendanceRuleNew.getValue();
        }
        return null;
    }


    /**
     * 获取所有考勤规则的map对象
     *
     * @param
     * @return
     */
    private Map<Integer, List<AttendanceRuleNew>> getRule() {
        var attendsRoleConfigs = attendanceRuleConfigRepository.findAllByAmputated(0);
        if (attendsRoleConfigs == null || attendsRoleConfigs.size() <= 0) {
            return null;
        }
        Map<Integer, List<AttendanceRuleNew>> map = new HashMap<>();
        for (AttendanceRuleConfig attendanceRuleConfig : attendsRoleConfigs) {

            var attendanceRuleNews = attendanceRuleNewRepository.findAllByAttendanceRuleConfigIdAndAmputatedAndState(attendanceRuleConfig.getId(), 0, 1);
            if (attendanceRuleNews != null || attendanceRuleNews.size() > 0) {
                map.put(attendanceRuleConfig.getId(), attendanceRuleNews);
            }
        }
        return map;
    }

    /**
     * 获取需要生成报表的员工
     *
     * @param pageData
     * @return
     */
    private List<AttendanceDailyDate> getEmp(PageData pageData) throws ParseException {

        List<AttendanceDailyDate> attendanceDailyDateList = attendanceStatisticsNewMapper.findAttendanceDailyDate(pageData);

        if (pageData.getString("days") == null) {
            if (attendanceDailyDateList != null && attendanceDailyDateList.size() > 0) {
                return null;
            }
        } else {
            attendanceStatisticsNewMapper.deleteByDate(pageData);
            verificationMapper.deleteByDate(pageData);
        }
        List<AttendanceDailyDate> empList;
        //判断今天是否是工作日
        if (!this.isNotWorkDays(pageData.getString("days"))) {
            ///1.1 工作日，以员工为主表,调休申请中跨天的部分，获得需要打卡的人员信息
            empList = attendanceDataMapper.attendanceDailyData(pageData);
        } else {
            //1.2 非工作日 ，以加班表为主表，获得需要打卡的人员信息
            empList = attendanceDataMapper.overtimeDailyData(pageData);
        }
        return empList;
    }
    /**
     * 处理调休逻辑
     *
     * @param empId 员工对象
     * @return
     */
    private Boolean resolveOffTime(Integer empId) {
        if (hasOffApplication(empId)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是工作日
     *
     * @param days
     * @return
     */
    private boolean isNotWorkDays(String days) {
        //若是节假日管理中的非工作日，则为非工作日
        List<HolidayOutput> list = holidayMapper.isHoliday(days);
        if (list != null && list.size() > 0) {
            return true;
        }
        //若不是节假日管理中的非工作日，判断是否为节假日管理中的工作日
        List<HolidayOutput> workDayList = holidayMapper.isWorkDay(days);
        if (workDayList != null && workDayList.size() > 0) {
            return false;
        }
        //若非节假日管理中的非工作日和工作日则判断是否为周六周天，若为周六或者周天则为非工作日
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format1.parse(days);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> findDates(String dBegin, String dEnd) throws ParseException {
        //日期工具类准备
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //设置开始时间
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(dBegin));

        //设置结束时间
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(dEnd));

        //装返回的日期集合容器
        List<String> Datelist = new ArrayList<String>();

        // 每次循环给calBegin日期加一天，直到calBegin.getTime()时间等于dEnd
        while (format.parse(dEnd).after(calBegin.getTime()))  {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            Datelist.add(format.format(calBegin.getTime()));
        }
        return Datelist;
    }

    public void updateStatics(String beginDates,String endDates) throws ParseException {
        List<String> list=findDates(beginDates,endDates);
        for(String string:list){
            createAttendanceDailyReport(string);
        }
        if(list.size() == 0){
            createAttendanceDailyReport(beginDates);
        }
    }
}
