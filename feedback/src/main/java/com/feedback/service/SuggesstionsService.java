package com.feedback.service;

import com.common.Enum.MessageTypeEnum;
import com.common.model.PageData;
import com.common.request.GetConfig;
import com.common.response.ResponseResult;
import com.common.utils.ExportExcel;
import com.feedback.config.RedisComponent;
import com.feedback.core.base.BaseMapper;
import com.feedback.core.base.BaseService;
import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.core.util.AppConsts;
import com.feedback.domain.input.SuggesstionResult;
import com.feedback.domain.output.*;
import com.feedback.mapper.jpa.FeedbackInfoRepository;
import com.feedback.mapper.jpa.SuggesstionsRepository;
import com.feedback.mapper.mybatis.*;
import com.feedback.model.FeedbackInfo;
import com.feedback.model.Organization;
import com.feedback.model.Suggestions;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.feedback.core.base.BaseController.SYS_EORRO;

@Service
public class SuggesstionsService extends BaseService<SuggesstionsOutput, Suggestions,Integer> {
    private Logger log = LoggerFactory.getLogger(SuggesstionsService.class);
    @Autowired
    private SuggestionsMapper suggestionsMapper;
    @Autowired
    private SuggesstionsRepository suggesstionsRepository;
    @Autowired
    private FeedbackInfoRepository feedbackInfoRepository;
    @Autowired
    private EmployeesMapper employeesMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private SMSReceiveMapper smsReceiveMapper;
    @Autowired
    private WindowMapper windowMapper;
    @Autowired
    private FeedbackInfoMapper feedbackInfoMapper;
    @Autowired
    private SMSSendMapper smsSendMapper;

    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private TimeService timeService;

    @Value("${spring.message.deadline}")
    private String deadline;

    @Override
    public BaseMapper<Suggestions, Integer> getMapper() {
        return suggesstionsRepository;
    }

    @Override
    public MybatisBaseMapper<SuggesstionsOutput> getMybatisBaseMapper() {
        return suggestionsMapper;
    }

    @Autowired
    private RedisComponent redisComponent;

    public Integer selectByOrgName(String orgName){
        Organization organization = organizationMapper.selectByName(orgName);
        if(organization == null){
            return -1;
        }
        //根据组织编号从新获取组织对象
        organization = organizationMapper.selectByOrgaNo(organization.getOrganizationNo());
        return organization.getId();
    }

    public Integer selectRelayId(Integer orgaId){
        OrganizationOutput organizationOutput = organizationMapper.selectByPrimaryKey(orgaId);
        if(organizationOutput.getDepartmentalManager()==null){
            return null;
        }
        return organizationOutput.getDepartmentalManager();
    }

    public Integer selectByWindowName(Integer orgId,String windowName){
        WindowOutput window = windowMapper.selectByOrAndWindowNo(orgId,windowName);
        if(window == null){
            return -1;
        }
        return window.getId();
    }

    public EmployeesOutput selectByEmpNo(String empNo){
        EmployeesOutput employeesOutput = employeesMapper.selectByEmpNo(empNo);
        if(employeesOutput == null){
            return null;
        }
        return employeesOutput;
    }

    public FeedbackInfo selectByFeedbackId(Integer feedbackId){
        return feedbackInfoMapper.selectByPrimaryKey(feedbackId);
    }


    @Override
    public SuggesstionsOutput selectById(Integer id){
        SuggesstionsOutput suggesstionsOutput = suggestionsMapper.selectByPrimaryKey(id);
        return suggesstionsOutput;
    }


    public SuggesstionsOutput getResponseById(Integer id) {
        SuggesstionsOutput suggesstionsOutput = suggestionsMapper.selectByPrimaryKey(id);
        return suggesstionsOutput;
    }

    public List<EmployeesOutput> hasObject(Integer id,Integer OrgaId) {

        List<Organization> organizationList = organizationMapper.selectOrganizationMobile(OrgaId);
        if(organizationList ==null ||organizationList.size()==0 ){
            return null;
        }

        List<EmployeesOutput> employeesOutputList = Lists.newArrayList();

        for(Organization organization:organizationList){
            EmployeesOutput employeesOutput = employeesMapper.selectByPhone(organization.getPhoneNumber());
            if(employeesOutput == null){
                return null;
            }
            employeesOutputList.add(employeesOutput);
        }
        return employeesOutputList;
    }

    public Suggestions changeState(Integer id, Date outOfDate) {
//    public Suggestions changeState(Integer id) {
        Suggestions byId = suggesstionsRepository.getById(id);
        if(byId == null){
            return null;
        }
        byId.setPublish(AppConsts.Publish_Yes);
        byId.setOutOfDate(AppConsts.OutOfDate_No);
        byId.setOutDateTime(outOfDate);
        byId.setPublishDateTime(new Date());//发布时间
        byId.setLastUpdateDateTime(new Date());
        byId.setLastUpdateUserId(getUsers().getId());
        byId.setLastUpdateUserName(getUsers().getUsername());
        return suggesstionsRepository.save(byId);

    }

    public FeedbackInfoOutput selectBySuggessId(Integer feedbackId){
        FeedbackInfoOutput feedbackInfoOutput = feedbackInfoMapper.selectByPrimaryKey(feedbackId);
        if(feedbackInfoOutput == null){
            return null;
        }
        return feedbackInfoOutput;
    }

    /**设置到反馈信息到redis**/
    public void setValueToRedis(FeedbackInfo feedbackInfo) {
//        //过期设置成二小时
        redisComponent.sentinelSet("FEED_INFO:" + feedbackInfo.getId(), "", Long.valueOf(60*60*Integer.valueOf(deadline)));
    }

    /**将投诉建议推送到redis*/
    public void putMessageToRedis(Integer id, Date outOfDate) {
        Date date = new Date();
        long l = outOfDate.getTime() - date.getTime();
        if (l < 0) {
            redisComponent.sentinelSet("HZFT_FEEDBACK:" +id, "", 5L);
        }else{
            redisComponent.sentinelSet("HZFT_FEEDBACK:" +id, "",  l / 1000);
        }
    }

    /**推送已经逾期的记录到投诉建议表中*/
    public void judgeOutOfDateState(int id) {
        Suggestions byId = suggesstionsRepository.getById(id);
        //当是未处理
        if(byId.getDealState().equals(AppConsts.DealState_No)){
                //设置成已经逾期
                byId.setOutOfDate(AppConsts.OutOfDate_Yes);
                //设置逾期日期为此刻
                byId.setOutDateTime(new Date());
                Integer suggId = suggesstionsRepository.save(byId).getId();
                if(suggId<0) {
                    return;
                }
                log.info("已经将一条投诉记录推送到逾期未处理中");
                return;
            }
        }

    public Suggestions transToSuggesstion(Integer id, SuggesstionResult suggesstionResult) {
        Suggestions byId = suggesstionsRepository.getById(id);
        byId.setDealResult(suggesstionResult.getDealResult());
        byId.setDealTime(suggesstionResult.getDealTime());
        byId.setAttachmentList(suggesstionResult.getAttachmentList());
        return byId;
    }

    @Transactional
    public ResponseResult setInvalid(String idList) throws Exception {
        var strs = idList.split(",");
        for(var str : strs){
            var id = Integer.parseInt(str);
            Suggestions t =  getMapper().getById(id);
            if(t==null){
                continue;
            }
            t.setPublish(AppConsts.Publish_Invalid);
            int update = this.update(id, t);
            if(update<=0){
                return ResponseResult.error(SYS_EORRO);
            }
        }
        return ResponseResult.success();
    }

    public List<SuggesstionsOutput> findAllOutOfDatePageList(PageData pageData) {
        pageData.put("outOfDate",AppConsts.DealState_Yes);
        List<SuggesstionsOutput> pageList = suggestionsMapper.selectAllOutOfDatePageList(pageData);
        for (SuggesstionsOutput suggesstionsOutput:pageList
             ) {
            Date date = new Date();
            long l = date.getTime() - suggesstionsOutput.getOutDateTime().getTime();
            suggesstionsOutput.setOutDays(String.valueOf(l/1000/60/60/24));
        }
        return pageList;
    }

    public void setOutState(){
        PageData pageData = new PageData();
        Page<SuggesstionsOutput> pageList = suggestionsMapper.selectPage(pageData);
    }


    public void selectPage(PageData pageData) throws Exception {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Page<SuggesstionsOutput> pageList = suggestionsMapper.selectPage(pageData);;

        List<Suggestions> suggestionList = new ArrayList<>();
            for (SuggesstionsOutput suggesstionsOutput:pageList) {

                if(suggesstionsOutput.getPublish() == 1 && StringUtils.isNotBlank(String.valueOf(suggesstionsOutput.getPublishDateTime()))
                && suggesstionsOutput.getDealState() == 1 && StringUtils.isNotBlank(String.valueOf(suggesstionsOutput.getDealTime()))){
                    long long1 = suggesstionsOutput.getDealTime().getTime()-suggesstionsOutput.getPublishDateTime().getTime();
                    suggesstionsOutput.setOutTimes(String.valueOf(long1/1000/60/60/24));
//                    pageData.put("key","suggestionDeal");
//                    var map = GetConfig.getByKey(loadBalancerClient,pageData);
//                    Integer sd = Integer.valueOf(map.get("suggestionDeal"));
                    if(suggesstionsOutput.getDealTime().getTime() > suggesstionsOutput.getOutDateTime().getTime()){
                        suggesstionsOutput.setOutOfDate(AppConsts.OutOfDate_Yes);
                    }

                    Suggestions suggestions = new Suggestions();
                    BeanUtils.copyProperties(suggesstionsOutput,suggestions);
                    suggestionList.add(suggestions);
                }
        }

        suggesstionsRepository.saveAll(suggestionList);
    }

    //获取分页的投诉建议
    public ResponseResult findOutOfDatePageList (PageData pageData) {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        //已经逾期
        pageData.put("outOfDate",AppConsts.OutOfDate_Yes);

        Page<SuggesstionsOutput> pageList = suggestionsMapper.selectOutOfDatePageList(pageData);
        for (SuggesstionsOutput suggesstionsOutput:pageList) {
            Date date = new Date();
            long l = date.getTime() - suggesstionsOutput.getOutDateTime().getTime();
            suggesstionsOutput.setOutDays(String.valueOf(l/1000/60/60/24));
        }
        return ResponseResult.success(new PageInfo<>(pageList));
    }

    public SuggesstionsOutput getOutSuggesstionDetail(Integer id) {
        SuggesstionsOutput suggesstionsOutput = suggestionsMapper.getOutSuggesstionDetail(id);
        if(suggesstionsOutput == null){
            return null;
        }
        Date date = new Date();
        long l = date.getTime() - suggesstionsOutput.getOutDateTime().getTime();
        suggesstionsOutput.setOutDays(String.valueOf(l/1000/60/60/24));
        return suggesstionsOutput;
    }



    public String outOfDatePageListExport(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "投诉建议逾期未处理表";
        String excelName = "投诉建议逾期未处理表";
        String[] rowsName = new String[]{"序号","手机号码","用户姓名","投诉部门","反馈时间","指定回复人","逾期天数(天)"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        pageData.put("userId",getUsers().getId().toString());
        if(getUsers().getAdministratorLevel()!=9){
            if(getUsers().getUserType()==0){
                pageData.put("employeeNo",getUsers().getUsername());
            }else {
                pageData.put("orgId",getUsers().getOrganizationId().toString());
            }
        }
        List<SuggesstionsOutput> list=this.findAllOutOfDatePageList(pageData);
        if(list.size()>0){
            int i=1;
            for(SuggesstionsOutput output:list){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=output.getSuggestionPhoneNumber();
                objs[2]=output.getSuggestionName();
                objs[3]=output.getOrganizationName();
                objs[4]=output.getCreatedDateTime();
                objs[5]=output.getUserName();
                objs[6]=output.getOutDays();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    /**第七步查询出群众回馈的不满意原因的短信*/
    public void selectUnStatisReason(){
        log.info("开始接受群众不满意原因的短信");
        //1.查询出未回复未发布的投诉建议,如果没有直接return
        List<Suggestions> suggestionsList = suggesstionsRepository.
                    findAllByReplyTypeAndAmputatedAndDateResource(AppConsts.Reply_Un,0,AppConsts.Resource_Message);
        if(suggestionsList.size() == 0){
            return;
        }
        for(Suggestions suggestions:suggestionsList){

            //根据短信类型以及手机号码去发件箱表中查询该条发送记录
            SMSSendOutput smsSendOutput = smsSendMapper.selectByTypeAndPhone(MessageTypeEnum.WINDOW_VISIT.getCode(),suggestions.getSuggestionPhoneNumber());
            if(smsSendOutput == null){
                return;
            }
            SMSReceiveOutPut smsReceiveOutPut = smsReceiveMapper.selectBySendId(smsSendOutput.getId());
            if(smsReceiveOutPut== null){
                return;
            }
            //设置已经处理不满意详情
            suggestions.setContent(smsReceiveOutPut.getDescription());
            //添加投诉建议内容状态为已经添加
            suggestions.setReplyType(AppConsts.Reply_is);
            int result = suggesstionsRepository.save(suggestions).getId();
            if(result<0){
                return;
            }

            //根据投诉建议id查询反馈信息
            FeedbackInfoOutput feedbackInfoOutput = feedbackInfoMapper.selectByPrimaryKey(suggestions.getFeedbackId());
            if(feedbackInfoOutput == null){
                log.error("根据该条投诉建议没有找到对应的反馈详情");
                return;
            }

            /**第八步第一次不满意详情，第九步在suggestionController中*/
            feedbackInfoOutput.setOneDetail(smsReceiveOutPut.getDescription());
            FeedbackInfo feedbackInfo = new FeedbackInfo();
            BeanUtils.copyProperties(feedbackInfoOutput,feedbackInfo);
            int feedbackId = feedbackInfoRepository.save(feedbackInfo).getId();
            if(feedbackId<0){
                log.error("没有成功添加第一次不满意详情到反馈信息表中");
                return;
            }
            log.info("成功添加群众不满意的原因到投诉建议表中");
            log.info("成功添加群众不满意的原因到反馈信息表中");
            return;
        }
    }


    /**第14步查询出群众回馈的不满意原因的短信*/
    public void selectUnStatisReasonTwo(){
        log.info("开始接受群众再次评价不满意原因的短信");

        List<Suggestions> suggestionsList = suggesstionsRepository.
                    findAllByReplyTypeAndAmputatedAndDateResource(AppConsts.Reply_Un,0,AppConsts.Resource_Second);
        if(suggestionsList.size() == 0){
            return;
        }
        for(Suggestions suggestions:suggestionsList){

            //根据短信类型以及手机号码去发件箱表中查询该条发送记录
            SMSSendOutput smsSendOutput = smsSendMapper.selectByTypeAndPhone(MessageTypeEnum.WINDOW_RE_VISIT.getCode(),suggestions.getSuggestionPhoneNumber());
            if(smsSendOutput == null){
                return;
            }
            SMSReceiveOutPut smsReceiveOutPut = smsReceiveMapper.selectBySendId(smsSendOutput.getId());
            if(smsReceiveOutPut== null){
                return;
            }
            //根据投诉建议id去投诉建议内容表里去查询投诉内容
            suggestions.setContent(smsReceiveOutPut.getDescription());
            //设置成已经处理不满意详情
            suggestions.setReplyType(AppConsts.Reply_is);
            int result = suggesstionsRepository.save(suggestions).getId();
            if(result<0){
                log.error("没有成功添加第二次不满意详情到投诉建议表中");
                return;
            }
            //根据投诉建议id查询反馈信息
            FeedbackInfoOutput feedbackInfoOutput = feedbackInfoMapper.selectByPrimaryKey(suggestions.getFeedbackId());
            /**第15步处理第二次不满意详情*/
            feedbackInfoOutput.setTwoDetail(smsReceiveOutPut.getDescription());
            FeedbackInfo feedbackInfo = new FeedbackInfo();
            BeanUtils.copyProperties(feedbackInfoOutput,feedbackInfo);
            int feedbackId = feedbackInfoRepository.save(feedbackInfo).getId();
            if(feedbackId<0){
                log.error("没有成功添加第二次不满意详情到反馈信息表中");
                return;
            }
            log.info("成功添加二次群众不满意的原因到投诉建议表中");
            log.info("成功添加二次群众不满意的原因到反馈信息表中");
            return;
        }
    }


    /**使用dbLink每2小时一次将199服务器上投诉建议同步到83服务器上*/
    public void sync(){
        try {
            List<SuggesstionsOutput> suggesstionsOutputList = suggestionsMapper.selectSyncData();
            if(suggesstionsOutputList == null||suggesstionsOutputList.size()==0){
                log.info("当天没有投诉信息");
                return;
            }
            List<Suggestions> suggestionsList = Lists.newArrayList();
            for(SuggesstionsOutput suggesstionsOutput:suggesstionsOutputList){
                Suggestions suggestions = new Suggestions();
                BeanUtils.copyProperties(suggesstionsOutput,suggestions);
                suggestionsList.add(suggestions);
            }
            Integer size = suggesstionsRepository.saveAll(suggestionsList).size();
            log.info("199服务器上一共同步了:{}",size+"条投诉建议");
            return;

        }catch (Exception e){
            log.error("dbLink同步异常:{}",e.getMessage());
        }
    }
}
