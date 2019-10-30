package com.api.service;

import com.alibaba.fastjson.JSONObject;
import com.api.config.RedisComponent;
import com.api.domain.output.EmployeesOutput;
import com.api.domain.output.OrganizationOutput;
import com.api.domain.output.StationPeopleOutput;
import com.api.mapper.jpa.*;
import com.api.mapper.mybatis.EmployeesMapper;
import com.api.mapper.mybatis.OrganizationMapper;
import com.api.mapper.mybatis.StationPeopleMapper;
import com.api.model.*;
import com.api.util.PostUtil;
import com.common.Enum.AppraiseStateEnum;
import com.common.Enum.MessageTypeEnum;
import com.common.Enum.SmsTemplateEnum;
import com.common.Enum.TakeNumberEnum;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.GetMessageTemplate;
import com.common.utils.HttpRequestUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Service
public class StationPeopleService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(StationPeopleService.class);
    @Autowired
    private StationPeopleRepository stationPeopleRepository;
    @Autowired
    private StationPeopleMapper stationPeopleMapper;
    @Autowired
    private EmployeesMapper employeesMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private EvaluationResultsRepository evaluationResultsRepository;
    @Autowired
    private EvaluateResultRepository evaluateResultRepository;
    @Autowired
    private FeedbackInfoRepository feedbackInfoRepository;
    @Autowired
    private CounterRepository counterRepository;
    @Autowired
    private QueueingListRepository queueingListRepository;

    @Autowired
    private StationGroupRepository stationGroupRepository;

    @Autowired
    private RedisComponent redisComponent;



    @Value("${sqlserver.url}")
    private String url;
    @Value("${sqlserver.user}")
    private String user;
    @Value("${sqlserver.password}")
    private String password;
    @Value("${sqlserver.className}")
    private String className;

    private static final String URL = "http://smrz.zjtax.gov.cn:8082/taxService/service/taxNsfwCommonApp!doService";

    private static final String CHARSET = "GBK";

    /**
     * 保存取号人信息
     * @param mapEx queueing_list数据表
     * @param map group数据表
     * @return
     * @throws ParseException
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void addRedisMessage(Map<String,Object> mapEx,Map<String,Object> map ,StationPeople stationPeopleEx) throws ParseException {
        StationPeople stationPeople = new StationPeople();

        //获取主键
        stationPeople.setResourceId((Integer) mapEx.get("id"));
        //获取身份证号码
        stationPeople.setCode((String) mapEx.get("code"));
        //获取部门编号
        stationPeople.setDeptCode((String) mapEx.get("dept_code"));
        //获取部门名称
        stationPeople.setDeptName((String) mapEx.get("dept_name"));

        stationPeople.setFullSequence((String) mapEx.get("full_sequence"));
        //获取队列编号
        stationPeople.setGroupCode((String) mapEx.get("group_code"));

        stationPeople.setGroupName((String) mapEx.get("group_name"));
        //获取群众名称
        stationPeople.setName((String) mapEx.get("name"));
        //获取群众手机号码
        stationPeople.setMobile((String) mapEx.get("mobile"));
        //获取取号时间
        stationPeople.setCreationTime(this.getDate((String) mapEx.get("creation_time")));
        //获取取号状态，默认已取号
        stationPeople.setStatusCode((String) mapEx.get("status_code"));
        stationPeople.setSortIndex((Integer) mapEx.get("sort_index"));
        stationPeople.setScheduleCode("schedule_code");
        stationPeople.setSequenceNumber((Integer) mapEx.get("sequence"));

        //获取事项名称
        stationPeople.setMatters((String)map.get("name"));
        //设置取号时间为当前时间
        stationPeople.setTakeNumberTime(new Date());
        if(stationPeopleEx!=null){
            if(stationPeopleEx.getStatus()!=null){
                stationPeople.setStatus(stationPeopleEx.getStatus());
            }
            if(stationPeople.getStatus() == TakeNumberEnum.START_WORK.getCode()){
                stationPeople.setStartWorkTime(stationPeopleEx.getStartWorkTime());
            }
            if(stationPeople.getStatus() == TakeNumberEnum.COMPLETE.getCode()){
                stationPeople.setCompleteTime(stationPeopleEx.getCompleteTime());
            }
        }
        stationPeopleRepository.save(stationPeople);
    }

    //推送取号数据
    public String addNsfw(Map<String, Object> mapEx, Map<String, Object> map ,String ywid,String state) throws ParseException {
        if(state.equals("3")){
            System.out.println("ok");
        }
        String url = "http://smrz.zjtax.gov.cn:8082/taxService/service/taxNsfwCommonApp!doService";
        String charset = "GBK";
        StationGroup stationGroup = stationGroupRepository.findByCode((String)mapEx.get("group_code"));
        Map<String,String> params = new HashMap<>();
        params.put("domain.ywId",ywid);
        Map<String,String> stationPeopleMap = new HashMap();
        stationPeopleMap.put("bid",ywid);
        stationPeopleMap.put("ver","1.0");
        stationPeopleMap.put("ID",mapEx.get("id").toString());
        stationPeopleMap.put("govhallid","133011002");//由税务局配置
        stationPeopleMap.put("Ticket",(String)mapEx.get("full_sequence"));
        stationPeopleMap.put("Idcard",(String)mapEx.get("code"));
        stationPeopleMap.put("Createtime",new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(getDate((String) mapEx.get("creation_time"))));
        stationPeopleMap.put("State",state);
        if(mapEx.get("call_time")!=null){
            stationPeopleMap.put("Calltime",new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(getDate((String)mapEx.get("call_time"))));
        }
        stationPeopleMap.put("Acccode",(String)mapEx.get("service_emp_code"));
        stationPeopleMap.put("Counterid",(String)mapEx.get("service_counter_code"));
        stationPeopleMap.put("Finishtime",(String)mapEx.get("finish_time"));
        stationPeopleMap.put("Evaid",map.get("id").toString());
        stationPeopleMap.put("bizid",stationGroup.getId().toString());
        String date = (String) map.get("creation_time");
        if(date!=null){
            stationPeopleMap.put("Evatime" ,new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(getDate(date)));
        }
        String parmJson= JSONObject.toJSONString(stationPeopleMap);
        System.out.println("--------------------------------------------------"+parmJson);
        params.put("domain.ParmJson",parmJson);
        String response = PostUtil.doPost(url,params,charset,true);
        System.out.println(response);
        return response;
    }

    public void callAndWait(Map<String,Object> mapEx,Map<String, Object> map) throws ParseException{
        PageData pageData = new PageData();
        pageData.put("statusCode",mapEx.get("status_code"));
        pageData.put("resourceId",(Integer)mapEx.get("id"));
        //若存在就更新，若不存在就插入
        StationPeople stationPeople = new StationPeople();
        if(stationPeopleMapper.selectByPrimaryKey((Integer)mapEx.get("id"))!=null){
            Integer result = stationPeopleMapper.updateByResultId(pageData);
        }
    }

    public void startWork(Map<String,Object> mapEx,Map<String, Object> map) throws ParseException{
        PageData pageData = new PageData();
        pageData.put("statusCode",mapEx.get("status_code"));
        pageData.put("resourceId",mapEx.get("id"));

        //状态设置为开始办理的状态
        pageData.put("status",TakeNumberEnum.START_WORK.getCode());
        pageData.put("startWorkTime",new Date());
        if(stationPeopleMapper.selectByPrimaryKey((Integer)mapEx.get("id"))!=null){
            Integer result = stationPeopleMapper.updateByResultId(pageData);
        }
//      logger.info("开始办理resourceId:{}",map.get("id"));
    }

    public void complete(Map<String,Object> mapEx,Map<String, Object> map) throws ParseException {
        PageData pageData = new PageData();
        pageData.put("statusCode",mapEx.get("status_code"));
        pageData.put("resourceId",mapEx.get("id"));

        //状态设置为已经完成的状态
        pageData.put("status",TakeNumberEnum.COMPLETE.getCode());
        pageData.put("completeTime",new Date());
        if(stationPeopleMapper.selectByPrimaryKey((Integer)mapEx.get("id"))!=null){
            Integer result = stationPeopleMapper.updateByResultId(pageData);
        }
    }

    //过号
    public void passNumber(Map<String,Object> map){
        PageData pageData = new PageData();
        pageData.put("statusCode",map.get("status_code"));
        pageData.put("resourceId",map.get("id"));

        //状态设置为已经完成的状态
        pageData.put("status",TakeNumberEnum.PASS_NUMBER.getCode());
        pageData.put("passTime",new Date());
        stationPeopleMapper.updateByResultId(pageData);
    }

    /**取消排队*/
    public void updateStatusCodeByResourceID(Map<String,Object> map){
        PageData pageData = new PageData();
        pageData.put("statusCode",map.get("status_code"));
        pageData.put("resourceId",map.get("id"));
        //状态设置为已经取消的状态.
        pageData.put("status", TakeNumberEnum.IS_CANCEL.getCode());
        pageData.put("cancelTime",new Date());
        stationPeopleMapper.updateByResultId(pageData);
    }

    /**时间转换格式*/
    private Date getDate(String time) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
        Date date1 = df.parse(time);
        return date1;
    }

    /**直连sqlServer NQueue.queue.counter数据表*/
    @Transactional(rollbackFor = Exception.class)
    public void sysSourceCounter() throws Exception{
        String sql = "SELECT id,code,name,dept_Code,group_codes,online_flag,current_emp_code,last_login_time " +
                "FROM [queue].[counter]";

        ResultSet re ;

        Class.forName(className);

        Connection conn= DriverManager.getConnection(url,user,password);
        Statement statement = conn.createStatement();
        re = statement.executeQuery(sql);
        List<Counter> counterList = Lists.newArrayList();
        while (re.next()){
            Counter counter = new Counter();
            counter.setId(re.getInt("id"));
            counter.setCode(re.getString("code"));
            counter.setName(re.getString("name"));
            counter.setDeptCode(re.getString("dept_code"));
            counter.setGroupCodes(re.getString("group_codes"));
            counter.setOnlineFlag(re.getInt("online_flag"));
            counter.setCurrentEmpCode(re.getString("current_emp_code"));
            counter.setLastLoginTime(re.getTimestamp("last_login_time"));
            counterList.add(counter);
        }
        Integer size = counterRepository.saveAll(counterList).size();
        if(size == 0){
            logger.error("没有查询到源窗口数据表");
            return;
        }
        logger.info("查询出"+size+"条窗口源数据");
    }

    /**时间条件同步取号源数据*/
    @Transactional(rollbackFor = Exception.class)
    public void getStationPeople(String startTime,String endTime) throws Exception{

        startTime = "'"+startTime+"'";
        endTime = "'"+endTime+"'";

        String sql = "SELECT qg.name as matters ,qq.* FROM [queue].[group] qg\n" +
                "LEFT JOIN [queue].[queueing_list] qq\n" +
                "on qg.code = qq.group_code\n" +
                "where qq.creation_time BETWEEN " +startTime +
                "and "+endTime + "order by qq.id";

        ResultSet re = null;
        Connection conn = null ;
        Statement statement = null;
        List<QueueingList> queueingListList = Lists.newArrayList();
        try {
            Class.forName(className);
            conn = DriverManager.getConnection(url, user, password);
            statement = conn.createStatement();
            re = statement.executeQuery(sql);

            while (re.next()) {
                QueueingList queueingList = new QueueingList();
                queueingList.setId(re.getInt("id"));
                queueingList.setSort_index(re.getInt("sort_index"));
                queueingList.setSequence(re.getInt("sort_index"));
                queueingList.setFull_sequence(re.getString("full_sequence"));
                queueingList.setCode(re.getString("code"));
                queueingList.setName(re.getString("name"));
                queueingList.setMobile(re.getString("mobile"));
                queueingList.setPriority_code(re.getString("priority_code"));
                queueingList.setGroup_code(re.getString("group_code"));
                queueingList.setCreation_time(re.getTimestamp("creation_time"));
                queueingList.setCall_time(re.getTimestamp("call_time"));
                queueingList.setService_time(re.getTimestamp("service_time"));
                queueingList.setService_emp_code(re.getString("service_emp_code"));
                queueingList.setService_counter_code(re.getString("service_counter_code"));
                queueingList.setFinish_time(re.getTimestamp("finish_time"));
                queueingList.setStatus_code(re.getString("status_code"));
                queueingList.setDept_code(re.getString("dept_code"));
                queueingList.setCross_target_id(re.getInt("cross_target_id"));
                queueingList.setSchedule_code(re.getString("schedule_code"));
                queueingList.setAppointed_date(re.getTimestamp("appointed_date"));
                queueingList.setSource(re.getString("source"));
                queueingList.setMatters(re.getString("matters"));
                //设置为未下发的状态
                queueingList.setState(0);
                queueingListList.add(queueingList);

                queueingListRepository.saveAll(queueingListList).size();
            }
        }finally {
            re.close();
            conn.close();
            statement.close();
        }
    }

    /**
     * 业务类型信息更新上传到税务系统中
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBiz() throws SQLException,ClassNotFoundException {
        String sql = "SELECT ID,NAME,ENABLED FROM [queue].[group]";
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
                map.put("domain.ywId","nsfw.savebiz");
                Map<String,String> paramMap = new HashMap<String,String>();
                paramMap.put("bid","nsfw.savebiz");
                paramMap.put("ver","1.0");
                paramMap.put("bizid",Integer.toString(re.getInt("id")));
                paramMap.put("govhallid","133011002");
                paramMap.put("Bizname",re.getString("name"));
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

    //同步业务表
    @Transactional(rollbackFor = Exception.class)
    public void asynGroup() throws  Exception{
        List<StationGroup> stationGroupList = new ArrayList<>();
        String sql = "SELECT ID,CODE,ENABLED FROM [queue].[group]";
        ResultSet re = null;
        Connection conn = null ;
        Statement statement = null;
        try {
            Class.forName(className);
            conn = DriverManager.getConnection(url, user, password);
            statement = conn.createStatement();
            re = statement.executeQuery(sql);
            while (re.next()) {
                StationGroup stationGroup = new StationGroup();
                stationGroup.setCode(re.getString("code"));
                stationGroup.setId(re.getInt("id"));
                stationGroup.setEnabled(re.getInt("enabled"));
                stationGroupList.add(stationGroup);
            }
            stationGroupRepository.saveAll(stationGroupList);
        }finally {
            re.close();
            conn.close();
            statement.close();
        }
    }


    /**时间条件同步评价结果源数据表*/
    @Transactional(rollbackFor = Exception.class)
    public void getEvalResult(String startTime, String endTime) throws Exception {
        startTime = "'"+startTime+"'";
        endTime = "'"+endTime+"'";

        String sql = "SELECT * FROM eval.evaluate_result WHERE creation_time BETWEEN "+ startTime +
                " and " +  endTime;

        ResultSet re = null;
        Connection conn = null;
        Statement statement = null;
        try {
            Class.forName(className);

            conn = DriverManager.getConnection(url,user,password);
            statement = conn.createStatement();
            re = statement.executeQuery(sql);

            List<EvaluateResult> evaluateResultList = Lists.newArrayList();

        while (re.next()){
            EvaluateResult evaluateResult= new EvaluateResult();
            evaluateResult.setId(re.getInt("id"));
            evaluateResult.setQueueing_list_id(re.getInt("queueing_list_id"));
            evaluateResult.setEmployee_code(re.getString("employee_code"));
            evaluateResult.setCounter_code(re.getString("counter_code"));
            evaluateResult.setEvaluate_code(re.getString("evaluate_code"));
            evaluateResult.setEvaluate_value(re.getInt("evaluate_value"));
            evaluateResult.setAppraiser_mobile(re.getString("appraiser_mobile"));
            evaluateResult.setEval_description(re.getString("eval_description"));
            evaluateResult.setCreation_time(re.getTimestamp("creation_time"));
            evaluateResult.setDept_code(re.getString("dept_code"));
            //设置源数据为未下发的状态
            evaluateResult.setState(0);
            evaluateResultList.add(evaluateResult);
        }
            evaluateResultRepository.saveAll(evaluateResultList);
        }finally {
            re.close();
            conn.close();
            statement.close();
        }
    }


    /**将同步来的取号源数据插入到现有的stationPeople表中*/
    @Transactional(rollbackFor = Exception.class)
    public void sysStationPeople(){
        /**查询到未下发的取号叫号记录*/
        List<QueueingList> queueingListList = queueingListRepository.findAllByState(0);

        if(queueingListList.size()==0||queueingListList==null){
            logger.error("没有未下发的取号叫号源数据");
            return;
        }
        List<StationPeople> stationPeopleList = Lists.newArrayList();
        for(QueueingList queueingList:queueingListList){

            //根据主键resourceId先去中间表查询，如果已经存在就跳出
            StationPeople stationPeople = stationPeopleMapper.selectByResourceId(queueingList.getId());
            if(stationPeople!=null){
                queueingList.setState(1);
                continue;
            }

            //如果手机号码为空直接跳出循环
            if(queueingList.getMobile()==null||queueingList.getMobile()==""){
                continue;
            }
            //如果身份证号码为空也跳出循环
            if(queueingList.getCode()==null||queueingList.getCode()==""){
                continue;
            }

            stationPeople = new StationPeople();
            //设置手机号
            stationPeople.setMobile(queueingList.getMobile());
            //设置stationPeople身份证号码
            stationPeople.setCode(queueingList.getCode());
            //设置stationPeople主键
            stationPeople.setResourceId(queueingList.getId());

            //设置stationPeople创建时间
            stationPeople.setCreationTime(queueingList.getCreation_time());
            //设置取号时间
            stationPeople.setTakeNumberTime(queueingList.getCreation_time());

            //如果服务的时间为空，我们就认为设置过号时间为取号时间
            if(queueingList.getService_time()==null){
                stationPeople.setPassTime(queueingList.getCreation_time());
                //设置开始工作时间
                stationPeople.setStartWorkTime(null);
                //设置完成时间
                stationPeople.setCompleteTime(null);
            }
            //设置开始工作时间
            stationPeople.setStartWorkTime(queueingList.getService_time());
            //设置完成时间
            stationPeople.setCompleteTime(queueingList.getFinish_time());
            //设置stationPeople的部门编号
            stationPeople.setDeptCode(queueingList.getDept_code());

            OrganizationOutput organizationOutput = organizationMapper.selectByOrgaNo(queueingList.getDept_code());
            if(organizationOutput == null){
                stationPeople.setDeptName(null);
            }else {
                //设置部门名称
                stationPeople.setDeptName(organizationOutput.getName());
            }
            //设置完整的取号数
            stationPeople.setFullSequence(queueingList.getFull_sequence());
            //设置窗口号
            stationPeople.setGroupCode(queueingList.getGroup_code());
            //设置办理事项
            stationPeople.setMatters(queueingList.getMatters());

            //设置群众名称
            stationPeople.setName(queueingList.getName());
            stationPeople.setPriorityCode(queueingList.getPriority_code());
            stationPeople.setScheduleCode(queueingList.getSchedule_code());
            stationPeople.setSortIndex(queueingList.getSort_index());
            stationPeople.setSource(queueingList.getSource());

            stationPeople.setStatusCode(queueingList.getStatus_code());

            //将源数据中的取号记录设置为已经下发的1的状态
            queueingList.setState(1);
            stationPeopleList.add(stationPeople);
        }
        Integer size = stationPeopleRepository.saveAll(stationPeopleList).size();
        if(size == 0){
            logger.error("保存到stationPeople表数据保存失败");
            return;
        }
        Integer result =queueingListRepository.saveAll(queueingListList).size();
        if(result == 0){
            logger.error("改变queueLing的源数据的状态保存失败");
            return;
        }
        logger.info("保存到stationPeople表"+size+"条数据保存成功");
    }

    /**将同步的评价结果源数据保存到评价结果表中*/
    @Transactional(rollbackFor = Exception.class)
    public void sysEvalResult(){
        //查询未下发的结果源数据
        List<EvaluateResult> evaluateResultList = evaluateResultRepository.findAllByState(0);
        if(evaluateResultList.size() == 0 || evaluateResultList==null){
            logger.error("没有评价结果");
            return;
        }

        //设置评价结果中间表数据集合
        List<EvaluationResults> evaluationResultsList = Lists.newArrayList();

        for(EvaluateResult evaluateResult : evaluateResultList){
            //如果评价结果中间表数据已经存在
            EvaluationResults evaluationResults = evaluationResultsRepository.findByResourceId(evaluateResult.getId());
            //如果已经存在就跳出并且设置为已经下发完成
            if(evaluationResults!=null){
                evaluateResult.setState(1);
                continue;
            }
            //如果不存在就new一个新的评价结果中间表对象
            evaluationResults = new EvaluationResults();
            //设置评价结果中间表主键
            evaluationResults.setResourceId(evaluateResult.getId());
            //设置评价的手机号码
            evaluationResults.setAppraiserMobile(evaluateResult.getAppraiser_mobile());
            //设置评价窗口
            evaluationResults.setCounterCode(evaluateResult.getCounter_code());
            //设置评价时间
            evaluationResults.setCreationTime(evaluateResult.getCreation_time());
            //设置评价的部门编号
            evaluationResults.setDeptCode(evaluateResult.getDept_code());
            //设置评价的员工工号
            evaluationResults.setEmployeeCode(evaluateResult.getEmployee_code());
            //设置评价结果的code
            evaluationResults.setEvaluateCode(evaluateResult.getEvaluate_code());
            //设置关联取号中间表的主键
            evaluationResults.setQueueingListId(evaluateResult.getQueueing_list_id());

            //设置成已经需要下发状态
            evaluateResult.setState(1);
            evaluationResultsList.add(evaluationResults);
        }

        Integer evaSize =  evaluationResultsRepository.saveAll(evaluationResultsList).size();
        if(evaSize == 0){
            logger.error("保存到评价结果表中的数据保存失败");
            return;
        }
        Integer evlouResult = evaluateResultRepository.saveAll(evaluateResultList).size();
        if(evlouResult == 0){
            logger.error("修改评价结果"+evlouResult+"条源数据表保存失败");
            return;
        }
        return;
    }

    /**定时下发到反馈信息*/
    @Transactional(rollbackFor = Exception.class)
    public synchronized void synFeedBack(){
        //查询需要下发的结果源数据
        List<EvaluateResult> evaluateResultList = evaluateResultRepository.findAllByState(1);

        List<FeedbackInfo> feedbackInfoList = Lists.newArrayList();
        if(evaluateResultList==null||evaluateResultList.size()==0){
            logger.error("暂时没有需要同步到反馈信息的评价结果");
            return;
        }
        for(EvaluateResult evaluateResult : evaluateResultList){
            //状态设置为下发完成
            evaluateResult.setState(2);
            //先根据评价结果的Queueing_list_id去反馈信息表中查询数据
            List<FeedbackInfo> feedbackInfoListByRe = feedbackInfoRepository.findAllByResourceId(evaluateResult.getQueueing_list_id());
            //如果反馈信息存在，就跳出本次循环
            if(feedbackInfoListByRe.size()>0){
                continue;
            }
            //根据评价结果的queListId查询不到取号记录
            StationPeopleOutput stationPeople = stationPeopleMapper.selectByResourceId(evaluateResult.getQueueing_list_id());
            if (stationPeople == null) {
                logger.error("没有查询到对应的取号叫号的源记录信息");
                continue;
            }

            if(!stationPeople.getDeptCode().equals(evaluateResult.getDept_code())){
                continue;
            }
            //根据员工工号查询出员工
            EmployeesOutput employeesOutput = employeesMapper.selectByEmpNo(evaluateResult.getEmployee_code());
            //根据组织编号查询出组织对象
            OrganizationOutput organizationOutput = organizationMapper.selectByOrgaNo(evaluateResult.getDept_code());

            FeedbackInfo feedbackInfo = new FeedbackInfo();
            feedbackInfo.setAmputated(0);
            //设置未评价
            feedbackInfo.setAppraiseState(AppraiseStateEnum.UN_APPRAISE.getCode());
            feedbackInfo.setCreatedDateTime(evaluateResult.getCreation_time());
            feedbackInfo.setCreatedUserId(0);
            feedbackInfo.setCreatedUserName("定时同步遗漏的反馈信息");
            feedbackInfo.setFeedbackTime(evaluateResult.getCreation_time());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(evaluateResult.getCreation_time());
            calendar.add(Calendar.HOUR, 2);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //获取最后评价时间
            feedbackInfo.setDeadline(sdf.format(calendar.getTime()));
            if(employeesOutput==null){
                feedbackInfo.setEmployeesName(" ");
                feedbackInfo.setEmployeesNo(" ");
            }else {
                //设置员工姓名
                feedbackInfo.setEmployeesName(employeesOutput.getName());
                //设置员工工号
                feedbackInfo.setEmployeesNo(employeesOutput.getEmployeeNo());
            };
            feedbackInfo.setLastUpdateDateTime(calendar.getTime());
            feedbackInfo.setLastUpdateUserId(0);
            feedbackInfo.setLastUpdateUserName("定时同步遗漏的反馈信息");
            //设置办理事项
            feedbackInfo.setMatters(stationPeople.getMatters());

            if(organizationOutput == null){
                feedbackInfo.setOrganizationId(null);
                feedbackInfo.setOrganizationName(" ");
            }else {
                feedbackInfo.setOrganizationId(organizationOutput.getId());
                feedbackInfo.setOrganizationName(organizationOutput.getName());
            }

            if(stationPeople.getName() == null||stationPeople.getName() == ""){
                feedbackInfo.setPersonName(" ");
            }else {
                feedbackInfo.setPersonName(stationPeople.getName());
            }

            feedbackInfo.setPhone(stationPeople.getMobile());
            feedbackInfo.setWindowNo(evaluateResult.getCounter_code());
            //设置与取号叫号相关联的resourceId
            feedbackInfo.setResourceId(evaluateResult.getQueueing_list_id());
            feedbackInfoList.add(feedbackInfo);

            PageData pageData = new PageData();
            //设置窗口评价类型
            pageData.put("type",SmsTemplateEnum.CKPJ_TYPE.getCode());
            //窗口评价的类型的短信模板
            ResponseResult smsTempResult = HttpRequestUtil.sendMyGetRequest("http://10.32.250.88:8775/smstemplate/findByType?type="+ SmsTemplateEnum.CKPJ_TYPE.getCode(),pageData);
            if(smsTempResult.getCode() != 200){
                logger.error("没有找到短信模板");
                continue;
            }
            //获取模板内容
            String template = (String) smsTempResult.getData();
            var TepMap = GetMessageTemplate.getKey(template);
            //窗口号
            TepMap.put("ckh",feedbackInfo.getWindowNo());
            //办理事项内容
            TepMap.put("blywmc",feedbackInfo.getMatters());
            //获取短信内容
            String rsContent = GetMessageTemplate.getContent(template, TepMap);

            List<String> reList = new ArrayList<>();
            reList.add(feedbackInfo.getPhone()+"/"+feedbackInfo.getPersonName());

            PageData pageData1 = new PageData();
            pageData1.put("sendList",reList);
            pageData1.put("description",rsContent);
            //立刻发送
            pageData1.put("isTiming",0);
            //创建时间为评价时间
            pageData1.put("createdDateTime",evaluateResult.getCreation_time());
            //窗口评价类型
            pageData1.put("type", MessageTypeEnum.WINDOW_APPRAISE.getCode());
            //发送短信
            ResponseResult responseResult = HttpRequestUtil.sendPostRequest( "http://10.32.250.88:8775/smsSend/form", pageData1);
            if(responseResult.getCode() != 200){
                logger.info("发送给群众的评价短信发送失败");
                continue;
            }
            logger.info("发送给群众的评价短信发送成功");

            this.setUnStatisToRedis(feedbackInfo);
            logger.info("将反馈信息成功推送到redis");
        }
        Integer evaSize = evaluateResultRepository.saveAll(evaluateResultList).size();
        if(evaSize != 0 ){
            logger.info("评价源数据表的下发状态修改完成");
        }
        Integer size = feedbackInfoRepository.saveAll(feedbackInfoList).size();
        if (size == 0) {
            logger.error("没有添加到反馈信息的评价结果");
            return;
        }

        logger.info(size+"条反馈信息添加成功");
        return;
    }


    public void setFeedbackInfoToRedis(FeedbackInfo feedbackInfo){
        //设置30分钟后过期时间为30分钟
        redisComponent.set("EVL_FEED_BACK:" + feedbackInfo.getId(), "", Long.valueOf(60*30));
    }

    public void setUnStatisToRedis(FeedbackInfo feedbackInfo){
        //设置为1秒后发送
        redisComponent.set("EVL_FEED_BACK:" + feedbackInfo.getId(), "", 1L);
    }
}
