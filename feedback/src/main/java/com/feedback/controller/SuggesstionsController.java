package com.feedback.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.common.Enum.AttachmentEnum;
import com.common.Enum.MessageTypeEnum;
import com.common.Enum.SmsTemplateEnum;
import com.common.model.PageData;
import com.common.request.GetConfig;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import com.common.utils.GetMessageTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.feedback.core.base.BaseController;
import com.feedback.core.base.BaseService;
import com.feedback.core.util.AppConsts;
import com.feedback.domain.input.AppraisalPlanItemAttachmentInput;
import com.feedback.domain.input.SuggesstionResult;
import com.feedback.domain.output.EmployeesOutput;
import com.feedback.domain.output.FeedbackInfoOutput;
import com.feedback.domain.output.SuggesstionsOutput;
import com.feedback.model.*;
import com.feedback.service.SuggesstionsService;
import com.feedback.service.TimeService;
import com.google.common.collect.Maps;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/suggesstions")
@Api(value = "投诉建议controller",description = "投诉建议操作",tags = {"投诉建议操作接口"})
public class SuggesstionsController extends BaseController<SuggesstionsOutput, Suggestions,Integer> {

    @Autowired
    private SuggesstionsService suggesstionsService;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private TimeService timeService;


    @Override
    public BaseService<SuggesstionsOutput,Suggestions, Integer> getService() {
        return suggesstionsService;
    }


    @Override
    @ApiOperation("新增或修改投诉建议")
    @PostMapping(value = "form")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="投诉建议",value="传入json格式",required=true) Suggestions suggestions) throws Exception {

        Integer suggestionId;
        if(id==null){
            suggestions.setAmputated(0);
            //未处理
            suggestions.setDealState(AppConsts.DealState_No);
            //未发布
            suggestions.setPublish(AppConsts.Publish_No);
            //未回复状态
            suggestions.setReplyType(AppConsts.Reply_Un);
            //未逾期
            suggestions.setOutOfDate(AppConsts.OutOfDate_No);
            //获得投诉部门的部门管理员
            //根据投诉的组织名称获取组织的id
            Integer orgaId = suggesstionsService.selectByOrgName(suggestions.getOrganizationName().trim());
            if(orgaId < 0){
                return ResponseResult.error("该部门没有找到");
            }
            suggestions.setOrganizationId(orgaId);
            //根据orgaId查询出回复人的id
            Integer replayId = suggesstionsService.selectRelayId(orgaId);
            if(replayId == null){
                suggestions.setReplyUserId(null);
            }
            suggestions.setReplyUserId(replayId);
            //如果上级组织name不为空
            if(StringUtils.isNotBlank(suggestions.getUpperOrganizationName())){
                Integer upOrgaId = suggesstionsService.selectByOrgName(suggestions.getUpperOrganizationName());
                if(upOrgaId<0){
                    return ResponseResult.error("该上级部门没有找到");
                }
                suggestions.setUpperOrganizationId(upOrgaId);
            }
            //根据投诉的窗口号查询出对应的窗口id
            if(StringUtils.isNotBlank(suggestions.getWindowName())){
                Integer windowId = suggesstionsService.selectByWindowName(orgaId,suggestions.getWindowName().trim());
                if(windowId < 0){
                    return ResponseResult.error("该窗口没有找到对应的窗口id");
                }
                suggestions.setWindowId(windowId);
            }
            //根据投诉工号查询出员工的姓名和id
            if(StringUtils.isNotBlank(suggestions.getEmployeeNo())){
                EmployeesOutput employeesOutput = suggesstionsService.selectByEmpNo(suggestions.getEmployeeNo().trim());
                if(employeesOutput == null){
                    return ResponseResult.error("该员工编号没有查到对应的员工");
                }
                suggestions.setEmployeeId(employeesOutput.getId());
                suggestions.setEmployeeName(employeesOutput.getName());
            }

            suggestionId = suggesstionsService.add(suggestions);
            if(suggestionId<=0){
                return ResponseResult.error(SYS_EORRO);
            }
            return ResponseResult.success();
        } else {
//            if(suggestions.getDateResource().equals(AppConsts.Resource_Wechat)
//                    ||suggestions.getDateResource().equals(AppConsts.Resource_Message)||
//                    suggestions.getDateResource().equals(AppConsts.Resource_Second)){
//                return ResponseResult.error("该来源方式不可编辑");
//            }
            suggestionId=suggesstionsService.update(id,suggestions);
            if(suggestionId<0){
                return ResponseResult.error(SYS_EORRO);
            }
        }
        List<Attachment> attachmentList=suggestions.getAttachmentList();
        List<Attachment> attachments=new ArrayList<>();
        if(!CollectionUtils.isEmpty(attachmentList)){
            for(Attachment attachment:attachmentList){
                attachment.setResourcesId(suggestionId);
                attachment.setResourcesType(AttachmentEnum.COMPLAINT_TYPE.getCode());
                attachment.setSourceFileName(attachment.getSuffix());
                attachments.add(attachment);
            }
            PageData pageData = new PageData();
            pageData.put("attachmentList",attachments);
            if(ServiceCall.postResult(loadBalancerClient,"attachment/attachmentApi","BIGDATA",pageData).getCode()!=200){
                return ResponseResult.error("上传附件失败");
            }
        }
        return ResponseResult.success();
    }

    @GetMapping(value = "get")
    @ApiOperation(value="根据id获取投诉详情")
    public ResponseResult getById(Integer id)  {
        if(id==null){
            ResponseResult.error(PARAM_EORRO);
        }
        SuggesstionsOutput suggesstionsOutput = suggesstionsService.selectById(id);
        PageData pageData = new PageData();
        pageData.put("id",id.toString());
        pageData.put("type",AttachmentEnum.COMPLAINT_TYPE.getCode().toString());
         ResponseResult responseResult = ServiceCall.getResult(loadBalancerClient, "attachment/getOpenAttachmentListByResouceIdAndType", "BIGDATA", pageData);
        if(responseResult.getCode()!=200){
            return ResponseResult.success(suggesstionsOutput);
        }
        suggesstionsOutput.setAttachmentList((List<Attachment>)responseResult.getData());
        return ResponseResult.success(suggesstionsOutput);
    }

    @Override
    @ApiOperation("删除投诉建议")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        return super.logicDelete(idList);
    }


    @GetMapping(value = "getDealResult")
    @ApiOperation(value="根据id获取反馈内容")
    public ResponseResult getDealResult(Integer id)  {
        if(id==null){
            ResponseResult.error(PARAM_EORRO);
        }
        SuggesstionsOutput suggesstionsOutput = suggesstionsService.selectById(id);
        if(AppConsts.DealState_No.equals(suggesstionsOutput.getDealState())){
            return ResponseResult.error("当前投诉建议未被处理");
        }
        PageData pageData = new PageData();
        pageData.put("type",AttachmentEnum.FEEDACK_TYPE.getCode().toString());
        pageData.put("id",id.toString());
        ResponseResult responseResult = ServiceCall.getResult(loadBalancerClient, "/attachment/getAttachmentListByResouceIdAndType", "bigdata", pageData);
        suggesstionsOutput.setAttachmentList((List<Attachment>)responseResult.getData());
        return ResponseResult.success(suggesstionsOutput);
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的投诉建议列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="dateResource",value="数据来源",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="departmentId",value="投诉部门",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="startTime",value="开始时间",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="endTime",value="结束时间",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="dealState",value="处理状态",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="name",value="群众姓名",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="phone",value="群众电话",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="jobNumber",value="工号",required=false,dataType="string", paramType = "query"),
    })
    public ResponseResult selectPageList(HttpServletRequest request) throws Exception {
        PageData pageData = new PageData(request);
        pageData.put("userId",suggesstionsService.getUsers().getId().toString());
        if(suggesstionsService.getUsers().getAdministratorLevel()!=9){
            if(suggesstionsService.getUsers().getUserType()==0){
                pageData.put("employeeNo",suggesstionsService.getUsers().getUsername());
            }else {
                pageData.put("orgId",suggesstionsService.getUsers().getOrganizationId().toString());
//                pageData.put("publish",AppConsts.Publish_Yes);
            }
        }
         suggesstionsService.selectPage(pageData);
        return super.selectPageList(pageData);
    }

    /**第九步发布同时通知部门管理员处理*/
    @ApiOperation("发布")
    @PostMapping(value = "publish")
    public ResponseResult publish(Integer id, String outOfDate) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(id==null){
            return ResponseResult.error(PARAM_EORRO);
        }
        //若未选择逾期时间则时间默认为当前时间加2个工作日
        Date outOfDateTime;
        if(StringUtils.isBlank(outOfDate)){
            PageData pageData = new PageData();
            pageData.put("key","suggestionDeal");
           // 获取逾期天数为2个工作日
            var ruleMap = GetConfig.getByKey(loadBalancerClient,pageData);
            if(ruleMap.size() == 0){
                return ResponseResult.error("获取逾期日期发生错误,请查看获取逾期日期的系统参数");
            }
            //获取到2个工作日后的此刻时间
            List<String> dateList = timeService.getWorkDays(new Date(),Integer.valueOf(ruleMap.get("suggestionDeal")));
            outOfDate = dateList.get(2);
            outOfDateTime = simpleDateFormat.parse(outOfDate);
        }else{

            outOfDateTime = simpleDateFormat.parse(outOfDate);
            if(outOfDateTime.before(new Date())){
                return ResponseResult.error("逾期时间必须大于当前时间");
            }
        }
        //发布填充规定回复时间、更改发布状态和逾期状态并将投诉短信发送给投诉部门的负责人
          //1、判断是否已经发布
        SuggesstionsOutput suggesstionsOutput = suggesstionsService.selectById(id);
        if(suggesstionsOutput==null){
            return ResponseResult.error(SYS_EORRO);
        }else if(suggesstionsOutput.getPublish().equals(AppConsts.Publish_Yes)){
            return ResponseResult.error("该条投诉建议已发布，不能重复发布。");
        }
        //2、判断投诉信息是否存在对应的回复人且回复人有电话号码
        List<EmployeesOutput> employeesOutputList = suggesstionsService.hasObject(id,suggesstionsOutput.getOrganizationId());
        if(employeesOutputList == null){
            return ResponseResult.error("该组织下没有部门管理员可以投诉");
        }

        //更改逾期时间和发布状态以及逾期状态并通知投诉对象
        for(EmployeesOutput employeesOutput:employeesOutputList){
            List<String> list = new ArrayList<>();
            list.add(employeesOutput.getPhoneNumber()+"/"+employeesOutput.getName());
            PageData pageData = new PageData();
            pageData.put("type", SmsTemplateEnum.TSJY_TYPE.getCode());
            ResponseResult result = ServiceCall.getResult(loadBalancerClient,"smstemplate/findByType","message",pageData);
            if(result.getCode() != 200){
                return ResponseResult.error("没有找到短信模板");
            }
            String template = (String) result.getData();
            var map = GetMessageTemplate.getKey(template);
            map.put("tsjy_bmgly", employeesOutput.getName());
            //获取模板内容
            PageData pageData1 = new PageData();
            String content = GetMessageTemplate.getContent(template, map);
            pageData1.put("sendList",list);
            pageData1.put("description",content);
            pageData1.put("isTiming",0);
            pageData1.put("type", MessageTypeEnum.INFO_DEPART_MANAGER.getCode());
            //发送短信给部门管理员
            ResponseResult responseResult = ServiceCall.postResult(loadBalancerClient, "smsSend/form", "message", pageData1);
            if(responseResult.getCode() != 200){
                return ResponseResult.error("发送给部门管理员的短信发送失败");
            }
        }
        suggesstionsService.putMessageToRedis(id,outOfDateTime);
        //设置成已经发布，设置发布时间
        Suggestions suggestions = suggesstionsService.changeState(id,outOfDateTime);
//        Suggestions suggestions = suggesstionsService.changeState(id);

        if(suggestions!=null){
            /**第十步添加发送给群众的两次评价短信，第11步在feedbackInfoService中*/
            PageData reSend = new PageData();
            reSend.put("type", SmsTemplateEnum.CKZCPJ_TYPE.getCode());
            ResponseResult resendResult = ServiceCall.getResult(loadBalancerClient,"smstemplate/findByType","message",reSend);
            if(resendResult.getCode() != 200){
                return ResponseResult.error("没有找到短信模板");
            }
            String resTemplate = (String) resendResult.getData();
            var resMap = GetMessageTemplate.getKey(resTemplate);
            //根据suggessId查询出该条投诉建议信息
            FeedbackInfoOutput feedbackInfoOutput = suggesstionsService.selectBySuggessId(suggesstionsOutput.getFeedbackId());
            if(feedbackInfoOutput == null){
                return ResponseResult.success("已经通知部门管理员处理,发布成功该条投诉建议");
            }
            resMap.put("ckh",feedbackInfoOutput.getWindowNo());
            resMap.put("blywmc",feedbackInfoOutput.getMatters());

            String rscontent = GetMessageTemplate.getContent(resTemplate, resMap);
            List<String> relist = new ArrayList<>();
            relist.add(feedbackInfoOutput.getPhone()+"/"+feedbackInfoOutput.getPersonName());
            reSend = new PageData();
            reSend.put("sendList",relist);
            reSend.put("description",rscontent);
            reSend.put("isTiming",0);
            reSend.put("type", MessageTypeEnum.WINDOW_RESEND.getCode());
            ResponseResult result1 = ServiceCall.postResult(loadBalancerClient, "smsSend/form", "message", reSend);
            if(result1.getCode() != 200){
                return ResponseResult.error("再次发送给群众的评价短信发送失败");
            }
            //设置到redis
            suggesstionsService.setValueToRedis(feedbackInfoOutput);
            return ResponseResult.success("已经通知部门管理员处理,发布成功");
        }

        return ResponseResult.error("未找到部门负责人或负责人手机号，发布失败");
    }

    @GetMapping(value = "response")
    @ApiOperation(value="根据id获取回复信息")
    public ResponseResult getResponseById(Integer id)  {
        if( id==null ){
            ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(suggesstionsService.getResponseById(id));
    }

    @ApiOperation("新增或修改处理结果")
    @RequestMapping(value = "formDealResult", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult formDealResult(Integer id, @Validated @RequestBody @ApiParam(name="投诉建议",value="传入json格式",required=true) SuggesstionResult suggesstionResult) throws Exception {
        Suggestions suggestions = suggesstionsService.transToSuggesstion(id,suggesstionResult);
        if(!AppConsts.Publish_Yes.equals(suggestions.getPublish())){
            return ResponseResult.error("当前投诉建议未被发布无法处理");
        }
        //设置成已经处理
        suggestions.setDealState(AppConsts.DealState_Yes);
        //设置处理时间
        suggestions.setDealTime(new Date());
        //设置成未逾期
        suggestions.setOutOfDate(AppConsts.OutOfDate_No);

        int update = suggesstionsService.update(id, suggestions);
        if(update<0){
            return ResponseResult.error(SYS_EORRO);
        }
        List<Attachment> attachmentList=suggestions.getAttachmentList();
        List<Attachment> attachments=new ArrayList<Attachment>();
        if(attachmentList!=null&&attachmentList.size()>0){
            for(Attachment attachment:attachmentList){
                attachment.setResourcesId(id);
                attachment.setResourcesType(AttachmentEnum.COMPLAINT_TYPE.getCode());
                attachment.setSourceFileName(attachment.getSuffix());
                attachments.add(attachment);
            }
            PageData pageData = new PageData();
            pageData.put("attachmentList",attachments);
            if(!ServiceCall.postResult(loadBalancerClient,"attachment/attachmentApi","bigdata",pageData).isSuccess()){
                return ResponseResult.error("上传附件失败");
            }
        }
        return ResponseResult.success();
    }

    @ApiOperation("提醒")
    @RequestMapping(value = "remind", method = RequestMethod.POST)
    public ResponseResult remind(Integer id) throws Exception {
        if(id==null||"".equals(id)){
            return ResponseResult.error(PARAM_EORRO);
        }
        //2、判断投诉信息是否存在对应的回复人且回复人有电话号码
        SuggesstionsOutput suggesstionsOutput = suggesstionsService.selectById(id);
        List<EmployeesOutput> employeesOutputList = suggesstionsService.hasObject(id,suggesstionsOutput.getOrganizationId());
        if(employeesOutputList==null) {
            return ResponseResult.error("该部门下没有部门管理员账号可以提醒");
        }
        for(EmployeesOutput employeesOutput:employeesOutputList) {
            List<String> list = new ArrayList<>();
            list.add(employeesOutput.getPhoneNumber()+"/"+employeesOutput.getName());
            //更改逾期时间和发布状态以及逾期状态并通知投诉对象
            PageData pageData = new PageData();
            pageData.put("type", SmsTemplateEnum.TSJY_TYPE.getCode());
            ResponseResult result = ServiceCall.getResult(loadBalancerClient,"smstemplate/findByType","message",pageData);
            if(result.getCode() != 200){
                return ResponseResult.error("没有找到短信模板");
            }
            String template = (String) result.getData();
            var map = GetMessageTemplate.getKey(template);
            map.put("tsjy_bmgly", employeesOutput.getName());
            //获取模板内容
            PageData pageData1 = new PageData();
            String content = GetMessageTemplate.getContent(template, map);
            pageData1.put("sendList",list);
            pageData1.put("description",content);
            pageData1.put("isTiming",0);
            pageData1.put("type", MessageTypeEnum.INFO_DEPART_MANAGER.getCode());
            //发送短信给部门管理员
            ResponseResult responseResult = ServiceCall.postResult(loadBalancerClient, "smsSend/form", "message", pageData1);
            if(responseResult.getCode() != 200){
                return ResponseResult.error("发送给部门管理员的短信发送失败");
            }
        }
        return ResponseResult.success("提醒部门管理员处理成功");
    }

    @ApiOperation("设定投诉建议为无效事件")
    @GetMapping(value = "setInvalid")
    public ResponseResult setInvalid(String idList) throws Exception {
        return suggesstionsService.setInvalid(idList);
    }

    @GetMapping(value = "findOutOfDatePageList")
    @ApiOperation("获取逾期未处理的投诉建议的")
    @ApiImplicitParams({
            @ApiImplicitParam(name="suggestionPhoneNumber",value="手机号码",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="suggestionName",value="用户姓名",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="departmentId",value="投诉部门",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="userName",value="制定回复人",required=false,dataType="String", paramType = "query"),
            @ApiImplicitParam(name="feedBackTimeBegin",value="反馈开始时间",required=false,dataType="String", paramType = "query"),
            @ApiImplicitParam(name="feedBackTimeEnd",value="反馈结束时间",required=false,dataType="String", paramType = "query")})
    public ResponseResult findOutOfDatePageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        Integer administratorLevel = suggesstionsService.getUsers().getAdministratorLevel();
        pageData.put("userId",suggesstionsService.getUsers().getId().toString());
        if(administratorLevel!=9){
            if(suggesstionsService.getUsers().getUserType()==0){
                pageData.put("employeeNo",suggesstionsService.getUsers().getUsername());
            }else {
                pageData.put("orgId",suggesstionsService.getUsers().getOrganizationId().toString());
            }
        }
        return suggesstionsService.findOutOfDatePageList(pageData);
    }
    /**
     *
     * @param
     * @return
     */
    @ApiOperation("根据搜索条件导出指标")
    @RequestMapping(value = "outOfDatePageListExport", method = RequestMethod.GET)
    public ResponseResult appraisalIndexExport(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = suggesstionsService.outOfDatePageListExport(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "getOutSuggesstionDetail")
    @ApiOperation(value="获得逾期的投诉建议的详情")
    public ResponseResult getOutSuggesstionDetail(Integer id)  {
        if( id==null ){
            ResponseResult.error(PARAM_EORRO);
        }
        SuggesstionsOutput suggesstionsOutput = suggesstionsService.getOutSuggesstionDetail(id);
        return ResponseResult.success(suggesstionsOutput);
        
    }

    @GetMapping(value = "sysnc")
    @ApiOperation(value="同步199服务器")
    public ResponseResult sysnc(){
        suggesstionsService.sync();
        return ResponseResult.success("同步成功");
    }


    @PostMapping("formEmployeeRecordWithNoAppraisal")
    public ResponseResult formEmployeeRecordWithNoAppraisal(@Valid @RequestBody AppraisalEmployeeRecord appraisalEmployeeRecord) throws JsonProcessingException {
        PageData pageData = new PageData();
        pageData.put("appraisalEmployeeRecord", appraisalEmployeeRecord);
        if(!ServiceCall.postResult(loadBalancerClient,"/appraisalemployeerecord/formEmployeeRecordWithNoAppraisal","assessment",pageData).isSuccess()){
            return ResponseResult.error("员工扣分失败");
        }
        return ResponseResult.success();
    }

    //修改部门考核计划分数
    @PostMapping("formOrgScore")
    public ResponseResult formEmployeeRecordWithNoAppraisal(@Valid @RequestBody AppraisalPlanItem appraisalPlanItem) throws JsonProcessingException {
        PageData pageData = new PageData();
        pageData.put("id", appraisalPlanItem.getId().toString());
        pageData.put("appraisalPlanItem",appraisalPlanItem);
        ResponseResult result = ServiceCall.postResult(loadBalancerClient, "/appraisalplanitem/formOrgScore", "assessment", pageData);
        return result;

    }

    //编辑评分说明
    @GetMapping("descrptionEdit")
    public ResponseResult descrptionEdit(Integer id,String description) throws JsonProcessingException {
        PageData pageData = new PageData();
        pageData.put("id", id);
        pageData.put("description",description);
        ResponseResult result = ServiceCall.postResult(loadBalancerClient, "/appraisalplanitem/formDescrptionInput", "assessment", pageData);
        return result;

    }

    //获取部门考核计划详情
    @GetMapping("getDetail")
    public ResponseResult getDetail(Integer planId) {
        PageData pageData = new PageData();
        pageData.put("planId", planId.toString());
        ResponseResult result = ServiceCall.getResult(loadBalancerClient, "/appraisalplanitem/findByPlanId", "assessment", pageData);
        return result;
    }

    /**获取考核计划明细的附件*/
    @GetMapping("getAppraisalPlanItemAttachment")
    public ResponseResult getAppraisalPlanItemAttachment(Integer planItemId) {
        PageData pageData = new PageData();
        pageData.put("id",planItemId.toString());
        ResponseResult result = ServiceCall.getResult(loadBalancerClient, "/appraisalplanitem/getAdditionById", "assessment", pageData);
        return result;
    }

    /**保存考核计划明细的附件*/
    @PostMapping("saveAppraisalPlanItemAttachment")
    public ResponseResult saveAppraisalPlanItemAttachment(Integer planItemId,@RequestBody AppraisalPlanItemAttachmentInput appraisalPlanItemAttachmentInput) throws JsonProcessingException {
        PageData pageData = new PageData();
        pageData.put("planItemId",planItemId.toString());
        pageData.put("list",appraisalPlanItemAttachmentInput.getList());
        ResponseResult result = ServiceCall.postResult(loadBalancerClient, "/appraisalplanitem/saveAppraisalPlanItemAttachment", "assessment", pageData);
        return result;
    }
}
