package com.assistdecision.service;

import com.assistdecision.domain.output.*;
import com.assistdecision.mapper.mybatis.*;
import com.assistdecision.model.StationPeople;
import com.common.model.Config;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.BigDecimalUtil;
import com.common.utils.HttpClientUtil;
import com.common.utils.HttpRequestUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.VisitorSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author: young
 * @project_name: assist-decision
 * @description:
 * @date: Created in 2019-02-27  15:14
 * @modified by:
 */
@Service
public class AssistDecisionService {

    @Autowired
    private AttendanceStatisticsMapper attendanceStatisticsMapper;
    @Autowired
    private AppraisalPlanMapper appraisalPlanMapper;
    @Autowired
    private StationPeopleMapper stationPeopleMapper;
    @Autowired
    private FeedbackInfoMapper feedbackInfoMapper;
    @Value("${findDoTrendUrl}")
    private String findDoTrendUrl;

    public AttendanceStatisticsOutput findAttendance(PageData pageData){
        //迟到
        Integer sumLate = attendanceStatisticsMapper.selectSumLate(pageData);
        //请假
        Integer sumLeave =attendanceStatisticsMapper.selectSumLeave(pageData);
        //未打卡
        Integer sumNotPunch = attendanceStatisticsMapper.selectSumNotPunch(pageData);
        //早退
        Integer sumEarly = attendanceStatisticsMapper.selectEarly(pageData);

        AttendanceStatisticsOutput attendanceStatisticsOutput = new AttendanceStatisticsOutput();
        attendanceStatisticsOutput.setLateCount(sumLate);
        attendanceStatisticsOutput.setLeaveCount(sumLeave);
        attendanceStatisticsOutput.setNotPunchCount(sumNotPunch);
        attendanceStatisticsOutput.setEarlyCount(sumEarly);

        return attendanceStatisticsOutput;
    }

    public List<AppraisalPlanOutput> departManager(PageData pageData){
        List<AppraisalPlanOutput> appraisalPlanOutputs = appraisalPlanMapper.selectScore(pageData);
        if(appraisalPlanOutputs==null||appraisalPlanOutputs.size()==0){
            return new ArrayList<>();
        }
        return appraisalPlanOutputs;
    }

    public List<StationPeopleOutput> averageMinute(){
        List<StationPeopleOutput> stationPeopleOutputs = stationPeopleMapper.selectAverageService();
        if(stationPeopleOutputs == null||stationPeopleOutputs.size() == 0){
            return null;
        }
        return stationPeopleOutputs;
    }

    public List<FeedbackInfoOutput> business(){
        List<FeedbackInfoOutput> feedbackInfoOutputs = feedbackInfoMapper.selectBusiness();
        if(feedbackInfoOutputs==null||feedbackInfoOutputs.size()==0){
            return null;
        }
        return feedbackInfoOutputs;
    }

    public List<FeedbackInfoOutput> windowDo(){

        //获取前十个月的集合
        List<String> dateList = this.getDateList(10);
        Collections.reverse(dateList);

        List<FeedbackInfoOutput> feedbackInfoOutputs = Lists.newArrayList();
        for(String date:dateList){
            FeedbackInfoOutput feedbackInfoOutput = feedbackInfoMapper.selectWindowDo(date);
            feedbackInfoOutput.setDate(date);
            feedbackInfoOutputs.add(feedbackInfoOutput);
        }
        if(feedbackInfoOutputs.size()==0||feedbackInfoOutputs==null){
            return null;
        }
        return feedbackInfoOutputs;
    }

    public ResponseResult doCount(){
        ResponseResult result = HttpRequestUtil.sendGetRequest(findDoTrendUrl,null);
        if(result.getCode()!=200){
            return ResponseResult.success();
        }
        return result;
    }

    public List<FeedbackInfoOutput> hotDo(){
        List<FeedbackInfoOutput> feedbackInfoOutputs=new ArrayList<>();

        //获取前1个月
        List<String> dateList = this.getDateList(1);
        //获取当月
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        dateList.add(sdf.format(cal.getTime()));

        for(String date:dateList){
            feedbackInfoOutputs.addAll(feedbackInfoMapper.selectHotDo(date));
        }
        return feedbackInfoOutputs;
    }

    public List<FeedbackInfoOutput> satisAndComplete(){
        List<FeedbackInfoOutput> feedbackInfoOutputs = feedbackInfoMapper.selectAllCount();
        for(FeedbackInfoOutput feedbackInfoOutput:feedbackInfoOutputs){
            //根据组织id各查询出满意数量和跑一次数量
            Integer satisCount = feedbackInfoMapper.selectSatisByOrgaId(feedbackInfoOutput.getOrganizationId());
            Integer completeCount = feedbackInfoMapper.selectCompByOrgaId(feedbackInfoOutput.getOrganizationId());

            Integer allDoCount = feedbackInfoOutput.getDoCount();
            BigDecimal satispoint= new BigDecimal(satisCount).divide(new BigDecimal(allDoCount),4, RoundingMode.HALF_UP);
            BigDecimal complePoint = new BigDecimal(completeCount).divide(new BigDecimal(allDoCount),4, RoundingMode.HALF_UP);
            satispoint=BigDecimalUtil.mul(satispoint.doubleValue(),100);
            complePoint = BigDecimalUtil.mul(complePoint.doubleValue(),100);
            feedbackInfoOutput.setSatisfaction(satisCount);
            feedbackInfoOutput.setCompleteRate(completeCount);
            feedbackInfoOutput.setSatisPoint(satispoint);
            feedbackInfoOutput.setCompletePoint(complePoint);
        }
        return feedbackInfoOutputs;

    }

    private List<String> getDateList(Integer month){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar cal ;
        List<String> dateList = Lists.newArrayList();
        //获取前十个月；
        for(int i=1;i<=month;i++){
            cal = Calendar.getInstance();
            cal.add(Calendar.MONTH,-i);
            String date = sdf.format(cal.getTime());
            dateList.add(date);
        }
        return dateList;
    }

    public Map<String,AttendanceDetailOutput> findAttendanceDetail(PageData pageData) {

        Map<String,AttendanceDetailOutput> attendanceDetailOutputMap = new HashMap<>();
        Set<String> empNoList = new HashSet<>();

        List<AttendanceStatisticsOutput> earlyOutputs = attendanceStatisticsMapper.selectEarlyDetail(pageData);
        List<String> earlyEmpnoList = earlyOutputs.stream().map(AttendanceStatisticsOutput::getEmployeeNo).collect(Collectors.toList());

        List<AttendanceStatisticsOutput> leaveOutputs = attendanceStatisticsMapper.selectLeaveDetail(pageData);
        List<String> leaveEmpnoList = leaveOutputs.stream().map(AttendanceStatisticsOutput::getEmpno).collect(Collectors.toList());

        List<AttendanceStatisticsOutput> noPunchOutputs = attendanceStatisticsMapper.selectNotPunchDetail(pageData);
        List<String> noPunchempnoList = noPunchOutputs.stream().map(AttendanceStatisticsOutput::getEmployeeNo).collect(Collectors.toList());

        List<AttendanceStatisticsOutput> lateOutputs = attendanceStatisticsMapper.selectLateDetail(pageData);
        List<String> lateEmpnoList = lateOutputs.stream().map(AttendanceStatisticsOutput::getEmployeeNo).collect(Collectors.toList());

        empNoList.addAll(earlyEmpnoList);
        empNoList.addAll(leaveEmpnoList);
        empNoList.addAll(noPunchempnoList);
        empNoList.addAll(lateEmpnoList);

        if (CollectionUtils.isEmpty(empNoList)){
            return attendanceDetailOutputMap;
        }


        for(String empNo:empNoList){
            AttendanceDetailOutput attendanceDetailOutput = new AttendanceDetailOutput();
            earlyOutputs = earlyOutputs.stream().filter(e->e.getEmployeeNo() == empNo).collect(Collectors.toList());
            leaveOutputs = leaveOutputs.stream().filter(e->e.getEmployeeNo() == empNo).collect(Collectors.toList());
            noPunchOutputs = noPunchOutputs.stream().filter(e->e.getEmployeeNo() == empNo).collect(Collectors.toList());
            lateOutputs = lateOutputs.stream().filter(e->e.getEmployeeNo() == empNo).collect(Collectors.toList());
            attendanceDetailOutput.setEmpNo(empNo);
            attendanceDetailOutput.setLateDetail(lateOutputs);
            attendanceDetailOutput.setLeaveDetail(leaveOutputs);
            attendanceDetailOutput.setEarlyDetail(earlyOutputs);
            attendanceDetailOutput.setNoPunchDetail(noPunchOutputs);

            attendanceDetailOutputMap.put(empNo,attendanceDetailOutput);
        }

        return attendanceDetailOutputMap;
    }

    //各部门办件人流量
    public List<StationPeopleOutput> getVisitorNumber(PageData pageData){

        List<StationPeopleOutput> stationPeopleOutputList = stationPeopleMapper.selectAllDeptTake(pageData);

        return stationPeopleOutputList;

    }

    //查询办结量
    public List<StationPeopleOutput> getCompNumber(PageData pageData) {
        List<StationPeopleOutput> stationPeopleOutputList = stationPeopleMapper.selectAllDeptComp(pageData);
        return stationPeopleOutputList;
    }

    /**事项办件时长*/
    public List<StationPeopleOutput> mattersAverageMinutes() {

        List<StationPeopleOutput> stationPeopleOutputList = stationPeopleMapper.mattersAverageMinutes();
        return stationPeopleOutputList;
    }

    public List<StationPeopleOutput> deptAverageMinutes() {

        List<StationPeopleOutput> stationPeopleOutputList  = stationPeopleMapper.deptAverageMinutes();
        return stationPeopleOutputList;
    }

    public List<FeedbackInfoOutput> getSatisfyCondition(PageData pageData){
        List<FeedbackInfoOutput> feedbackInfoOutputList = feedbackInfoMapper.selectAllCountAndDept(pageData);

        for (FeedbackInfoOutput feedbackInfoOutput : feedbackInfoOutputList){
            pageData.put("organizationId",feedbackInfoOutput.getOrganizationId());
            //根据组织id各查询出满意数量
            Integer satisCount = feedbackInfoMapper.selectSatisByPageData(pageData);

            Integer unsatisCount = feedbackInfoMapper.selectUnSatisByPageData(pageData);

            Integer allDoCount = feedbackInfoOutput.getDoCount();
            BigDecimal satispoint= new BigDecimal(satisCount).divide(new BigDecimal(allDoCount),4, RoundingMode.HALF_UP);

            satispoint=BigDecimalUtil.mul(satispoint.doubleValue(),100);
            feedbackInfoOutput.setSatisPoint(satispoint);

            feedbackInfoOutput.setUnSatisCount(unsatisCount);
        }

        return feedbackInfoOutputList;
    }

    public List<FeedbackInfoOutput> getCompThisMonth(PageData pageData) {

        //查询出该部门下所有的事项
        List<FeedbackInfoOutput> feedbackInfoOutputList = feedbackInfoMapper.selectAllMaters(pageData);

        return feedbackInfoOutputList;
    }


    public PageInfo<List<AttendanceStatisticsOutput>> getWorkerDetail(HttpServletRequest request) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        PageData pageData = new PageData(request);

        PageHelper.startPage(pageData.getPageIndex(),pageData.getRows());

        //1.获取考勤的内容
        List<AttendanceStatisticsOutput> attendanceStatisticsOutputs = attendanceStatisticsMapper.workerDetail(pageData);

        if(!StringUtils.isBlank(pageData.getString("startTime"))){
            pageData.put("year",pageData.get("startTime").toString().substring(0,4));
            pageData.put("month",pageData.get("startTime").toString().substring(5,7));
        }else {
            pageData.put("year",sdf.format(new Date()).substring(0,4));
            pageData.put("month",sdf.format(new Date()).substring(5,7));
        }
        //2.获取考核得分
        List<AppraisalPlanOutput> appraisalPlanOutputList = appraisalPlanMapper.selectScoreByDate(pageData);

        for(AttendanceStatisticsOutput attendanceStatisticsOutput : attendanceStatisticsOutputs){
            for(AppraisalPlanOutput appraisalPlanOutput : appraisalPlanOutputList){
                if(attendanceStatisticsOutput.getEmployeeId().equals(appraisalPlanOutput.getEmployeeId())){
                    attendanceStatisticsOutput.setFinalScore(appraisalPlanOutput.getFinalScore());
                    attendanceStatisticsOutput.setDescription(appraisalPlanOutput.getDescription()+" "+
                            appraisalPlanOutput.getScoreTypeName()+" "+
                            appraisalPlanOutput.getRuleScore()+"分");
                }
            }
        }
        PageInfo pageInfo = new PageInfo(attendanceStatisticsOutputs);
        return pageInfo;
    }
}
