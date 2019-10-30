package com.meeting.controller;

import com.common.Enum.ApprovalTypeEnum;
import com.common.Enum.AttachmentEnum;
import com.common.model.PageData;
import com.common.request.Audit;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import com.meeting.core.base.BaseController;
import com.meeting.core.base.BaseService;
import com.meeting.domain.output.*;
import com.meeting.model.Attachment;
import com.meeting.model.MeetingApply;
import com.meeting.model.PitApply;
import com.meeting.service.PitApplyService;
import com.meeting.service.PitService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/pitapply")
public class PitApplyController extends BaseController<PitApplyOutput, PitApply,Integer> {
    @Autowired
    private PitService pitService;

    @Autowired
    private PitApplyService pitApplyService;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Override
    public BaseService<PitApplyOutput, PitApply, Integer> getService() {
        return pitApplyService;
    }

    @Override
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id,@RequestBody  PitApply pitApply) throws Exception {
        String name=null;
        PageData pageData=new PageData();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(pitApply.getAppointmentDate());
        pageData.put("date",str);
        pageData.put("meetingRoomId",pitApply.getMeetingRoomId());
        List<PitApplyOutput> meetingApplyOutputList=pitApplyService.getByRoomId(pageData);
        if(meetingApplyOutputList.size()>0){
            for(PitApplyOutput meetingApplyOutput:meetingApplyOutputList){
                if(meetingApplyOutput.getId().equals(pitApply.getId())){
                    continue;
                }
                if((meetingApplyOutput.getStartDateTime()<pitApply.getStartDateTime()
                        &&meetingApplyOutput.getEndDateTime()>pitApply.getStartDateTime())
                        ||(meetingApplyOutput.getStartDateTime()<pitApply.getEndDateTime()
                        &&meetingApplyOutput.getEndDateTime()>pitApply.getEndDateTime())){
                    if(id==null){
                        return ResponseResult.error("会议室在此时间段已有会议，请修改会议时间或会议室");
                    }else {
                        if(!id.equals(meetingApplyOutput.getId())){
                            if((meetingApplyOutput.getStartDateTime()<pitApply.getStartDateTime()
                                    &&meetingApplyOutput.getEndDateTime()>pitApply.getStartDateTime())
                                    ||(meetingApplyOutput.getStartDateTime()<pitApply.getEndDateTime()
                                    &&meetingApplyOutput.getEndDateTime()>pitApply.getEndDateTime())){
                                return ResponseResult.error("会议室在此时间段已有会议，请修改会议时间或会议室");
                            }
                        }
                    }

                }
            }
        }
        if(pitApply.getAttendants()!=null&&!pitApply.getAttendants().equals("")){
            String[] ids=pitApply.getAttendants().split(",");
            List<Integer> idlist=new ArrayList<Integer>();
            for(String s:ids){
                idlist.add(Integer.parseInt(s));
            }
            name=pitApplyService.namelist(idlist);
        }
        pitApply.setAttendantsName(name);
        if(id!=null){
            PitApplyOutput meetingApplyOutput=pitApplyService.getById(id);
            if(!meetingApplyOutput.getCreatedUserId().equals(getService().getUsers().getId())&&!
                    meetingApplyOutput.getEmployeesId().equals(getService().getUsers().getEmployeeId())){
                return ResponseResult.error("不是创建人或预约人不能编辑");
            }
            if(meetingApplyOutput.getState()==1){
                return ResponseResult.error("已审核的会议预约不能编辑");
            }
            pitApply.setState(meetingApplyOutput.getState());
        }else {
            pitApply.setState(0);
        }

        int applyId=0;
        var result = "";
        if(id==null){
            applyId=pitApplyService.add(pitApply);
        }else {
            applyId=pitApplyService.update(id,pitApply);
        }
        if(applyId>0){
            List<Attachment> attachmentList=pitApply.getAttachmentList();
            List<Attachment> attachments=new ArrayList<Attachment>();
            if(attachmentList!=null&&attachmentList.size()>0){
                for(Attachment attachment:attachmentList){
                    if(attachment.getFileName()==null||"".equals(attachment.getFileName())){
                        return ResponseResult.error("附件名不能为空");
                    }
                    if(attachment.getUrl()==null||"".equals(attachment.getUrl())){
                        return ResponseResult.error("附件地址不能为空");
                    }
                    if(attachment.getSuffix()==null||"".equals(attachment.getSuffix())){
                        return ResponseResult.error("附件后缀名不能为空");
                    }
                    attachment.setResourcesId(applyId);
                    attachment.setResourcesType(AttachmentEnum.PIT_TYPE.getCode());
                    attachment.setSourceFileName(attachment.getSuffix());
                    attachments.add(attachment);
                }
                PageData pageData1 = new PageData();
                pageData1.put("attachmentList",attachments);
                var resultAtt =ServiceCall.postResult(loadBalancerClient,"/attachment/attachmentApi","bigdata",pageData1);
                if(!resultAtt.isSuccess()){
                    return ResponseResult.error("上传附件失败");
                }
            }
        }else {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success(result);
    }

    @Override
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, ParseException, IllegalAccessException {
        PageData pageData=new PageData();
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }else{
            if(idList.split(",").length>1){
                return ResponseResult.error("预约只能单个删除");
            }else{
                PitApplyOutput meetingApply=pitApplyService.getById(Integer.parseInt(idList.split(",")[0]));
                if(!meetingApply.getEmployeesId().equals(getService().getUsers().getEmployeeId())){
                    return ResponseResult.error("不是预约人不能删除");
                }
                pageData.put("id",idList);
                pageData.put("approvalType","5");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Integer a=meetingApply.getStartDateTime()+8;
                String s=formatter.format(meetingApply.getAppointmentDate()).substring(0,10)+" "+a+":00:00";
                String s1=formatter.format(new Date());
                if(meetingApply.getState()==1){
                    if(formatter.parse(s).before(formatter.parse(s1))){
                        return ResponseResult.error("不能取消以前的预约");
                    }
                }
            }
        }
        return super.logicDelete(idList);
    }


    @Override
    @GetMapping(value = "get")
    public ResponseResult get(Integer id){
        if( id==null ){
            ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(pitApplyService.getById(id));
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的会议预约")
    @ApiImplicitParams({@ApiImplicitParam(name="date",value="预约日期",required=false,dataType="string", paramType = "query")})
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData=new PageData(request);
        String str=null;
        if(pageData.getMap().get("date")==null||pageData.getMap().get("date").equals("")){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            str = format.format(new Date());
            pageData.put("date",str);
        }else {
            str=pageData.getMap().get("date");
        }
        PitData meetingData=new PitData();
        List<PitOutput> roomOutputs=pitService.getByApply(pageData);
        List<PitApplyOutput> meetingApplyOutputs=pitApplyService.getByDate(pageData);
        List<PitOutput> roomOutputs1=new ArrayList<PitOutput>();
        for(PitOutput roomOutput:roomOutputs){
            List<PitApplyOutput> meetingApplyOutputs1=new ArrayList<PitApplyOutput>();
            for(PitApplyOutput meetingApplyOutput:meetingApplyOutputs){
                PitApplyOutput meetingApplyOutput1=new PitApplyOutput();
                if(meetingApplyOutput.getMeetingRoomId().equals(roomOutput.getId())){
                    meetingApplyOutput1=pitApplyService.getById(meetingApplyOutput.getId());
                    meetingApplyOutputs1.add(meetingApplyOutput1);
                }
            }
            roomOutput.setPitApplyOutputList(meetingApplyOutputs1);
            roomOutputs1.add(roomOutput);
        }
        meetingData.setDate(str);
        meetingData.setPitlist(roomOutputs1);
        return ResponseResult.success(meetingData);
    }


}
