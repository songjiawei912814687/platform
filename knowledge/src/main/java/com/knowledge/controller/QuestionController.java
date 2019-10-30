package com.knowledge.controller;

import com.common.Enum.AttachmentEnum;
import com.common.Enum.MessageTypeEnum;
import com.common.Enum.SmsTemplateEnum;
import com.common.model.PageData;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import com.common.utils.HttpRequestUtil;
import com.google.common.collect.Lists;
import com.knowledge.core.base.BaseController;
import com.knowledge.core.base.BaseService;
import com.knowledge.domain.output.QuestionOutput;
import com.knowledge.model.Attachment;
import com.knowledge.model.Question;
import com.knowledge.model.Users;
import com.knowledge.service.QuestionService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("question")
public class QuestionController extends BaseController<QuestionOutput,Question,Integer> {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Override
    public BaseService<QuestionOutput, Question, Integer> getService() {
        return questionService;
    }

    @Override
    @ApiOperation("提出问题")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="指标信息",value="传入json格式",required=true) Question question) throws Exception {

        Integer applyId ;
        if(question.getOrganizationId()==null||question.getOrganizationId().equals("")){
            return ResponseResult.error("所属组织机构不能为空");
        }
        if(!questionService.verificationOrg(question)){
            return ResponseResult.error("必须将问题关联末级组织");
        }
        question.setState(0);
        if(id!=null){
            Question question1=getService().getById(id);
            if(question==null){
                return ResponseResult.error("问题不存在");
            }
            if(!question1.getCreatedUserId().equals(getService().getUsers().getId())){
                return ResponseResult.error("不是创建人不能编辑");
            }
            applyId = questionService.update(id,question);
            if(applyId<=0){
                return ResponseResult.error("跟新失败");
            }
        }else {
            question.setAnswerState(0);
            question.setIsOpen(0);
            question.setIsTop(0);
            applyId = questionService.add(question);
            if(applyId<=0){
                return ResponseResult.error("新增失败");
            }
        }
        if (!CollectionUtils.isEmpty(question.getAttachmentList())) {
            List<Attachment> attachmentList = Lists.newArrayList();
            for (Attachment attachment : question.getAttachmentList()) {
                attachment.setResourcesId(applyId);
                attachment.setResourcesType(AttachmentEnum.QUESTION.getCode());
                attachment.setSourceFileName(attachment.getSuffix());
                attachment.setCreatedDateTime(new Date());
                attachment.setCreatedUserId(this.getService().getUsers().getId());
                attachment.setCreatedUserName(this.getService().getUsers().getUsername());
                attachment.setAmputated(0);
                attachmentList.add(attachment);
            }
            //附件上传
            PageData pageData = new PageData();
            pageData.put("attachmentList", attachmentList);
            ResponseResult attendanceSourceResult = HttpRequestUtil.sendPostRequest("http://10.32.250.88:8770/attachment/attachmentApi", pageData);
            if (attendanceSourceResult.getCode() != 200) {
                return ResponseResult.error("附件上传失败");
            }
           return ResponseResult.success();
        }
        return ResponseResult.success();
    }

    @Override
    @ApiOperation("删除咨询问题")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        String[] strings=idList.split(",");
        if(strings.length>1){
            return ResponseResult.error("只能单个删除");
        }else {
            Question question=getService().getById(Integer.parseInt(strings[0]));
            if(question==null){
                return ResponseResult.error(PARAM_EORRO);
            }else {
                if(!question.getCreatedUserId().equals(getService().getUsers().getId())){
                    return ResponseResult.error("不是创建人不能删除");
                }
            }

        }
        return super.logicDelete(idList);
    }


    @ApiOperation("获取所有咨询问题")
    @GetMapping(value = "selectAll")
    public ResponseResult selectAll(HttpServletRequest request){
        return  super.selectAll(new PageData(request));
    }


    @GetMapping(value = "findPageList")
    @ApiImplicitParams({@ApiImplicitParam(name="title",value="问题标题",required=false,dataType="String", paramType = "query"),
            @ApiImplicitParam(name="organizationId",value="组织机构Id",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="type",value="问题类型",required=false,dataType="String", paramType = "query")
    })
    public ResponseResult selectPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        if(pageData.containsKey("organizationId")){
          if(pageData.get("organizationId")!=null&&!pageData.get("organizationId").equals("")){
              pageData.put("paths",","+pageData.get("organizationId")+",");
          }
        }
        pageData.put("userId",questionService.getUsers().getId().toString());
        if(questionService.getUsers().getAdministratorLevel()!=9){
            if(questionService.getUsers().getUserType()==1){
                pageData.put("orgId",questionService.getUsers().getOrganizationId().toString());
            }
        }
        return super.selectPageList(pageData);
    }

    @Override
    @GetMapping(value = "get")
    @ApiOperation(value="根据id获取单个咨询问题")
    public ResponseResult get(Integer id) {

        QuestionOutput questionOutput = this.getService().selectById(id);
        PageData pageData = new PageData();
        pageData.put("id",id.toString());
        pageData.put("type",AttachmentEnum.QUESTION.getCode().toString());
        ResponseResult call = ServiceCall.getResult(loadBalancerClient,"attachment/getAttachmentListByResouceIdAndType","BIGDATA",pageData);
        if(call.getCode()!=200){
            return ResponseResult.success(questionOutput);
        }
        List<Attachment> attachmentList =(List<Attachment>) call.getData();
        if(!CollectionUtils.isEmpty(attachmentList)){
            questionOutput.setAttachmentList(attachmentList);
        }
        return ResponseResult.success(questionOutput);
    }

    @ApiOperation("是否公开")
    @GetMapping(value = "isOpen")
    public ResponseResult isOpen(Integer id,Integer isOpen){
        if (id == null||id.equals("")||isOpen==null||isOpen.equals("")||!(isOpen==0||isOpen==1)) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = 0;
        try {
            result = questionService.updateIsOpen(id,isOpen);
        } catch (Exception e) {
            return ResponseResult.error(SYS_EORRO);
        }
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

    @ApiOperation("是否置顶")
    @GetMapping(value = "isTop")
    public ResponseResult isTop(Integer id,Integer isTop){
        if (id == null||id.equals("")||isTop==null||isTop.equals("")||!(isTop==0||isTop==1)) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = 0;
        try {
            result = questionService.updateIsTop(id,isTop);
        } catch (Exception e) {
            return ResponseResult.error(SYS_EORRO);
        }
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

    @ApiOperation("发布")
    @GetMapping(value = "issued")
    public ResponseResult issued(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        if (id == null||id.equals("")) {
            return ResponseResult.error(PARAM_EORRO);
        }
        QuestionOutput question=getService().selectById(id);
        if(question==null){
           return ResponseResult.error(PARAM_EORRO);
        }
        PageData pageData = new PageData();
        pageData.put("organizationId",question.getOrganizationId()+"");
        List<Map<String,String>> result = (List<Map<String,String>>)ServiceCall.getResult(loadBalancerClient, "/organization/getOrganizationMobile", "personnel", pageData).getData();
        if(result==null||result.size()<=0){
            return ResponseResult.error("问题所属部门没有责任人不能发布");
        }
        boolean a=false;
        for(Map output:result){
            //判断有没有手机号码
            if(output.get("phoneNumber")!=null){
                a=true;
            }
        }
        if(!a){
            return ResponseResult.error("问题所属部门没有责任人不能发布");
        }
        Question question1=questionService.getById(id);
        question1.setState(1);
        question1.setReleaseTime(new Date());
        if(questionService.update(id,question1)>0){
            questionService.sendMassage(result,"wttsbmfzr");
        }else {
            return ResponseResult.error("发布失败");
        }
        return ResponseResult.success();
    }

    @ApiOperation("回答问题")
    @RequestMapping(value = "answerQuestion", method = RequestMethod.POST)
    public ResponseResult answerQuestion(Integer id,@RequestBody Question question) throws Exception {
        if(id==null||id.equals("")||question.getDescription()==null||question.getDescription().equals("")){
            return ResponseResult.error(PARAM_EORRO);
        }
        question.setAnswerState(1);
        Integer applyId = questionService.add(question);
        if(applyId<0){
            return ResponseResult.error("问题回答失败");
        }
        if (!CollectionUtils.isEmpty(question.getAttachmentList())) {
            List<Attachment> attachmentList = Lists.newArrayList();
            for (Attachment attachment : question.getAttachmentList()) {
                attachment.setResourcesId(applyId);
                attachment.setResourcesType(AttachmentEnum.QUESTION.getCode());
                attachment.setSourceFileName(attachment.getSuffix());
                attachment.setCreatedDateTime(new Date());
                attachment.setCreatedUserId(this.getService().getUsers().getId());
                attachment.setCreatedUserName(this.getService().getUsers().getUsername());
                attachment.setAmputated(0);
                attachmentList.add(attachment);
            }
            //附件上传
            PageData pageData = new PageData();
            pageData.put("attachmentList", attachmentList);
            ResponseResult attendanceSourceResult = HttpRequestUtil.sendPostRequest("http://10.32.250.88:8770/attachment/attachmentApi", pageData);
            if (attendanceSourceResult.getCode() != 200) {
                return ResponseResult.error("附件上传失败");
            }
        }
        return ResponseResult.success();
    }



    @ApiOperation("获取所有类型")
    @GetMapping(value = "/getTypeList")
    public ResponseResult getTypeList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        pageData.put("key","questionType");
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,"/config/getListByKey","bigdata",pageData);
        return response;
    }

    /**4.导出到excel**/
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ResponseResult export(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = questionService.export(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.error("导出失败");
    }

    /**
     * 已回答的问题，发送答案给提问人，
     * 未回答问题，提示问题未回答，没有答案可以发送
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation("发送答案给提问人")
    @GetMapping(value = "sendAnswer")
    public ResponseResult sendAnswer(Integer id) throws Exception {

        if (id == null||id.equals("")) {
            return ResponseResult.error(PARAM_EORRO);
        }
        QuestionOutput question=getService().selectById(id);
        if(question==null){
            return ResponseResult.error(PARAM_EORRO);
        }

        if (question.getDescription() == null || question.getDescription().equals("")
                || question.getAskName() == null || question.getAskName().equals("")
                || question.getAskTel() == null || question.getAskTel().equals("")) {
            return ResponseResult.error(PARAM_EORRO);
        }
        if (question.getAnswerState() == 0) {
            return ResponseResult.error("问题未回答，没有答案可以发送");
        } else if (question.getAnswerState() == 1) {
            PageData pageData = new PageData();
            List<String> list = new ArrayList<>();
            list.add(question.getAskTel() + "/" + question.getAskName());
            //"fsda"== 发送答案
            String des = questionService.getContent("fsda", question.getAskName());
            if (StringUtils.isNotBlank(des)) {
                des = des +"问题:"+question.getTitle()+";答案:"+question.getDescription();
                pageData.put("description", des);
                pageData.put("isTiming", 0);
                pageData.put("type", MessageTypeEnum.MANUAL_TYPE.getCode());
                pageData.put("sendList", list);
                try {
                    var r = ServiceCall.postResult(loadBalancerClient, "/smsSend/form", "message", pageData);
                } catch (Exception e) {

                }
            }else{
                return ResponseResult.error("发送答案失败");
            }
        }
        return ResponseResult.success();
    }


    @PostMapping("importQuestion")
    public ResponseResult importQuestion(MultipartFile files) throws InvocationTargetException, IntrospectionException, IllegalAccessException, IOException {
        if(files.isEmpty()){
            return ResponseResult.error("请选择文件");
        }
       return questionService.checkedFile(files);
    }
}
