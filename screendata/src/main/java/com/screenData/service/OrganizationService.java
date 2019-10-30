package com.screenData.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.utils.BigDecimalUtil;
import com.common.utils.HttpRequestUtil;
import com.google.common.collect.Lists;
import com.screenData.config.RedisComponent;
import com.screenData.core.base.BaseMapper;
import com.screenData.core.base.BaseService;
import com.screenData.core.base.MybatisBaseMapper;
import com.screenData.domain.output.*;
import com.screenData.mapper.jpa.CounterRepository;
import com.screenData.mapper.jpa.OrganizationRepository;
import com.screenData.mapper.mybatis.*;
import com.screenData.model.Config;
import com.screenData.model.Counter;
import com.screenData.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrganizationService extends BaseService<OrganizationOutput, Organization,Integer> {
    @Autowired
    private OrganizationRepository repository;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private WindowEmployeesService windowEmployeesService;
    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private ConfigService configService;
    @Autowired
    private EmployeesMapper employeesMapper;
    @Autowired
    private FeedbackInfoMapper feedbackInfoMapper;
    @Autowired
    private WindowEmployeesMapper windowEmployeesMapper;

    @Autowired
    private WindowMapper windowMapper;

    @Autowired
    private CounterRepository counterRepository;

    @Value("${queHost}")
    private String queHost;

    private final static Logger log = LoggerFactory.getLogger(OrganizationService.class);


    @Override
    public BaseMapper<Organization, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<OrganizationOutput> getMybatisBaseMapper() {
        return organizationMapper;
    }


    //获取业务列表
    public List<Config> getDoMatters() throws Exception{

        List<Config>yewumingchengList = configService.getListByParentIdAndState(99,0);
        //返回业务列表
        return yewumingchengList;
    }



    //根据事项id,获取服务窗口数量
     public Integer getWindowCount(Integer organizationId) {
        Integer allCounterCount = 0;

         //根据传来的事项id获取部门列表
         try {
             List<Config> deptCodeList = configService.getListByParentIdAndState(organizationId,0);
             if(deptCodeList ==null){
                 return 0;
             }
             for(Config config:deptCodeList){
                String key = config.getConfigValue();
                String value = redisComponent.get(key);

                if(value == null){
                    return allCounterCount;
                }
                net.sf.json.JSONObject object = generate(value);
                if(object == null){
                    return allCounterCount;
                }
                var counterCount = object.get("counterCount");
                if(counterCount == null){
                    counterCount = 0;
                }
                allCounterCount=allCounterCount+(Integer) counterCount;
            }
         } catch (Exception e) {
             return allCounterCount;
         }

         //获取服务窗口总数
         return allCounterCount;
     }

     private net.sf.json.JSONObject generate(String value){

         net.sf.json.JSONObject object = null;
         try {
             String[] arr = value.split(";");
             String queObject = arr[0];
             queObject = "{"+queObject+"}";
             object = net.sf.json.JSONObject.fromObject(queObject);
             if(object == null){
                 return null;
             }
         } catch (Exception e) {
             return object;
         }
         return object;
     }

    /**部门负责人*/
    public List<EmployeesOutput> selectDepartMange(Integer organizationId) throws Exception{

        List<EmployeesOutput> employeesOutputList = Lists.newArrayList();

        List<Config> deptCodeList = configService.getListByParentIdAndState(organizationId,0);
        if(CollectionUtils.isEmpty(deptCodeList)){
            return employeesOutputList;
        }
        deptCodeList = deptCodeList.stream().filter(e->e.getDisPlayOrderBy()==1000).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(deptCodeList)){
            return employeesOutputList;
        }
        List<String> orgNoList = deptCodeList.stream().map(Config::getConfigValue).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(orgNoList)){
            return employeesOutputList;
        }
        List<OrganizationOutput> organizationOutputList = organizationMapper.selectByOrgaNoList(orgNoList);
        if(CollectionUtils.isEmpty(organizationOutputList)){
            return employeesOutputList;
        }
        List<Integer> departmentalManagerList = organizationOutputList.stream()
                .map(Organization::getDepartmentalManager)
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(departmentalManagerList)){
            return employeesOutputList;
        }
        employeesOutputList = employeesMapper.selectByIdList(departmentalManagerList);

        //获取部门负责人的列表
        return employeesOutputList;
    }

    //当前开放窗口数量
//    public Integer getWindowOpenCount(Integer organizationId){
//
//        Integer allOnlineCounterCount = 0;
//
//        //根据传来的事项id获取部门列表
//        List<Config> deptCodeList = configService.getListByParentId(organizationId);
//        if(deptCodeList==null){
//            return 0;
//        }
//
//        for(Config config:deptCodeList){
//            String key = config.getConfigValue();
//            String value = redisComponent.get(key);
//            if(value == null){
//                return allOnlineCounterCount;
//            }
//            net.sf.json.JSONObject object = generate(value);
//            if(object == null){
//                return allOnlineCounterCount;
//            }
//
//            var onlineCounterCount = object.get("onlineCounterCount");
//
//            if(onlineCounterCount == null){
//                onlineCounterCount = allOnlineCounterCount;
//            }
//            allOnlineCounterCount = allOnlineCounterCount+(Integer) onlineCounterCount;
//        }
//
//        //获取当前总开放窗口数
//        return allOnlineCounterCount;
//    }

    public Integer getWindowOpenCount(Integer organizationId){

        Integer allOnlineCounterCount= this.getOpenWindow(organizationId).size();
        if(allOnlineCounterCount==null||allOnlineCounterCount==0){
            return 0;
        }

        //获取当前总开放窗口数
        return allOnlineCounterCount;
    }

//    public List<String> getOpenWindow(Integer organizationId){
//        List<String> windowNoList = Lists.newArrayList();
//
//        //根据传来的事项id获取部门列表
//        List<Config> deptCodeList = configService.getListByParentId(organizationId);
//        if(deptCodeList==null){
//            return windowNoList;
//        }
//        for(Config config:deptCodeList) {
//            String key = config.getConfigValue();
//            String value = redisComponent.get(key);
//            if(value == null){
//                return windowNoList;
//            }
//            String[] queues = value.split(";");
//
//            List<String> queueList = Arrays.asList(queues);
//
//            for(int i=0;i<queueList.size();i++){
//                if(i==0){
//                    continue;
//                }
//                //获取员工信息
//                String empInfo = queueList.get(i);
//                String[]array = empInfo.split(",");
//
//                try {
//                    if(array[0] == null){
//                        continue;
//                    }
//                    //获取工号
//                    String empNo = array[0];
//
//                    if(array[1] == null){
//                        continue;
//                    }
//                    //获取窗口号
//                    String windowNo = array[1];
//
//                    if(array[2] == null){
//                        continue;
//                    }
//                    //获取状态
//                    String status = array[2];
//
//                    //当获取的状态是1时添加窗口号
//                    if(status.equals("1") ){
//                        windowNoList.add(windowNo);
//                    }
//                } catch (Exception e) {
//                    return windowNoList;
//                }
//            }
//        }
//        //可办理窗口的窗口号
//        return windowNoList;
//    }

//    public List<String> getOpenWindow(Integer organizationId){
//        List<String> windowNoList = Lists.newArrayList();
//
//        //根据传来的事项id获取部门列表
//        List<Config> deptCodeList = configService.getListByParentIdAndState(organizationId,0);
//        if(deptCodeList==null){
//            return windowNoList;
//        }
//
//        List<String> orgNoList = deptCodeList.stream().map(Config::getConfigValue).collect(Collectors.toList());
//        for(Config config:deptCodeList) {
//            //获取每个组织编号
//            String key = config.getConfigValue();
//            //1是在线状态
//            List<Counter> counterList = counterRepository.findAllByDeptCodeAndOnlineFlag(key,1);
//            for(Counter counter : counterList){
//                String window = counter.getCode();
//                windowNoList.add(window);
//            }
//        }
//        //可办理窗口的窗口号
//        return windowNoList;
//    }

    /**可办理的窗口号列表*/
    public List<String> getOpenWindow(Integer organizationId){
        List<String> windowNoList = Lists.newArrayList();

        //根据传来的事项id获取部门列表
        List<Config> deptCodeList = configService.getListByParentIdAndState(organizationId,0);
        if(CollectionUtils.isEmpty(deptCodeList)){
            return windowNoList;
        }

        List<String> orgNoList = deptCodeList.stream().map(Config::getConfigValue).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(orgNoList)){
            return windowNoList;
        }

        List<Counter> counterList = counterRepository.findAllByOnlineFlagAndDeptCodeInOrderByCode(1,orgNoList);

        windowNoList = counterList.stream().map(Counter::getCode).collect(Collectors.toList());

        //可办理窗口的窗口号
        return windowNoList;
    }

    public BigDecimal getAverageaitingime(Integer organizationId){

        BigDecimal avarage = new BigDecimal(0);

        //设置总时长为0
        Integer allAverageWaitingMinute = 0;

        //根据传来的事项id获取部门列表
        List<Config> deptCodeList = configService.getListByParentId(organizationId);
        if(deptCodeList == null){
            return avarage;
        }
        for(Config config:deptCodeList) {
            String key = config.getConfigValue();
            String value = redisComponent.get(key);
            if(value == null){
                return avarage;
            }

            net.sf.json.JSONObject object =generate(value);

            if(object == null){
                return avarage;
            }
            //获取平均等待时长
            var averageWaitingMinute =  object.get("averageWaitingMinute");
            if(averageWaitingMinute==null){
                return avarage;
            }
            allAverageWaitingMinute=allAverageWaitingMinute + (Integer) averageWaitingMinute;
        }

        if(allAverageWaitingMinute==0){
            return avarage;
        }
         avarage = BigDecimalUtil.div(allAverageWaitingMinute.doubleValue(),Double.valueOf(deptCodeList.size()));

        //返回平均等待时长
        return avarage;
    }

    public Integer getCurrentWaitingNumber(Integer organizationId){
        //总的等候人数
        Integer allWaitingCount=0;

        //根据传来的事项id获取部门列表
        List<Config> deptCodeList = configService.getListByParentId(organizationId);
        if(deptCodeList == null){
            return allWaitingCount;
        }
        for(Config config:deptCodeList) {
            String key = config.getConfigValue();
            String value = redisComponent.get(key);

            if(value ==null){
                return allWaitingCount;
            }

            net.sf.json.JSONObject object = generate(value);

            if(object == null){
                return allWaitingCount;
            }
            //获取当前等待人数
            var waitingCount =  object.get("waitingCount");
            if(waitingCount == null){
                return allWaitingCount;
            }
            allWaitingCount= allWaitingCount + (Integer) waitingCount;
        }

        //当前等待的总人数
        return allWaitingCount;
    }

    public Integer getBookingOnLine(Integer organizationId){
        Integer allAppointment = 0;

        //根据传来的事项id获取部门列表
        List<Config> deptCodeList = configService.getListByParentId(organizationId);
        if(deptCodeList ==null){
            return allAppointment;
        }
        for(Config config:deptCodeList) {
            String key = config.getConfigValue();
            String value = redisComponent.get(key);
            if(value == null){
                return allAppointment;
            }
            net.sf.json.JSONObject object = generate(value);
            if(object == null){
                return allAppointment;
            }
            //网上预约人数
            var appointment = object.get("appointment");
            if(appointment==null){
                return allAppointment;
            }
            allAppointment= allAppointment + (Integer) appointment;
        }

        //网上预约总人数
        return allAppointment;
    }

    //上岗情况
    public BigDecimal getBoardingSituation(Integer organizationId){

        BigDecimal boardingSituation = new BigDecimal(0);

        //所有上岗窗口数
        Integer allCounterCount = 0;

        //根据传来的事项id获取部门列表
        List<Config> deptCodeList = configService.getListByParentId(organizationId);
        if(deptCodeList==null){
            return boardingSituation;
        }
        for(Config config:deptCodeList) {
            String key = config.getConfigValue();
            String value = redisComponent.get(key);

            if(value == null){
                return boardingSituation;
            }
            net.sf.json.JSONObject object = generate(value);
            if(object == null){
                return boardingSituation;
            }
            //共有的窗口数
            var counterCount =  object.get("counterCount");
            if(counterCount==null){
                return boardingSituation;
            }
            allCounterCount= allCounterCount + (Integer) counterCount;
        }

        if(allCounterCount==0){
            return boardingSituation;
        }

        List<String> windowNoList = getOpenWindow(organizationId);
        Integer doCount = windowNoList.size();
        if(doCount==0||doCount==null){
            return boardingSituation;
        }
        BigDecimal point = BigDecimalUtil.div(doCount.doubleValue(),allCounterCount.doubleValue());
        boardingSituation =  BigDecimalUtil.mul(point.doubleValue(),100);

        //上岗情况的百分比
        return boardingSituation;
    }

    //未上岗情况
//    public List<String> getAbsenceOfPosts(Integer organizationId) {
//
//        List<String> windowNoList = Lists.newArrayList();
//
//        //根据传来的事项id获取部门列表
//        List<Config> deptCodeList = configService.getListByParentId(organizationId);
//        if (deptCodeList == null) {
//            return windowNoList;
//        }
//        for (Config config : deptCodeList) {
//            String key = config.getConfigValue();
//            String value = redisComponent.get(key);
//
//            if (value == null) {
//                return windowNoList;
//            }
//
//            OrganizationOutput organizationOutput = organizationMapper.selectByOrgaNo(key);
//            if (organizationOutput == null) {
//                return windowNoList;
//            }
//
//            //获取该组织下所有的窗口号
//            PageData pageData = new PageData();
//            pageData.put("organizationId", organizationOutput.getId());
//            List<WindowOutput> windowOutputList = windowMapper.selectByOrganizationId(pageData);
//            for (WindowOutput windowOutput : windowOutputList) {
//                windowNoList.add(windowOutput.getWindowNo());
//            }
//
//            //获取窗口号
//
//            String[] queues = value.split(";");
//
//            List<String> queueList = Arrays.asList(queues);
//
//            for (int i = 0; i < queueList.size(); i++) {
//                if (i == 0) {
//                    continue;
//                }
//                //获取员工信息
//                try {
//                    String empInfo = queueList.get(i);
//                    String[] array = empInfo.split(",");
//
//                    String empNo = "";
//                    //获取工号
//                    if (array[0] != null) {
//                        empNo = array[0];
//                    }
//                    String windowNo = "";
//                    if (array[1] != null) {
//                        //获取窗口号
//                        windowNo = array[1];
//                    }
//                    String status = "";
//                    if (array[2] != null) {
//                        //获取状态
//                        status = array[2];
//                    }
//
//                    //当状态是1就是登陆状态下可以办理
//                    if (status.equals("1")) {
//                        windowNoList.remove(windowNo);
//                    }
//                } catch (Exception e) {
//                    return windowNoList;
//                }
//            }
//        }
//        //未上岗窗口号集合
//        return windowNoList;
//     }

    public List<String> getAbsenceOfPosts(Integer organizationId) {

        List<String> windowNoList = Lists.newArrayList();

        //根据传来的事项id获取部门列表
        List<Config> deptCodeList = configService.getListByParentId(organizationId);
        if (deptCodeList == null) {
            return windowNoList;
        }
        List<String> orgNoList = deptCodeList.stream().map(Config::getConfigValue).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(orgNoList)){
            return windowNoList;
        }
        List<Counter> counterList = counterRepository.findAllByOnlineFlagAndDeptCodeInOrderByCode(0,orgNoList);
        if(CollectionUtils.isEmpty(counterList)){
            return windowNoList;
        }
        windowNoList = counterList.stream().map(Counter::getCode).collect(Collectors.toList());
        //未上岗窗口号集合
        return windowNoList;
    }

    //满意度
    public BigDecimal getSatisfaction(Integer organizationId){

        BigDecimal statisPoint = new BigDecimal(0);

        Integer allFeedCount = 0;
        Integer allStatis=0;

        //根据传来的事项id获取部门列表
        List<Config> deptCodeList = configService.getListByParentId(organizationId);
        if(CollectionUtils.isEmpty(deptCodeList)){
            return statisPoint;
        }
        for(Config config:deptCodeList) {
            //获取部门编号
            String key = config.getConfigValue();
            OrganizationOutput organizationOutput = organizationMapper.selectByOrgaNo(key);
            if(organizationOutput==null){
                break;
            }
            //根据组织id查询出已经评价的反馈信息
            Integer feedCount  = feedbackInfoMapper.selectByOrganizationId(organizationOutput.getId());

            //如果没有查询到反馈信息则给100%满意度
            if(feedCount==0||feedCount ==null){
                return new BigDecimal(100);
            }
            //所有已经评价的反馈信息
            allFeedCount +=feedCount;

            //根据组织id查询出已经评价为满意的反馈信息
            Integer statis = feedbackInfoMapper.selectDissatisfaction(organizationOutput.getId());
            if(statis==null){
                break;
            }
            //所有已经评价为满意的反馈信息
            allStatis+=statis;

        }

        if(allFeedCount==0){
            return statisPoint;
        }

        if(allStatis==0){
            return new BigDecimal(0);
        }

        BigDecimal point = BigDecimalUtil.div(allStatis.doubleValue(),allFeedCount.doubleValue());
        statisPoint = BigDecimalUtil.mul(point.doubleValue(),100);

        //满意度百分比
        return statisPoint;
    }

    //不满意详情
    public List<String> getDissatisfaction(Integer organizationId){
        List<String> unStatisList = Lists.newArrayList();

        //根据传来的事项id获取部门列表
        List<Config> deptCodeList = configService.getListByParentId(organizationId);
        if(deptCodeList==null){
            return unStatisList;
        }
        for(Config config:deptCodeList) {
            //获取部门编号
            String key = config.getConfigValue();
            OrganizationOutput organizationOutput = organizationMapper.selectByOrgaNo(key);
            if(organizationOutput==null){
                return unStatisList;
            }
            List<FeedbackInfoOutput> feedbackInfoOutputList = feedbackInfoMapper.selectUnstatis(organizationOutput.getId());
            if(feedbackInfoOutputList==null||feedbackInfoOutputList.size()==0){
                return unStatisList;
            }
            for(FeedbackInfoOutput feedbackInfoOutput:feedbackInfoOutputList){
                String unstis = feedbackInfoOutput.getOneDetail()+","+feedbackInfoOutput.getTwoDetail();
                unStatisList.add(unstis);
            }
        }

        //不满意详情
        return unStatisList;
    }

    //今日办件量
    public Integer getTodayolume(Integer organizationId){

        //今日总办件量
        Integer allDoCount = 0;

        //根据传来的事项id获取部门列表
        List<Config> deptCodeList = configService.getListByParentId(organizationId);
        if(CollectionUtils.isEmpty(deptCodeList)){
            return allDoCount;
        }
        for(Config config:deptCodeList) {
            //获取部门编号
            String key = config.getConfigValue();
            OrganizationOutput organizationOutput = organizationMapper.selectByOrgaNo(key);
            if(organizationOutput==null){
                return allDoCount;
            }
            List<FeedbackInfoOutput> feedbackInfoOutputList = feedbackInfoMapper.selectTodayByOrganizationId(organizationOutput.getId());
            if(feedbackInfoOutputList==null){
                return allDoCount;
            }
            allDoCount+=feedbackInfoOutputList.size();
        }

        //今日办件总量
        return allDoCount;
    }

    //各窗口办件
    public List<String> getWindowTodayVolume(Integer organizationId){

        List<String> doList = Lists.newArrayList();

        //根据传来的事项id获取部门列表
        List<Config> deptCodeList = configService.getListByParentId(organizationId);
        if(deptCodeList==null){
            return doList;
        }
        List<String>orgnoList = deptCodeList.stream().map(Config::getConfigValue).collect(Collectors.toList());

        List<WindowOutput> windowOutputList = windowMapper.selectByOrganCode(orgnoList);

        //获取窗口号
        for(WindowOutput windowOutput:windowOutputList){
            Integer appointment =  feedbackInfoMapper.selectQueCount(windowOutput.getWindowNo());
            String domatters = windowOutput.getWindowNo()+"窗口"+appointment+"件";
            doList.add(domatters);
        }

        //返回各窗口办件
        return doList;
    }

    public List<WindowEmployeesOutput> getEmployeeList(Integer organizationId){

        List<WindowEmployeesOutput> windowEmployeesOutputList = Lists.newArrayList();

        //根据传来的事项id获取部门列表
        List<Config> deptCodeList = configService.getListByParentIdAndState(organizationId,0);
        if(CollectionUtils.isEmpty(deptCodeList)){
            return windowEmployeesOutputList;
        }

        List<String>orgnoList = deptCodeList.stream().map(Config::getConfigValue).collect(Collectors.toList());

        List<OrganizationOutput> organizationOutputList = organizationMapper.selectByOrgaNoList(orgnoList);

        List<Integer> orgIdList = organizationOutputList.stream().map(OrganizationOutput::getId).collect(Collectors.toList());

        return windowEmployeesMapper.selectOrganizationIdList(orgIdList);
    }

    /**获取QueryDeptQueuingSummaryByCompany列表的接口*/
    public void getMList(){

        String param = "companyCode=M&outputJson=1";

        String url = queHost + "/service/report.asmx/QueryDeptQueuingSummaryByCompany";
        try {
            String json = HttpRequestUtil.sendPost(url, param);
            if(json == ""){
                return;
            }

            Map<String, Object> jsonObject = JSONObject.parseObject(json);
            if(jsonObject == null){
                return;
            }
            JSONArray map = (JSONArray) jsonObject.get("attachObjectEx");
            if(map == null){
                return;
            }
            for (int i = 0; i < map.size(); i++) {
                Map<String, Object> queryDeptMap = (Map<String, Object>) map.get(i);
                //部门编号
                String code = (String) queryDeptMap.get("code");
                if(code==null||code==""){
                    continue;
                }
                //部门名称
                String name = (String) queryDeptMap.get("name");
                if(name==null||name==""){
                    continue;
                }
                //排队人数
                Integer count = (Integer) queryDeptMap.get("count");
                if(count==null){
                    count=0;
                }
                //等候人数
                Integer waitingCount = (Integer) queryDeptMap.get("waiting_count");
                if(waitingCount==null){
                    waitingCount=0;
                }
                //受理人数
                Integer calledCount = (Integer) queryDeptMap.get("called_count");
                if(calledCount==null){
                    calledCount=0;
                }
                //平均等待时长
                Integer averageWaitingMinute = (Integer) queryDeptMap.get("average_waiting_minute");
                if(averageWaitingMinute==null){
                    averageWaitingMinute=0;
                }
                //平均服务时长
                Integer averageServiceMinute = (Integer) queryDeptMap.get("average_service_minute");
                if(averageServiceMinute==null){
                    averageServiceMinute=0;
                }
                //预约未到人数
                Integer appointedCount = (Integer) queryDeptMap.get("appointed_count");
                if(appointedCount == null){
                    appointedCount = 0;
                }
                //预约已到人数
                Integer checkinedCount = (Integer) queryDeptMap.get("checkined_count");
                if(checkinedCount == null){
                    checkinedCount = 0;
                }
                //该部门下的窗口数量
                Integer counterCount = (Integer) queryDeptMap.get("counter_count");
                if(counterCount==null){
                    counterCount=0;
                }
                //该部门下窗口开通数量
                Integer onlineCounterCount = (Integer) queryDeptMap.get("online_counter_count");
                if(onlineCounterCount==null){
                    onlineCounterCount=0;
                }
                //受理事项数
                Integer transCount = (Integer) queryDeptMap.get("trans_count");
                if(transCount==null){
                    transCount=0;
                }
                //网上预约人数等于预约已到人数+预约未到人数
                Integer appointment = checkinedCount+appointedCount;
                if(appointment==null){
                    appointedCount=0;
                }
                String value = "count:"+count+",waitingCount:"+waitingCount+",calledCount:"+calledCount+
                            ",averageWaitingMinute:"+averageWaitingMinute+",averageServiceMinute:"+averageServiceMinute+
                        ",appointedCount:"+appointedCount+",checkinedCount:"+checkinedCount+",counterCount:"+counterCount+
                        ",onlineCounterCount:"+onlineCounterCount+",transCount:"+transCount+",appointment:"+appointment+";";
                StringBuilder sValue = new StringBuilder(value);
                //如果部门code不存在
                if(!redisComponent.hasKey(code)){
                    //设置到redis
                    redisComponent.set(code,value,Long.valueOf(60*60*288));
                    continue;
                }

                //根据部门编号获取登陆信息
                String longValue = redisComponent.get(code);
                StringBuilder sLongValue = new StringBuilder(longValue);

                if(longValue == null||longValue == ""){
                   continue;
                }
                //如果longValue中包含了员工登陆信息，不重新添加排队数据
                //删除平均等待时长的信息。
                sLongValue = sLongValue.delete(0,sLongValue.indexOf(";")+1);

                sValue = sValue.append(sLongValue);
                redisComponent.set(code,sValue.toString(),Long.valueOf(60*60*288));
            }
        } catch (Exception e) {
            log.error("排队数据跟新出现异常");
        }
        log.info("排队数据跟新完毕");
    }

    public List<OrganizationOutput> getByParentId(Integer id){
        List<OrganizationOutput> list=organizationMapper.getByParentId(id);
        List<OrganizationOutput> list1=new ArrayList<OrganizationOutput>();
        if(list!=null&&list.size()>0){
            for(OrganizationOutput output:list){
                List<WindowEmployeesOutput> list2=windowEmployeesService.getByOrganization(output.getId());
                if(list2!=null&&list2.size()>=3&&list2.size()<=6){
                    list1.add(output);
                }
            }
        }
        return list1;
    }
}
