package com.message.service;

import com.common.Enum.*;
import com.common.model.PageData;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import com.common.utils.GetMessageTemplate;
import com.common.utils.HolidayUtils;
import com.common.utils.HttpRequestUtil;
import com.common.utils.Valid;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.message.config.RedisComponent;
import com.message.core.base.BaseMapper;
import com.message.core.base.BaseService;
import com.message.core.base.MybatisBaseMapper;
import com.message.domain.input.SMSSendInput;
import com.message.domain.output.*;
import com.message.enums.IsTimingEnum;
import com.message.enums.SendMessageEnum;
import com.message.enums.SendStateEnum;
import com.message.mapper.jpa.FeedbackInfoRepository;
import com.message.mapper.jpa.SMSSendRepository;
import com.message.mapper.mybatis.AttendanceStatisticsMapper;
import com.message.mapper.mybatis.SMSSendMapper;
import com.message.mapper.mybatis.SuggestionsMapper;
import com.message.model.FeedbackInfo;
import com.message.model.SMSSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.message.core.base.BaseController.PARAM_EORRO;
import static com.message.core.base.BaseController.SYS_EORRO;


@Service
public class SMSSendService extends BaseService<SMSSendOutput, SMSSend, Integer> {

    private final static Logger log = LoggerFactory.getLogger(SMSReceiveService.class);

    @Autowired
    private SMSSendRepository repository;
    @Autowired
    private FeedbackInfoRepository feedbackInfoRepository;

    @Override
    public BaseMapper<SMSSend, Integer> getMapper() {
        return repository;
    }

    @Autowired
    private SMSSendMapper sMSSendMapper;
    @Autowired
    private SmsTemplateService smsTemplateService;
    @Override
    public MybatisBaseMapper<SMSSendOutput> getMybatisBaseMapper() {
        return sMSSendMapper;
    }
    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private MessageGroupService messageGroupService;
    @Autowired
    private AttendanceStatisticsMapper attendanceStatisticsMapper;

    @Value("${spring.message.deadline}")
    private String deadline;
    @Value("${sendMessageUrl}")
    private String sendMessageUrl;

    public ResponseResult addMessages(SMSSendInput smsSendInput) throws Exception {
        List<String> sendList = smsSendInput.getSendList();
        var result = getGroupList(smsSendInput);
        if(result != null){
            sendList.addAll(result);
        }
        boolean flag = this.verificationMessage(sendList);
        if (!flag) {
            return ResponseResult.error("收件人信息错误");
        }
        Date date = null;

        Date createDate = null;
        if(smsSendInput.getIsReply() != null && smsSendInput.getIsReply() > 0 ){
            date = this.getReplyLimitTime(smsSendInput.getWorkDays());
        }
        createDate = smsSendInput.getCreatedDateTime();
        List<SMSSend> list = this.removeRepeat(sendList);
        if (sendList != null && sendList.size() > 0) {
            for (SMSSend sendInfo : list) {
                sendInfo.setReplyLimitDate(date == null ? null : date);
                sendInfo.setIsReply(smsSendInput.getIsReply());
                sendInfo.setAmputated(0);
                sendInfo.setType(smsSendInput.getType() == null ? 99 : smsSendInput.getType());//手动下发
                sendInfo.setDescription(smsSendInput.getDescription());
                sendInfo.setTimingTime(smsSendInput.getTimingTime());
                sendInfo.setIsTiming(smsSendInput.getIsTiming());
                sendInfo.setState(SendStateEnum.UN_SEND.getCode());
                sendInfo.setCreatedDateTime(createDate == null?new Date():createDate);
                if(getUsers()!=null){
                    sendInfo.setCreatedUserId(getUsers().getId());
                    sendInfo.setCreatedUserName(getUsers().getUsername());
                    sendInfo.setLastUpdateUserId(getUsers().getId());
                    sendInfo.setLastUpdateUserName(getUsers().getUsername());
                }else {
                    sendInfo.setCreatedUserId(0);
                    sendInfo.setCreatedUserName("job");
                    sendInfo.setLastUpdateUserId(0);
                    sendInfo.setLastUpdateUserName("job");
                }
                sendInfo.setLastUpdateDateTime(new Date());


            }
            List<SMSSend> smsSends = repository.saveAll(list);
            this.setValueToRedis(smsSends);
            if (smsSends != null && smsSends.size() > 0) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error(SYS_EORRO);
            }

        }
        return ResponseResult.error(PARAM_EORRO);
    }

    private List<String> getGroupList(SMSSendInput smsSendInput){
        if(smsSendInput.getGroupIdList() == null || "".equals(smsSendInput.getGroupIdList())){
            return null;
        }
        List<String> sendList = new ArrayList<>();
        var strs = smsSendInput.getGroupIdList().split(",");
        for(var str : strs){
            var groupId = 0;
            if(Valid.isNumeric(str)){
                groupId = Integer.parseInt(str);
            }
            if(groupId == 0){
                continue;
            }
            var list = messageGroupService.getGroupEmployeesAll(groupId);
            var result = copyToSendList(list);
            if(result != null){
                sendList.addAll(result);
            }
        }
        return sendList;
    }

    private List<String> copyToSendList(List<MessageGroupEmployeeOutput> list){
        if(list == null || list.size() <= 0){
            return null;
        }
        List<String> sendList = new ArrayList<>();
        for(var emp : list){
            sendList.add(emp.getPhoneNumber() + "/" +emp.getEmployeesName());
        }
        return sendList;
    }


    private Date getReplyLimitTime(Double workDays) throws Exception {
        //当前时间加工作日，若中间存在节假日，则剔除直到间隔为准确的工作日
        //获得所有的节假日
        //存放holiday的每一天
        ResponseResult responseResult = ServiceCall.getResult(loadBalancerClient, "/holiday/findAll", "attendance", null);
        List<LinkedHashMap> list = (List<LinkedHashMap>) responseResult.getData();
        List<String> holidayList = new ArrayList();
        List<String> workDayList = new ArrayList();
        //遍历holidayList
        if (list != null && list.size() > 0) {
            for (LinkedHashMap holidayOutput : list) {
                String sstartTime = holidayOutput.get("startDate").toString();
                String sendTime = holidayOutput.get("endDate").toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date startTime = sdf.parse(sstartTime);
                Date tomorrow = HolidayUtils.getTomorrow(startTime);
                if (!sstartTime.equals(sendTime)) {
                    if (Integer.valueOf(holidayOutput.get("isWorkDay").toString()) == IsWorkDayStatusEnum.NOT_WORKDAY.getCode()) {
                        holidayList.add(sstartTime);
                        holidayList.add(sdf.format(tomorrow));
                    } else {
                        workDayList.add(sstartTime);
                        workDayList.add(sdf.format(tomorrow));
                    }
                    while (!sdf.format(tomorrow).equals(sendTime)) {
                        tomorrow = HolidayUtils.getTomorrow(startTime);
                        if (Integer.valueOf(holidayOutput.get("isWorkDay").toString()) == IsWorkDayStatusEnum.NOT_WORKDAY.getCode()) {
                            holidayList.add(sdf.format(tomorrow));
                        } else {
                            workDayList.add(sdf.format(tomorrow));
                        }
                        startTime = tomorrow;
                    }
                } else {
                    if (Integer.valueOf(holidayOutput.get("isWorkDay").toString()) == IsWorkDayStatusEnum.NOT_WORKDAY.getCode()) {
                        holidayList.add(sdf.format(startTime));
                    } else {
                        workDayList.add(sdf.format(startTime));
                    }
                }
            }
        }
        Date scheduleActiveDate = HolidayUtils.getScheduleActiveDate(new Date(), holidayList, workDayList, (int) workDays.doubleValue());
        long time = scheduleActiveDate.getTime() + (long) (workDays.doubleValue() - (int) workDays.doubleValue()) * 24 * 60 * 60 * 1000;
        Date date = new Date(time);
        return this.getNextDay(date, holidayList, workDayList);
    }

    private Date getNextDay(Date date, List<String> holidayList, List<String> workDayList) throws Exception {
        if (!HolidayUtils.isWorkDay(date, holidayList, workDayList)) {
            Date tomorrow = HolidayUtils.getTomorrow(date);
            getNextDay(tomorrow, holidayList, workDayList);
        }
        return date;
    }


    /**
     * 第二步定时20秒发送回馈短信，第三步在FeedbackInfoService中
     **/
    public ResponseResult sendFeedbackInfo() {
        //查询出发送状态为0,评价状态为0的反馈信息的集合
        List<FeedbackInfo> feedbackInfoList = feedbackInfoRepository.
                findAllBySendStateAndAppraiseState(FeedbackInfoSendStateEnum.WAIT_SEND.getCode(), AppraiseStateEnum.UN_APPRAISE.getCode());
        if (feedbackInfoList.size() == 0) {
            return ResponseResult.error("暂时没有可以发送的反馈信息");
        }
        List<SMSSend> smsSendList = Lists.newArrayList();
        for (FeedbackInfo feedbackInfo : feedbackInfoList) {
            //获取群众号码
            String phone = feedbackInfo.getPhone();
            //或者窗口号
            String windowNo = feedbackInfo.getWindowNo();
            //获取办理事项
            String matters = feedbackInfo.getMatters();
            //获取群众名字
            String personName = feedbackInfo.getPersonName();
            SMSSend smsSend = new SMSSend();
            smsSend.setIsTiming(IsTimingEnum.NO.getCode());

            SmsTemplateOutput smsTemplateOutput = smsTemplateService.getByType(SmsTemplateEnum.CKPJ_TYPE.getCode());
            if (smsTemplateOutput == null) {
                return ResponseResult.error("无可用的模板");
            }
            var map = GetMessageTemplate.getKey(smsTemplateOutput.getDescription());

            map.put("ckh", windowNo);
            map.put("blywmc", matters);
            String content = GetMessageTemplate.getContent(smsTemplateOutput.getDescription(), map);
            smsSend.setDescription(content);
            smsSend.setState(SendStateEnum.UN_SEND.getCode());
            //设置为窗口评价
            smsSend.setType(MessageTypeEnum.WINDOW_APPRAISE.getCode());
            smsSend.setReceiveEmployeeName(personName);
            smsSend.setReceiveTelephone(phone);
            smsSend.setAmputated(0);
            smsSend.setCreatedDateTime(new Date());
            smsSend.setCreatedUserName("feedbackInfoJob");
            smsSend.setCreatedUserId(1);
            smsSend.setLastUpdateDateTime(new Date());
            smsSend.setLastUpdateUserId(1);
            smsSend.setLastUpdateUserName("feedbackInfoJob");

            smsSendList.add(smsSend);
            //发送状态设置为已经发送成功状态
            feedbackInfo.setSendState(FeedbackInfoSendStateEnum.SEND_SUCCESS.getCode());

            //成功将反馈信息加入到redis
            setFeedbackInfoToRedis(feedbackInfo);
            log.info("成功将反馈信息加入到redis");

            Integer result = feedbackInfoRepository.save(feedbackInfo).getId();
            if(result<0){
                return ResponseResult.error("修改反馈信息短信发送状态失败");
            }
        }
        smsSendList = repository.saveAll(smsSendList);
        if(smsSendList.size()==0){
            return ResponseResult.error("没有成功添加发送给办事群众的短信内容");
        }
        //将待发送的反馈短信的消息队列设置到redis
        this.setValueToRedis(smsSendList);
        return ResponseResult.success("成功将反馈信息发送状态修改成功");
    }


    private void  setFeedbackInfoToRedis(FeedbackInfo feedbackInfo) {
        //过期设置成二小时
        redisComponent.sentinelSet("FEED_BACK_INFO:" + feedbackInfo.getId(), "", Long.valueOf(60 * 60 * Integer.valueOf(deadline)));
        log.info("成功将反馈信息添加到redis");
    }

    private void setValueToRedis(List<SMSSend> smsSends) {
        for (SMSSend sMSSend : smsSends) {
            setValueToRedis(sMSSend);
        }
    }


    private void setValueToRedis(SMSSend sMSSend) {
        if (sMSSend.getIsTiming().equals(IsTimingEnum.YES.getCode())) {

            if (sMSSend.getTimingTime() != null) {
                long l = sMSSend.getTimingTime().getTime() - System.currentTimeMillis();
                if (l < 0) {
                    redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);
                }else {
                    l = l / 1000;
                    redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", l);
                }
            } else {
                redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);
            }
        } else {
            redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);
        }
    }

    private boolean verificationMessage(List<String> sendList) {
        for (String str : sendList) {
            if (str == null || "".equals(str) || str.split("/").length != 2) {
                return false;
            }
        }
        return true;
    }

    private List<SMSSend> removeRepeat(List<String> sendList) {
        HashSet<String> sets = new HashSet<>();
        ArrayList<SMSSend> objects = new ArrayList<>();
        for (String str : sendList) {
            if (sets.contains(str)) {
                continue;
            } else {
                SMSSend smsSend = new SMSSend();
                String[] split = str.split("/");
                smsSend.setReceiveEmployeeName(split[1]);
                smsSend.setReceiveTelephone(split[0]);
                objects.add(smsSend);
                sets.add(split[0]);
            }
        }
        return objects;
    }

    public int reSendMessage(Integer messageId) {
        SMSSend byId = repository.getById(messageId);
        if (byId == null || SendStateEnum.SEND_SUCCESS.getCode().equals(byId.getState())) {
            return 0;
        } else {
            this.setValueToRedis(byId);
            return 1;
        }
    }

    private int count;

    public void send(Integer messageId) {
        ResponseResult responseResult = null;
        count++;
        var smsSend = repository.getById(messageId);
        if(smsSend == null){
            return;
        }
        Map<String, Object> map = new HashMap<>();
        String[] strs = new String[]{smsSend.getReceiveTelephone()};
        map.put("mobiles", strs);
        map.put("smsContent", smsSend.getDescription());
        try {
             responseResult = HttpRequestUtil.sendPostRequest(sendMessageUrl, map);
            if (responseResult.getCode() == 200) {
                log.info("成功发送短信");
                smsSend.setState(SendMessageEnum.SUCCESS.getCode());
            } else {
                log.error("发送短信失败");
                smsSend.setState(responseResult.getCode());
            }
            smsSend.setTimingTime(new Date());
            repository.save(smsSend);
        }catch (Exception e ){
            log.error("发送短信接口的请求post发生异常:{}",e.getMessage());
        }
        log.info("发送短信条数:{}",count);
    }


    public List<SMSSendOutput> findNotPage(PageData pageData) {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        return sMSSendMapper.selectNotPage(pageData);
    }


    public int reSend() {
        List<Integer> stateList=new ArrayList<>();
        stateList.add(0);
        stateList.add(500);
        var smsSends = repository.findAllByStateIn(stateList);
        for (var send : smsSends) {
            this.reSendMessage(send.getId());
        }
        return 1;
    }

    //发送考勤提醒短信
    public void sendMeesages(){
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String tp = simpleDateFormat.format(date);
        String[] strings=tp.split("-");
        Integer year=Integer.parseInt(strings[0]);
        Integer month=Integer.parseInt(strings[1]);
        Integer day=Integer.parseInt(strings[2]);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(cal.get(Calendar.DAY_OF_WEEK)-1==1){
            if(day<4){
                day=day-3;
                if(month==1){
                    year=year-1;
                    month=12;
                    day=31+day;
                }else {
                    month=month-1;
                    if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                        day=31+day;
                    }else if(month==2){
                        if(year%4==0&&year%100!=0||year%400==0){
                            day=29+day;
                        }else {
                            day=28+day;
                        }
                    }else {
                        day=30+day;
                    }
                }
            }else {
                day=day-3;
            }
        }else {
            if(day==1){
                if(month==1){
                    year=year-1;
                    month=12;
                    day=31;
                }else {
                    month=month-1;
                }
                if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                    day=31;
                }else if(month==2){
                    if(year%4==0&&year%100!=0||year%400==0){
                        day=29;
                    }else {
                        day=28;
                    }
                }else {
                    day=30;
                }
            }else {
                day=day-1;
            }
        }
        String dates=null;
        dates=year+"-"+month+"-"+day;
        if(month<10){
            dates=year+"-0"+month+"-"+day;
        }
        if(day<10){
            dates=year+"-"+month+"-0"+day;
            if(month<10){
                dates=year+"-0"+month+"-0"+day;
            }
        }
//        int a=attendanceStatisticsMapper.selectOrganizationByName("中心窗口");
        PageData pageData=new PageData();
//        pageData.put("organizationId",","+a);
        pageData.put("days",dates);
        List<AttendanceDailyDate> list=attendanceStatisticsMapper.findAttendanceDailyDate(pageData);
        if(list!=null&&list.size()>0){
            for(AttendanceDailyDate dailyDate:list){
                if(dailyDate.getStatusName().equals("异常")){
                    EmployeesOutput employeesOutput=attendanceStatisticsMapper.selectByEmployeesId(dailyDate.getEmployeeId());
                    String description=null;
                    if(employeesOutput!=null&&!employeesOutput.equals("")){
                        if(dailyDate.getBeLate().equals("是")){
                            description=getContent("kqcdtx",month+"",day+"",employeesOutput.getEmployeeName());
                        }else if(dailyDate.getLeaveEarly().equals("是")){
                            description=getContent("kqzttx",month+"",day+"",employeesOutput.getEmployeeName());
                        }else if(dailyDate.getPunch().equals("是")){
                            description=getContent("kqwdktx",month+"",day+"",employeesOutput.getEmployeeName());
                        }

                        if(description==null||description.equals("")){
                            continue;
                        }
                        SMSSend smsSend=new SMSSend();
                        smsSend.setAmputated(0);
                        smsSend.setIsTiming(0);
                        smsSend.setCreatedDateTime(new Date());
                        smsSend.setCreatedUserId(0);
                        smsSend.setCreatedUserName("job");
                        smsSend.setLastUpdateDateTime(new Date());
                        smsSend.setLastUpdateUserId(0);
                        smsSend.setLastUpdateUserName("job");
                        smsSend.setDisplayOrder(0);
                        smsSend.setDescription(description);
                        smsSend.setState(0);
                        smsSend.setReceiveEmployeeName(employeesOutput.getEmployeeName());
                        smsSend.setReceiveTelephone(employeesOutput.getTel());
                        smsSend.setType(MessageTypeEnum.MANUAL_TYPE.getCode());
                        smsSend=repository.save(smsSend);
                        List<SMSSend> list2=new ArrayList<SMSSend>();
                        list2.add(smsSend);
                        this.setValueToRedis(smsSend);
                    }
                }
            }
        }

    }


    public String findTemplateByKey(String type) {
        SmsTemplateOutput smsTemplateOutput=smsTemplateService.getByType(type);
        if(smsTemplateOutput!=null&&!smsTemplateOutput.equals("")){
            return  smsTemplateService.getByType(type).getDescription();
        }
        return null;
    }

    public String getContent(String type,String month,String day,String name) {
        String content = findTemplateByKey(type);
        if (content != null) {
            var map = GetMessageTemplate.getKey(content);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                switch (entry.getKey()) {
                    case "y"://月
                        entry.setValue(month);
                        break;
                    case "r"://日
                        entry.setValue(day);
                        break;
                    case "sqr"://姓名
                        entry.setValue(name);
                        break;
                }
            }
            return GetMessageTemplate.getContent(content, map);
        }
        return null;
    }

}
