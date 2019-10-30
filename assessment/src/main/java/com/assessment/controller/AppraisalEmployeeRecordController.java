package com.assessment.controller;

import com.assessment.core.base.BaseController;
import com.assessment.core.base.BaseService;
import com.assessment.domian.input.AppraisalComplaintInput;
import com.assessment.domian.input.AppraisalEmployeeRecordInput;
import com.assessment.domian.output.AppraisalEmployeeRecordOutput;
import com.assessment.model.AppraisalEmployeeRecord;
import com.assessment.model.Attachment;
import com.assessment.service.AppraisalEmployeeRecordService;
import com.assessment.service.AttachmentService;
import com.assessment.service.UserService;
import com.common.Enum.ApprovalTypeEnum;
import com.common.Enum.AttachmentEnum;
import com.common.Enum.CheckStatusEnum;
import com.common.Enum.IsValidEnum;
import com.common.model.PageData;
import com.common.request.Audit;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/appraisalemployeerecord")
@Api(value = "人员加减分记录controller",description = "人员加减分记录操作",tags = {"人员加减分记录操作接口"})
public class AppraisalEmployeeRecordController extends BaseController<AppraisalEmployeeRecordOutput, AppraisalEmployeeRecord,Integer> {

    @Autowired
    private AppraisalEmployeeRecordService appraisalEmployeeRecordService;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private UserService userService;

    @Override
    public BaseService<AppraisalEmployeeRecordOutput, AppraisalEmployeeRecord, Integer> getService() {
        return appraisalEmployeeRecordService;
    }

    @Override
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @RequestBody AppraisalEmployeeRecord appraisalEmployeeRecord) throws Exception {
        int recordId=0;
        if(id==null){
            var users = userService.selectByEmployeeId(appraisalEmployeeRecord.getEmployeeId());
            if(users == null){
                return ResponseResult.error("申请人账号未激活或申请人错误");
            }
            appraisalEmployeeRecord.setDisplayOrder(0);
            appraisalEmployeeRecord.setState(0);
            recordId=appraisalEmployeeRecordService.add(appraisalEmployeeRecord);
            if(!Audit.apply(loadBalancerClient,recordId,users.getId(), ApprovalTypeEnum.EMPLOYEE_RECORD_TYPE.getCode())){
                this.delete(String.valueOf(recordId));
                return ResponseResult.error("提交审批失败");
            }

        } else {
            recordId=appraisalEmployeeRecordService.update(id,appraisalEmployeeRecord);
        }
        if(recordId>0){
            List<Attachment> attachmentList=appraisalEmployeeRecord.getAttachmentList();
            if(attachmentList.size()>0){
                List<Attachment> attachments=new ArrayList<Attachment>();
                if(attachmentList!=null&&attachmentList.size()>0){
                    for(Attachment attachment:attachmentList){
                        attachment.setResourcesId(recordId);
                        attachment.setResourcesType(AttachmentEnum.APPRAISAL_EMPLOYEE_RECORD_TYPE.getCode());
                        attachment.setSourceFileName(attachment.getSuffix());
                        attachments.add(attachment);
                    }
                }
                PageData pageData = new PageData();
                pageData.put("attachmentList",attachments);
                if(!ServiceCall.postResult(loadBalancerClient,"/attachment/attachmentApi","bigdata",pageData).isSuccess()){
                    this.delete(String.valueOf(recordId));
                    return ResponseResult.error("上传附件失败");
                }
            }
        }else {
            this.delete(String.valueOf(recordId));
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

    @PostMapping(value = "formEmployeeRecordWithNoAppraisal")
    public ResponseResult formEmployeeRecordWithNoAppraisal(@RequestBody AppraisalEmployeeRecordInput appraisalEmployeeRecordInput) throws Exception {
        int recordId;
        AppraisalEmployeeRecord appraisalEmployeeRecord = appraisalEmployeeRecordInput.getAppraisalEmployeeRecord();

        var users = userService.selectByEmployeeId(appraisalEmployeeRecord.getEmployeeId());
        if(users == null){
            return ResponseResult.error("申请人账号未激活或申请人错误");
        }
        appraisalEmployeeRecord.setDisplayOrder(0);
        appraisalEmployeeRecord.setState(1);
        recordId=appraisalEmployeeRecordService.add(appraisalEmployeeRecord);

        if(recordId>0){
            List<Attachment> attachmentList=appraisalEmployeeRecord.getAttachmentList();
            if(!CollectionUtils.isEmpty(attachmentList)){
                List<Attachment> attachments=new ArrayList<Attachment>();
                for(Attachment attachment:attachmentList){
                    attachment.setResourcesId(recordId);
                    attachment.setResourcesType(AttachmentEnum.APPRAISAL_EMPLOYEE_RECORD_TYPE.getCode());
                    attachment.setSourceFileName(attachment.getSuffix());
                    attachment.setAmputated(0);
                    attachment.setCreatedDateTime(new Date());
                    attachment.setCreatedUserId(this.getService().getUsers().getId());
                    attachment.setLastUpdateDateTime(new Date());
                    attachment.setLastUpdateUserId(this.getService().getUsers().getId());
                    attachments.add(attachment);
                }

                PageData pageData = new PageData();
                pageData.put("attachmentList",attachments);
                if(!ServiceCall.postResult(loadBalancerClient,"/attachment/attachmentApi","bigdata",pageData).isSuccess()){
                    this.delete(String.valueOf(recordId));
                    return ResponseResult.error("上传附件失败");
                }
            }
        }else {
            this.delete(String.valueOf(recordId));
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

    //设置为无效
    @GetMapping("updateIsUse")
    public ResponseResult updateIsUse(Integer id,Integer validStatus) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        if(validStatus==null||id ==null){
            return ResponseResult.error(PARAM_EORRO);
        }
        //1.查询出该条加减分
        AppraisalEmployeeRecordOutput appraisalEmployeeRecordOutput = appraisalEmployeeRecordService.getById(id);
        if(appraisalEmployeeRecordOutput == null){
            return ResponseResult.error("该记录不存在");
        }
        if(!CheckStatusEnum.CHECK_FINISH.getCode().equals(appraisalEmployeeRecordOutput.getState())){
            return ResponseResult.error("只有在审核通过的情况下才可以设置");
        }
        appraisalEmployeeRecordOutput.setIsValid(validStatus);
        AppraisalEmployeeRecord appraisalEmployeeRecord = new AppraisalEmployeeRecord();
        BeanUtils.copyProperties(appraisalEmployeeRecordOutput,appraisalEmployeeRecord);
        int record = this.getService().update(id,appraisalEmployeeRecord);
        if(record<0){
            return ResponseResult.error("设置状态失败");
        }
        return ResponseResult.success("设置状态成功");
    }

    @Override
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws InvocationTargetException, IntrospectionException, IllegalAccessException, MethodArgumentNotValidException {
        if(idList==null){
            return ResponseResult.error(PARAM_EORRO);
        }
        AppraisalEmployeeRecordOutput appraisalEmployeeRecordOutput = appraisalEmployeeRecordService.getById(Integer.parseInt(idList));
        if(appraisalEmployeeRecordOutput == null){
            return ResponseResult.error("该记录不存在");
        }
        //如果是审核通过的情况下则返回
        if(CheckStatusEnum.CHECK_FINISH.getCode().equals(appraisalEmployeeRecordOutput.getState())){
            return ResponseResult.error("审核已经通过，无法删除");
        }

        ResponseResult deleteResult = super.logicDelete(idList);
        if(deleteResult.getCode()==200){
            String[] idArray = idList.split(",");
            for(String id:idArray){
                if(Audit.deleteByResourceId(loadBalancerClient,id,ApprovalTypeEnum.EMPLOYEE_RECORD_TYPE.getCode())){
                    return ResponseResult.success();
                }
                return ResponseResult.error("删除员工加减分审批失败");
            }

        }
        return ResponseResult.error("删除员工加减分失败");
    }

    @Override
    @GetMapping(value = "get")
    public ResponseResult get(Integer id) {
        if( id==null ){
            ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(appraisalEmployeeRecordService.getById(id));
    }

    @GetMapping(value = "findPageList")
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        pageData.put("userId",appraisalEmployeeRecordService.getUsers().getId().toString());
        if(pageData.getMap().get("organizationId")!=null&&!pageData.getMap().get("organizationId").equals("")){
            String  s=pageData.getMap().get("organizationId");
            pageData.put("path",s+",");
        }
        if(appraisalEmployeeRecordService.getUsers().getAdministratorLevel()!=9){
            if(appraisalEmployeeRecordService.getUsers().getUserType()==0){
                pageData.put("employeeId",appraisalEmployeeRecordService.getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",appraisalEmployeeRecordService.getUsers().getOrganizationId().toString());
            }
        }
        return super.selectPageList(pageData);
    }


    /**
     * 导出加减分记录信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ResponseResult export(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = appraisalEmployeeRecordService.export(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 改变员工加减分记录状态状态
     *
     * @param
     * @return
     */
    @RequestMapping(value = "updateState", method = RequestMethod.POST)
    public ResponseResult updateState(@RequestBody AppraisalComplaintInput appraisalComplaintInput) throws Exception {
        ResponseResult result = new ResponseResult();
        result.setSuccess(true);
        AppraisalEmployeeRecord appraisalEmployeeRecord=new AppraisalEmployeeRecord();
        if(appraisalComplaintInput.getId()==null||appraisalComplaintInput.getId().equals("")||appraisalComplaintInput.getState()==null||appraisalComplaintInput.getState().equals("")){
            ResponseResult.error(PARAM_EORRO);
        }
        appraisalEmployeeRecord.setState(appraisalComplaintInput.getState());
        appraisalEmployeeRecord.setId(appraisalComplaintInput.getId());
        //如果是审核通过的情况下设置成有效
        if(appraisalComplaintInput.getState().equals(CheckStatusEnum.CHECK_FINISH.getCode())){
            //设置成有效状态
            appraisalEmployeeRecord.setIsValid(IsValidEnum.IS_VALID.getCode());
        }else {
            //否则设置成无效状态
            appraisalEmployeeRecord.setIsValid(IsValidEnum.UN_VALID.getCode());
        }
        if( appraisalEmployeeRecordService.update(appraisalComplaintInput.getId(),appraisalEmployeeRecord)>0){
            result.setSuccess(false);
        }
        return result;
    }


    /**
     * 临时接口，添加待审核没有审批数据的审批数据
     *
     * @param
     * @return
     */
    @RequestMapping(value = "addDate", method = RequestMethod.POST)
    public ResponseResult addDate(@RequestBody AppraisalComplaintInput appraisalComplaintInput) throws Exception {
        List<AppraisalEmployeeRecordOutput> list = appraisalEmployeeRecordService.getByState(0);
        for (AppraisalEmployeeRecordOutput appraisalEmployeeRecordOutput:list) {
            var users = userService.selectByEmployeeId(appraisalEmployeeRecordOutput.getEmployeeId());
            if(!Audit.apply(loadBalancerClient,appraisalEmployeeRecordOutput.getId(),users.getId(), ApprovalTypeEnum.EMPLOYEE_RECORD_TYPE.getCode())){
                this.logicDelete(String.valueOf(appraisalEmployeeRecordOutput.getId()));
                return ResponseResult.error("提交审批失败");
            }
        }
        return ResponseResult.success();
    }

}
