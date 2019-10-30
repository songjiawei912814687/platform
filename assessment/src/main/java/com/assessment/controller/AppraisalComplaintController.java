package com.assessment.controller;

import com.assessment.core.base.BaseController;
import com.assessment.core.base.BaseService;
import com.assessment.domian.input.AppraisalComplaintInput;
import com.assessment.domian.output.AppraisalComplaintOutput;
import com.assessment.model.AppraisalComplaint;
import com.assessment.model.Attachment;
import com.assessment.service.AppraisalComplaintService;
import com.assessment.service.AttachmentService;
import com.common.Enum.ApprovalTypeEnum;
import com.common.Enum.AttachmentEnum;
import com.common.model.PageData;
import com.common.request.Audit;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/appraisalcomplaint")
@Api(value = "考核申诉controller",description = "考核申诉操作",tags = {"考核申诉操作接口"})
public class AppraisalComplaintController extends BaseController<AppraisalComplaintOutput, AppraisalComplaint,Integer> {


    @Autowired
    private AppraisalComplaintService appraisalComplaintService;

    @Override
    public BaseService<AppraisalComplaintOutput,AppraisalComplaint,Integer> getService(){
        return appraisalComplaintService;
    }
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private AttachmentService attachmentService;








    @Override
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String complaintId) throws InvocationTargetException, IntrospectionException, IllegalAccessException, MethodArgumentNotValidException {
        if(complaintId==null){
            return ResponseResult.error(PARAM_EORRO);
        }
        ResponseResult deleteResult = super.logicDelete(complaintId);
        if(deleteResult.getCode()==200){
            String[] idArray = complaintId.split(",");
            for(String id:idArray){
                if(Audit.deleteByResourceId(loadBalancerClient,id,ApprovalTypeEnum.ASSESSMENT_APPEAL_TYPE.getCode())){
                    return ResponseResult.success();
                }
                return ResponseResult.error("删除考核申诉审批失败");
            }

        }
        return ResponseResult.error("删除考核申诉失败");
    }

    @Override
    @GetMapping(value = "get")
    public ResponseResult selectById(Integer id)  {
        return ResponseResult.success(appraisalComplaintService.getById(id));
    }

    @GetMapping(value = "findassessmentRepresentation")
    @ApiOperation("获取考核申诉列表")
    @ApiImplicitParams({@ApiImplicitParam(name="organizationId",value="组织机构id",required=false,dataType="int", paramType = "query"),
                        @ApiImplicitParam(name="year",value="年",required=false,dataType="int", paramType = "query"),
                        @ApiImplicitParam(name="month",value="月",required=false,dataType="int", paramType = "query"),
                        @ApiImplicitParam(name="employeeName",value = "考核员工名称",required = false,dataType = "int",paramType = "query"),
                        @ApiImplicitParam(name="state",value = "状态",required = false,dataType = "int",paramType = "query")})
    public ResponseResult findAssessmentRepresentation(String organizationId,Integer year,Integer month,String employeeName,Integer state ){
        if((year!=null&&(year>9999||year%1!=0))||(month!=null&&(month<1||month>12||month%1!=0))){
            return ResponseResult.error(PARAM_EORRO);
        }
        PageData pageData = new PageData();
        pageData.put("year",year);
        pageData.put("month",month);
        pageData.put("state",state);
        pageData.put("organizationId", organizationId);
        pageData.put("userId",appraisalComplaintService.getUsers().getId().toString());
        if(appraisalComplaintService.getUsers().getAdministratorLevel()!=9){
            if(appraisalComplaintService.getUsers().getUserType()==0){
                pageData.put("employeeId",appraisalComplaintService.getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",appraisalComplaintService.getUsers().getOrganizationId().toString());
            }
        }
        return appraisalComplaintService.findAssessmentRepresentation(pageData);
    }
//public ResponseResult findassessmentRepresentation(HttpServletRequest httpServletRequest){
//        PageData pageData=new PageData(httpServletRequest);
//        if(pageData.getMap().get("organizationId")!=null&&!pageData.getMap().get("organizationId").equals("")){
//            String  s=appraisalComplaintService.getPathById(Integer.parseInt(pageData.getMap().get("organizationId")));
//            pageData.put("path",s+",");
//        }
//        return ResponseResult.success(appraisalComplaintService.getappraisalComplaint(pageData));
//    }




    /**
     * 导出考核申诉信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ResponseResult export(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str=appraisalComplaintService.export(response,request);
            return  ResponseResult.success(str);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.error("获取报表失败");
    }








    /**
     * 新增考核申诉信息
     *
     * @param
     * @return
     */

    @ApiOperation("考核申诉")
    @PostMapping(value = "assessmentRepresentation")
    public ResponseResult assessmentRepresentation(Integer planId, @Validated @RequestBody AppraisalComplaint appraisalComplaint) throws Exception {
        if(planId==null||appraisalComplaint.getDescription()==null||"".equals(appraisalComplaint.getDescription())){
            return  ResponseResult.error(PARAM_EORRO);
        }
        int appealId = appraisalComplaintService.assessmentRepresentation(planId,appraisalComplaint.getDescription());
        if (appealId==-1){
            return ResponseResult.error("未找到考核计划信息");
        }else if (appealId==-3){
            return ResponseResult.error("选定考核计划存在未审核申诉内容，不可重复申述");
        }else if (appealId==-2){
            return ResponseResult.error("选定考核计划没有最终得分，不可申述");
        }

        if (appealId > 0) {
            List<Attachment> attachmentList = appraisalComplaint.getAttachmentList();
            List<Attachment> attachments=new ArrayList<Attachment>();
            if (attachmentList != null && attachmentList.size() > 0) {
                for (Attachment attachment : attachmentList) {
                    if (attachment.getFileName() == null || "".equals(attachment.getFileName())) {
                        return ResponseResult.error("附件名不能为空");
                    }
                    if (attachment.getUrl() == null || "".equals(attachment.getUrl())) {
                        return ResponseResult.error("附件地址不能为空");
                    }
                    if (attachment.getSuffix() == null || "".equals(attachment.getSuffix())) {
                        return ResponseResult.error("附件后缀名不能为空");
                    }
                    attachment.setResourcesId(appealId);
                    attachment.setResourcesType(AttachmentEnum.APPRAISAL_COMPLAIN_TYPE.getCode());
                    attachment.setSourceFileName(attachment.getSuffix());
                    attachments.add(attachment);
                }
                PageData pageData = new PageData();
                pageData.put("attachmentList",attachments);
                if(ServiceCall.postResult(loadBalancerClient,"attachment/attachmentApi","bigdata",pageData).getCode()!=200){
                    return ResponseResult.error("上传附件失败");
                }
            }
            if(!Audit.apply(loadBalancerClient,appealId,getService().getUsers().getId(), ApprovalTypeEnum.ASSESSMENT_APPEAL_TYPE.getCode())){
                this.delete(String.valueOf(appealId));
                return ResponseResult.error("提交审批失败");
            }

        }
        else {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();




    }



    /**
     * 编辑考核申诉信息
     *
     * @param
     * @return
     */

    @ApiOperation("考核申诉申诉原因修改")
    @RequestMapping(value = "representationAdjustment", method = RequestMethod.POST)
    public ResponseResult representationAdjustment(@RequestParam("complaintId") Integer  complaintId,@Validated @RequestBody AppraisalComplaint appraisalComplaint) throws Exception {
        if(complaintId==null||appraisalComplaint.getDescription()==null||"".equals(appraisalComplaint.getDescription())){
            return  ResponseResult.error(PARAM_EORRO);
        }
        int appealId = appraisalComplaintService.representationAdjustment(complaintId, appraisalComplaint.getDescription());
        if (appealId > 0) {
            List<Attachment> attachmentList = appraisalComplaint.getAttachmentList();
            if(attachmentList!=null&&attachmentList.size()>0){
                List<Attachment> attachments=new ArrayList<Attachment>();
                if (attachmentList != null && attachmentList.size() > 0) {
                    for (Attachment attachment : attachmentList) {
                        if (attachment.getFileName() == null || "".equals(attachment.getFileName())) {
                            return ResponseResult.error("附件名不能为空");
                        }
                        if (attachment.getUrl() == null || "".equals(attachment.getUrl())) {
                            return ResponseResult.error("附件地址不能为空");
                        }
                        if (attachment.getSuffix() == null || "".equals(attachment.getSuffix())) {
                            return ResponseResult.error("附件后缀名不能为空");
                        }
                        attachment.setResourcesId(appealId);
                        attachment.setResourcesType(AttachmentEnum.APPRAISAL_COMPLAIN_TYPE.getCode());
                        attachment.setSourceFileName(attachment.getSuffix());
                        attachments.add(attachment);
                    }
                    PageData pageData = new PageData();
                    pageData.put("attachmentList",attachments);
                    if(!ServiceCall.postResult(loadBalancerClient,"attachment/attachmentApi","bigdata",pageData).isSuccess()){
                        return ResponseResult.error("上传附件失败");
                    }
                }
            }
        }
        else {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();


    }




    /**
     * 改变考核申诉状态状态
     *
     * @param
     * @return
     */
    @RequestMapping(value = "updateState", method = RequestMethod.POST)
    public ResponseResult updateState(@RequestBody AppraisalComplaintInput appraisalComplaintInput) throws Exception {
        ResponseResult result = new ResponseResult();
        result.setSuccess(true);
        AppraisalComplaint appraisalComplaint=new AppraisalComplaint();
        if(appraisalComplaintInput.getId()==null||appraisalComplaintInput.getId().equals("")||appraisalComplaintInput.getState()==null||appraisalComplaintInput.getState().equals("")){
            ResponseResult.error(PARAM_EORRO);
        }
        appraisalComplaint.setState(appraisalComplaintInput.getState());
        appraisalComplaint.setId(appraisalComplaintInput.getId());
        if( appraisalComplaintService.update(appraisalComplaintInput.getId(),appraisalComplaint)>0){
            result.setSuccess(false);
        }
        return result;
    }




}
