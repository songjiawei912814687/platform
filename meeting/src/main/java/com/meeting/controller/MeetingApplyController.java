package com.meeting.controller;

import com.common.Enum.ApprovalTypeEnum;
import com.common.Enum.AttachmentEnum;
import com.common.Enum.CheckStatusEnum;
import com.common.Enum.MeetingApplyEnum;
import com.common.model.PageData;
import com.common.request.Audit;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import com.github.pagehelper.PageInfo;
import com.meeting.core.base.BaseController;
import com.meeting.core.base.BaseService;
import com.meeting.domain.input.MeetingInput;
import com.meeting.domain.output.MeetingApplyOutput;
import com.meeting.domain.output.MeetingData;
import com.meeting.domain.output.MeetingRoomOutput;
import com.meeting.model.Attachment;
import com.meeting.model.MeetingApply;
import com.meeting.service.MeetingApplyService;
import com.meeting.service.MeetingRoomService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "/meetingapply")
public class MeetingApplyController extends BaseController<MeetingApplyOutput, MeetingApply,Integer> {
    @Autowired
    private MeetingApplyService meetingApplyService;
    @Override
    public BaseService<MeetingApplyOutput, MeetingApply, Integer> getService() {
        return meetingApplyService;
    }
    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Override
    @ApiOperation("新增或修改会议室信息")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="职务信息",value="传入json格式",required=true) MeetingApply meetingApply) throws Exception {
        String name=null;
        PageData pageData=new PageData();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(meetingApply.getAppointmentDate());
        pageData.put("date",str);
        pageData.put("meetingRoomId",meetingApply.getMeetingRoomId());
        List<MeetingApplyOutput> meetingApplyOutputList=meetingApplyService.getByRoomId(pageData);
        if(meetingApply.getIsNeedNeetwork()==null||meetingApply.getIsNeedNeetwork().equals("")){
            return ResponseResult.error("是否需要网络不能为空");
        }
        meetingApply.setIsNeedTea(1);
        if(meetingApplyOutputList.size()>0){
            for(MeetingApplyOutput meetingApplyOutput:meetingApplyOutputList){
                if(meetingApplyOutput.getId().equals(meetingApply.getId())){
                    continue;
                }
                if((meetingApplyOutput.getStartDateTime()<meetingApply.getStartDateTime()
                        &&meetingApplyOutput.getEndDateTime()>meetingApply.getStartDateTime())
                        ||(meetingApplyOutput.getStartDateTime()<meetingApply.getEndDateTime()
                        &&meetingApplyOutput.getEndDateTime()>meetingApply.getEndDateTime())){
                    if(id==null){
                        return ResponseResult.error("会议室在此时间段已有会议，请修改会议时间或会议室");
                    }else {
                        if(!id.equals(meetingApplyOutput.getId())){
                            if((meetingApplyOutput.getStartDateTime()<meetingApply.getStartDateTime()
                                    &&meetingApplyOutput.getEndDateTime()>meetingApply.getStartDateTime())
                                    ||(meetingApplyOutput.getStartDateTime()<meetingApply.getEndDateTime()
                                    &&meetingApplyOutput.getEndDateTime()>meetingApply.getEndDateTime())){
                                return ResponseResult.error("会议室在此时间段已有会议，请修改会议时间或会议室");
                            }
                        }
                    }

                }
            }
        }

        //获取预约开始时间
        String appointStartTime = MeetingApplyEnum.getDesc(meetingApply.getStartDateTime());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        Long betweenmill = sdf.parse(meetingApply.getRealStartTime()).getTime()-sdf.parse(appointStartTime).getTime();
        if(betweenmill>=60*30*1000||betweenmill<0){
            return ResponseResult.error("会议实际开始时间不能超过会议预约时间半小时");
        }

        if(meetingApply.getAttendants()!=null&&!meetingApply.getAttendants().equals("")){
            String[] ids=meetingApply.getAttendants().split(",");
            List<Integer> idlist=new ArrayList<Integer>();
            for(String s:ids){
                idlist.add(Integer.parseInt(s));
            }
            name=meetingApplyService.namelist(idlist);
        }
        if(meetingApply.getRetirees()!=null&&!meetingApply.getRetirees().equals("")){
            String[] ids=meetingApply.getRetirees().split(",");
            List<Integer> idlist=new ArrayList<Integer>();
            for(String s:ids){
                idlist.add(Integer.parseInt(s));
            }
           meetingApply.setRetireesName(meetingApplyService.getRetireesName(idlist));
        }
        if(id!=null){
            MeetingApplyOutput meetingApplyOutput=meetingApplyService.getById(id);
            if(!meetingApplyOutput.getCreatedUserId().equals(getService().getUsers().getId())&&!
                    meetingApplyOutput.getEmployeesId().equals(getService().getUsers().getEmployeeId())){
                return ResponseResult.error("不是创建人或预约人不能编辑");
            }
            if(meetingApplyOutput.getState()==1){
                return ResponseResult.error("已审核的会议预约不能编辑");
            }
            meetingApply.setState(meetingApplyOutput.getState());
        }else {
            meetingApply.setState(0);
        }
        meetingApply.setAttendantsName(name);
        int applyId=0;
        var result = "";
        if(id==null){
            applyId=meetingApplyService.add(meetingApply);
            if(!Audit.apply(loadBalancerClient,applyId,getService().getUsers().getId(), ApprovalTypeEnum.METTING_TYPE.getCode())){
                this.delete(String.valueOf(applyId));
                result = "提交审批失败";
                return ResponseResult.error(result);
            }
        }else {
            applyId=meetingApplyService.update(id,meetingApply);
        }
        if(applyId>0){
            List<Attachment> attachmentList=meetingApply.getAttachmentList();
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
                    attachment.setResourcesType(AttachmentEnum.MEETING_TYPE.getCode());
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
    @ApiOperation("删除会议室信息")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws ParseException, InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {

        PageData pageData=new PageData();
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }else{
            if(idList.split(",").length>1){
                return ResponseResult.error("会议室预约只能单个删除");
            }else{
                MeetingApplyOutput meetingApply=meetingApplyService.getById(Integer.parseInt(idList.split(",")[0]));
                if(!meetingApply.getEmployeesId().equals(getService().getUsers().getEmployeeId())){
                    return ResponseResult.error("不是预约人不能删除");
                }
                pageData.put("id",idList);
                pageData.put("approvalType","5");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Integer a=meetingApply.getStartDateTime()/2+8;
                String s=formatter.format(meetingApply.getAppointmentDate()).substring(0,10)+" "+a+":00:00";
                if(meetingApply.getStartDateTime()%2!=0){
                    s=formatter.format(meetingApply.getAppointmentDate()).substring(0,10)+" "+a+":30:00";
                }
                String s1=formatter.format(new Date());
                //不能取消已经开过或正在开的会议
                if(meetingApply.getState()==1){
                    if(formatter.parse(s).before(formatter.parse(s1))){
                        return ResponseResult.error("不能取消以前的会议");
                    }
                }
                try {
                    if(!ServiceCall.getResult(loadBalancerClient,"/auditNew/deleteByResourceId","bigdata",pageData).isSuccess()){
                        return ResponseResult.error("撤销审批失败");
                    }
                } catch (Exception e) {
                    return ResponseResult.error("撤销审批失败");
                }
                //会议如果已经审批通过然后撤销需通知网管和会务
                if(meetingApply.getState()==1){
                    meetingApplyService.sendMassage(meetingApply,"hysyycxtxyhry");
                    meetingApplyService.sendMassages(meetingApply,"hysyycxtxhw","hysyycxtxwg");
                }
            }
        }
        return super.logicDelete(idList);
    }




    @Override
    @ApiOperation("根据id获取单个会议预约")
    @GetMapping(value = "get")
    public ResponseResult get(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        if( id==null ){
            ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(meetingApplyService.getById(id));
    }


    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的会议预约")
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
        MeetingData meetingData=new MeetingData();
       List<MeetingRoomOutput> roomOutputs=meetingRoomService.getByApply(pageData);

       List<MeetingApplyOutput> meetingApplyOutputs=meetingApplyService.getByDate(pageData);
       List<MeetingRoomOutput> roomOutputs1=new ArrayList<MeetingRoomOutput>();
       for(MeetingRoomOutput roomOutput:roomOutputs){
           roomOutput.setName(roomOutput.getName()+"("+roomOutput.getContainNumber()+"人)");
           List<MeetingApplyOutput> meetingApplyOutputs1=new ArrayList<MeetingApplyOutput>();
           for(MeetingApplyOutput meetingApplyOutput:meetingApplyOutputs){
               MeetingApplyOutput meetingApplyOutput1=new MeetingApplyOutput();
               if(meetingApplyOutput.getMeetingRoomId().equals(roomOutput.getId())){
                   meetingApplyOutput1=meetingApplyService.getById(meetingApplyOutput.getId());
                   meetingApplyOutputs1.add(meetingApplyOutput1);
               }
           }
           roomOutput.setMeetingList(meetingApplyOutputs1);
           roomOutputs1.add(roomOutput);
       }
       meetingData.setDate(str);
       meetingData.setRoomList(roomOutputs1);
        return ResponseResult.success(meetingData);
    }
    @GetMapping(value = "getEmployeesList")
    public ResponseResult getEmployeesList(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        PageData pageData=new PageData(request);
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/employees/findPageList","personnel",pageData,request);
        return response;
    }

    @GetMapping(value = "getOrganList")
    public ResponseResult getOrganList(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/organization/getAll","personnel",null,request);

        return response;
    }

    @GetMapping(value = "getZtree")
    public ResponseResult getZtree(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/organization/getZtree","personnel",null,request);
        return response;
    }

    @GetMapping(value = "findMyMeetingApply")
    @ApiOperation("我的会议")
    public ResponseResult findMyMeetingApply(HttpServletRequest request){
        PageData pageData=new PageData(request);
        String str=null;
        if(pageData.getMap().get("date")==null||pageData.getMap().get("date").equals("")){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            str = format.format(new Date());
            pageData.put("date",str);
        }else {
            str=pageData.getMap().get("date");
        }
        pageData.put("employeeId",getService().getUsers().getId());
        return ResponseResult.success(new PageInfo<>(meetingApplyService.getMyMeetingApply(pageData)));
    }

    /**
     * 改变会议预约状态
     *
     * @param
     * @return
     */
    @RequestMapping(value = "updateState", method = RequestMethod.POST)
    public ResponseResult updateState(@RequestBody MeetingInput meetingInput) throws Exception {

        MeetingApply meetingApply=new MeetingApply();
        if(meetingInput.getId()==null||meetingInput.getId().equals("")||meetingInput.getState()==null||meetingInput.getState().equals("")){
            return ResponseResult.error(PARAM_EORRO);
        }
        meetingApply.setState(meetingInput.getState());
        meetingApply.setId(meetingInput.getId());

        if( meetingApplyService.update(meetingInput.getId(),meetingApply)<0){
            return ResponseResult.error("操作失败");
        }
        MeetingApplyOutput output=meetingApplyService.selectById(meetingInput.getId());
        if(meetingInput.getState()==1){
            meetingApplyService.sendMassage(output,"hysyytgtxyhry");
            meetingApplyService.sendMassageToSqr(output,"hysyysptg");
            meetingApplyService.sendMassages(output,"hysyytgtxhw","hysyytgtxwg");
        }else {
            meetingApplyService.sendMassageToSqr(output,"hysyyspbtg");
        }
        return ResponseResult.success();
    }


    @GetMapping(value = "getByUserType")
    public ResponseResult getByUserType() throws Exception {
        var user = this.getService().getUsers();
        if(user.getUserType() != 0 || user.getAdministratorLevel() == 9){
            return ResponseResult.error("请登录个人账号申请");
        }
        return ResponseResult.success();
    }

    @GetMapping(value = "getAddressBookList")
    public ResponseResult getAddressBookList(HttpServletRequest request){
        RestTemplate restTemplate = new RestTemplate();
        PageData pageData=new PageData(request);
        ResponseResult response = ServiceCall.getResult(loadBalancerClient,restTemplate,"/addressbook/findList","personnel",pageData,request);
        return response;
    }

}
