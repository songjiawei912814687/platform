package com.api.service;

import com.alibaba.fastjson.JSONObject;
import com.api.config.RedisComponent;
import com.api.domain.output.EmployeesOutput;
import com.api.domain.output.EvaluationResultsOutput;
import com.api.domain.output.OrganizationOutput;
import com.api.domain.output.StationPeopleOutput;
import com.api.mapper.jpa.EvaluationResultsRepository;
import com.api.mapper.jpa.FeedbackInfoRepository;
import com.api.mapper.mybatis.EmployeesMapper;
import com.api.mapper.mybatis.FeedbackInfoMapper;
import com.api.mapper.mybatis.OrganizationMapper;
import com.api.mapper.mybatis.StationPeopleMapper;
import com.api.model.EvaluationResults;
import com.api.model.FeedbackInfo;
import com.api.util.PostUtil;
import com.common.Enum.AppraiseStateEnum;
import com.common.Enum.MessageTypeEnum;
import com.common.Enum.SmsTemplateEnum;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.GetMessageTemplate;
import com.common.utils.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Service
public class EvaluationResultService  {

    @Autowired
    private EvaluationResultsRepository evaluationResultsRepository;
    @Autowired
    private StationPeopleMapper stationPeopleMapper;
    @Autowired
    private EmployeesMapper employeesMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private FeedbackInfoRepository feedbackInfoRepository;
    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private FeedbackInfoMapper feedbackInfoMapper;

    @Value("${sqlserver.url}")
    private String url;
    @Value("${sqlserver.user}")
    private String user;
    @Value("${sqlserver.password}")
    private String password;
    @Value("${sqlserver.className}")
    private String className;

    @Value("${feedback.deadline}")
    private String deadline;

    private static final Logger log = LoggerFactory.getLogger(EvaluationResultService.class);

    /**
     *  保存评价结果
     * @param map evaluate数据表
     * @return
     * @throws ParseException
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addEvaluationResult(Map<String,Object> map) throws ParseException {
        EvaluationResults evaluationResults = new EvaluationResults();
        //设置评价结果id
        evaluationResults.setResourceId((Integer) map.get("id"));
        //员工工号
        evaluationResults.setEmployeeCode((String) map.get("employee_code"));
        //获取窗口编号
        evaluationResults.setCounterCode((String) map.get("counter_code"));
        //评价结果编码分为A,B,C,D,Z z是未评价
        evaluationResults.setEvaluateCode((String) map.get("evaluate_code"));
        evaluationResults.setEvaluateVale((Integer) map.get("evaluate_value"));
        //群众手机
        evaluationResults.setAppraiserMobile((String) map.get("appraiser_mobile"));
        evaluationResults.setEvalDescription(null);
        //评价时间
        String date = (String) map.get("creation_time");
        evaluationResults.setCreationTime(getDate(date));
        //获取组织编号
        evaluationResults.setDeptCode((String) map.get("dept_code"));
        //设置关联取号数据的resourceId
        evaluationResults.setQueueingListId((Integer) map.get("queueing_list_id"));



        //根据评价结果的queueingListId获取取号叫号对象
        StationPeopleOutput stationPeople = stationPeopleMapper.selectByResourceId(evaluationResults.getQueueingListId());
        if (stationPeople == null) {
            return false;
        }
        //这步的主要目的是因为迈瑞同步来的取号数据可能会有多条评价结果，
        // 如果评价结果的组织编号和取号数据的组织编号不一致则不进行插入这条评价结果
        if(!stationPeople.getDeptCode().equals(evaluationResults.getDeptCode())){
            return false;
        }
        //保存
        Integer result = evaluationResultsRepository.save(evaluationResults).getResourceId();
        if (result < 0) {
            return false;
        }
//        log.info("评价结果的queueing_list_id:{}",map.get("queueing_list_id"));


        //根据员工工号查询出员工详情
        EmployeesOutput employeesOutput = employeesMapper.selectByEmpNo(evaluationResults.getEmployeeCode());
        //根据组织编号查询出组织对象
        OrganizationOutput organizationOutput = organizationMapper.selectByOrgaNo(evaluationResults.getDeptCode());
        //新增一条反馈信息
        FeedbackInfo feedbackInfo = new FeedbackInfo();
        feedbackInfo.setAmputated(0);
        feedbackInfo.setAppraiseState(AppraiseStateEnum.UN_APPRAISE.getCode());
        feedbackInfo.setCreatedDateTime(new Date());
        feedbackInfo.setCreatedUserId(0);
        feedbackInfo.setCreatedUserName("取号机推送");
        feedbackInfo.setFeedbackTime(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置最后评价时间
        feedbackInfo.setDeadline(sdf.format(calendar.getTime()));
        if(employeesOutput==null){
            feedbackInfo.setEmployeesName(" ");
            feedbackInfo.setEmployeesNo(" ");
        }else {
            //设置员工姓名
            feedbackInfo.setEmployeesName(employeesOutput.getName());
            //设置员工工号
            feedbackInfo.setEmployeesNo(employeesOutput.getEmployeeNo());
        }
        //设置最后跟新时间
        feedbackInfo.setLastUpdateDateTime(new Date());
        feedbackInfo.setLastUpdateUserId(0);
        feedbackInfo.setLastUpdateUserName("取号机推送");

        if(organizationOutput == null){
            feedbackInfo.setOrganizationId(null);
            feedbackInfo.setOrganizationName(" ");
        }else {
            feedbackInfo.setOrganizationId(organizationOutput.getId());
            feedbackInfo.setOrganizationName(organizationOutput.getName());
        }

        if (stationPeople.getName() == null || stationPeople.getName() == "") {
            feedbackInfo.setPersonName(" ");
        }else {
            feedbackInfo.setPersonName(stationPeople.getName());
        }
        //设置手机号码
        feedbackInfo.setPhone(stationPeople.getMobile());
        //设置办理事项
        feedbackInfo.setMatters(stationPeople.getMatters());
        //设置窗口号
        feedbackInfo.setWindowNo(evaluationResults.getCounterCode());
        //设置与取号叫号相关联的resourceId
        feedbackInfo.setResourceId(evaluationResults.getQueueingListId());
        //保存反馈信息
        Integer feedbackId = feedbackInfoRepository.save(feedbackInfo).getId();
        if (feedbackId < 0) {
            log.info("添加到反馈信息失败");
            return false;
        }
        log.info("返回的feedbackId:{}",feedbackId);

        //当评价为满意且跑一次的时候或者未评价时半小时后发送窗口评价的短信
        if ("A".equals(evaluationResults.getEvaluateCode())||"Z".equals(evaluationResults.getEvaluateCode())) {
            PageData pageData = new PageData();
            //窗口评价短信类型
            pageData.put("type",SmsTemplateEnum.CKPJ_TYPE.getCode());
            //窗口评价的类型的短信模板
            ResponseResult smsTempResult = HttpRequestUtil.sendMyGetRequest("http://10.32.250.88:8775/smstemplate/findByType?type="+SmsTemplateEnum.CKPJ_TYPE.getCode(),pageData);
            if(smsTempResult.getCode() != 200){
                log.info("没有找到短信模板");
                return false;
            }
            //获取模板内容
            String template = (String) smsTempResult.getData();
            var TepMap = GetMessageTemplate.getKey(template);

            //设置窗口号
            TepMap.put("ckh",feedbackInfo.getWindowNo());
            //设置办理事项内容
            TepMap.put("blywmc",feedbackInfo.getMatters());
            //获取短信的内容
            String rsContent = GetMessageTemplate.getContent(template, TepMap);

            List<String> reList = new ArrayList<>();
            reList.add(feedbackInfo.getPhone()+"/"+feedbackInfo.getPersonName());

            PageData pageData1 = new PageData();
            pageData1.put("sendList",reList);
            pageData1.put("description",rsContent);
            calendar = Calendar.getInstance();
            //获取半小时后
            calendar.add(Calendar.MINUTE, 30);
            //定时为半小时后发送
            pageData1.put("isTiming",1);
            //设置定时时间
            pageData1.put("timingTime",calendar.getTime());

            //设置发件箱中发送的类型为窗口评价
            pageData1.put("type", MessageTypeEnum.WINDOW_APPRAISE.getCode());
            //发送短信给群众
            ResponseResult responseResult = HttpRequestUtil.sendPostRequest( "http://10.32.250.88:8775/smsSend/form", pageData1);
            if(responseResult.getCode() != 200){
                log.error("发送给群众的短信发送失败");
                return false;
            }
            log.info("发送给群众评价短信发送成功");
            this.setFeedbackInfoToRedis(feedbackInfo);
            log.info("将反馈信息成功推送到redis");
            return true;
        }else {
            //发送窗口回访短信
            PageData pageData = new PageData();
            //窗口回访短信类型
            pageData.put("type",SmsTemplateEnum.CKHF_TYPE.getCode());
            //窗口回访的类型的模板短信
            ResponseResult smsTempResult = HttpRequestUtil.sendMyGetRequest("http://10.32.250.88:8775/smstemplate/findByType?type="+SmsTemplateEnum.CKHF_TYPE.getCode(),pageData);
            if(smsTempResult.getCode() != 200){
                log.info("没有找到短信回访的短信模板");
                return false;
            }
            //获取回访模板内容
            String hfTemplate = (String) smsTempResult.getData();

            List<String> hfList = new ArrayList<>();
            hfList.add(feedbackInfo.getPhone()+"/"+feedbackInfo.getPersonName());

            PageData pageData1 = new PageData();
            pageData1.put("sendList",hfList);
            pageData1.put("description",hfTemplate);

            pageData1.put("isTiming",0);
            //设置发件箱中发送的类型为窗口回访
            pageData1.put("type", MessageTypeEnum.WINDOW_VISIT.getCode());
            //发送短信给群众
            ResponseResult responseResult = HttpRequestUtil.sendPostRequest( "http://10.32.250.88:8775/smsSend/form", pageData1);
            if(responseResult.getCode() != 200){
                log.error("发送给群众的回访短信发送失败");
                return false;
            }
            log.info("发送给群众回访短信发送成功");
            this.setUnStatisToRedis(feedbackInfo);
            log.info("将反馈信息成功推送到redis");
            return true;
        }
    }

    public void setFeedbackInfoToRedis(FeedbackInfo feedbackInfo){
        //设置30分钟后过期时间为30分钟
        redisComponent.set("EVL_FEED_BACK:" + feedbackInfo.getId(), "", Long.valueOf(60*30));
    }

    public void setUnStatisToRedis(FeedbackInfo feedbackInfo){
        //设置为1秒后发送
        redisComponent.set("EVL_FEED_BACK:" + feedbackInfo.getId(), "", 1L);
    }

    private Date getDate(String time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = formatter.parse(time);
        return date;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveeva() throws SQLException,ClassNotFoundException {
        String sql = "SELECT ID,NAME,enabled FROM [eval].[evaluate_item]";
        ResultSet re = null;
        Connection conn = null ;
        Statement statement = null;
        String postUrl = "http://smrz.zjtax.gov.cn:8082/taxService/service/taxNsfwCommonApp!doService";
        String charset = "GBK";
        try {
            Class.forName(className);
            conn = DriverManager.getConnection(url, user, password);
            statement = conn.createStatement();
            re = statement.executeQuery(sql);
            while (re.next()) {
                Map<String,String> map = new HashMap<String,String>();
                map.put("domain.ywId","nsfw.saveeva");
                Map<String,String> paramMap = new HashMap<String,String>();
                paramMap.put("bid","nsfw.saveeva");
                paramMap.put("ver","1.0");
                paramMap.put("govhallid","133011002");
                paramMap.put("Evaid",String.valueOf(re.getInt("id")));
                paramMap.put("evaname",re.getString("name"));
                paramMap.put("yxbz",re.getBoolean("enabled")?"Y":"N");
                String parmJson= JSONObject.toJSONString(paramMap);
                map.put("domain.parmJson",parmJson);
                String res = PostUtil.doPost(postUrl,map,charset,true);
                System.out.println(res);
            }
        }finally {
            re.close();
            conn.close();
            statement.close();
        }
    }
}
