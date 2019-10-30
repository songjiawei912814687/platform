package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.input.updateStateInput;
import com.attendance.domian.output.EmployeesOutput;
import com.attendance.domian.output.HolidayOutput;
import com.attendance.domian.output.LeaveApplicationOutput;
import com.attendance.enums.AttendanceRuleEnum;
import com.attendance.mapper.jpa.LeaveApplicationRepository;
import com.attendance.mapper.mybatis.*;
import com.attendance.model.Attachment;
import com.attendance.model.LeaveApplication;
import com.attendance.model.Users;
import com.common.Enum.*;
import com.common.model.PageData;
import com.common.request.Audit;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import com.common.utils.ExportExcel;
import com.common.utils.HttpRequestUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Young
 * @description:
 * @date: Created in 22:42 2018/9/11
 * @modified by:
 */
@Service("leaveApplicationService")
@Slf4j
public class LeaveApplicationService extends BaseService<LeaveApplicationOutput, LeaveApplication,Integer> {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;
    @Autowired
    private EmployeesMapper employeesMapper;
    @Autowired
    private HolidayMapper holidayMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private AttendanceRuleNewMapper attendanceRuleNewMapper;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Override
    public BaseMapper<LeaveApplication, Integer> getMapper() {
        return leaveApplicationRepository;
    }
    @Override
    public MybatisBaseMapper<LeaveApplicationOutput> getMybatisBaseMapper() {
        return leaveApplicationMapper;
    }

    public Users getCurrentUserInfo() {
        Users users = getUsers();
        return users;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Transactional(rollbackFor = Exception.class)
    public ResponseResult formPost(Integer id,  LeaveApplication leaveApplication) throws Exception {

        //因公外出，开始时间不能大于当前时间
        if(leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.BOUT_LEAVE.getCode())){
            String startTime1 = sdf.format(leaveApplication.getStartDate()).substring(0, 10) + " " + leaveApplication.getStartTime();
            if(sdf.parse(startTime1).getTime()<System.currentTimeMillis()){
                return ResponseResult.error("开始时间必须超出当前时间");
            }
        }
        //如果是哺乳假，产假,事假,陪产假，年休假
        if (leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.BREASTFEEDING_LEAVE.getCode()) ||
                leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.MATERNITY_LEAVE.getCode()) ||
                leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.PERSONAL_LEAVE.getCode()) ||
                leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.PATERNITY_LEAVE.getCode()) ||
                leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.ANNUAL_LEAVE.getCode())) {
            //设置成循环假
            leaveApplication.setIsCycle(IsCyleEnum.IS_CYLE.getCode());
        } else {
            //非循环假
            leaveApplication.setIsCycle(IsCyleEnum.UN_CYLE.getCode());
        }
        //设置为非补录
        leaveApplication.setIsCollection(CollectionStatusEnum.UN_COLLECTION.getCode());

        Integer applyId;
        var result = "";
        leaveApplication.setIsSync(1);
        String startTime = sdf.format(leaveApplication.getStartDate()).substring(0, 10) + " " + leaveApplication.getStartTime();
        leaveApplication.setReportStartDate(sdf.parse(startTime));

        String endTime = sdf.format(leaveApplication.getEndDate()).substring(0, 10) + " " + leaveApplication.getEndTime();
        leaveApplication.setReportEndDate(sdf.parse(endTime));
        //判断如果请假
        if (sdf.parse(startTime).compareTo(sdf.parse(endTime))>=0) {
            return ResponseResult.error("请重新选择结束时间");
        }
        if (id == null) {

            List<Integer> statusList = Lists.newArrayList();
            //审核完成状态和待审核状态
            statusList.add(CheckStatusEnum.CHECK_FINISH.getCode());
            statusList.add(CheckStatusEnum.UN_CHECK.getCode());

            //根据员工id,请假开始时间和结束时间
            Integer count  = leaveApplicationMapper.selectCountByEmpAndDate(leaveApplication.getEmployeesId(),
                            leaveApplication.getStartDate(),
                            leaveApplication.getEndDate(),
                            leaveApplication.getStartTime(),
                            leaveApplication.getEndTime(),  statusList);
            //如果已经存在相同的请假申请
            if (count > 0) {
                return ResponseResult.error("请勿重复申请");
            }

            //不存在相同的请假申请则新增请假申请
            applyId = this.add(leaveApplication);

            //根据组织id查询配置的审批流规则
            Double approveRule = organizationMapper.selectApproveRule(leaveApplication.getOrganizationId());

            //计算时长
            Double duration = this.calLeaveDays(
                    leaveApplication.getStartDate(),
                    leaveApplication.getEndDate(),
                    leaveApplication.getStartTime(),
                    leaveApplication.getEndTime());

            //如果配置审批流且请假时长与规则配置的审批流时长相等
            if(approveRule!=null&&duration.equals(approveRule)){
                //就走配置level为99的审批流,审批账号为部门账号
                Boolean flag = Audit.applyApproveRule(loadBalancerClient, applyId, this.getUsers()==null?leaveApplication.getCreatedUserId():this.getUsers().getId(), ApprovalTypeEnum.LEAVE_TYPE.getCode(),1);
                if(flag){
                    result = "提交审批成功";
                }else {
                    this.deleteById(String.valueOf(applyId));
                    result = "提交审批失败";
                    return ResponseResult.error(result);
                }
            }else {
                //如果不配置审批流,走默认的审批流
                if (Audit.apply(loadBalancerClient, applyId, this.getUsers()==null?leaveApplication.getCreatedUserId():this.getUsers().getId(), ApprovalTypeEnum.LEAVE_TYPE.getCode())) {
                    result = "提交审批成功";

                } else {
                    this.deleteById(String.valueOf(applyId));
                    result = "提交审批失败";
                    return ResponseResult.error(result);
                }
            }
        } else {
            //否则进行跟新操作
            if (leaveApplication.getStatus().equals(CheckStatusEnum.CHECK_FINISH.getCode())) {
                return ResponseResult.error("审核已经完成无法更改请假信息");
            }

            //根据传入的id在数据库中查找该条请假申请
            LeaveApplication leaveApplicationCurrent = leaveApplicationRepository.findLeaveApplicationById(id);
            if (leaveApplicationCurrent == null) {
                return ResponseResult.error("未找到该条请假记录");
            }
            //如果不是本人创建无法编辑
            if(!leaveApplicationCurrent.getCreatedUserId().equals(this.getUsers().getId())){
                return ResponseResult.error("非本人创建无法编辑");
            }

            if (leaveApplicationCurrent.getStartDate().equals(leaveApplication.getStartDate()) &&
                    leaveApplicationCurrent.getStartTime().equals(leaveApplication.getStartTime()) &&
                    leaveApplicationCurrent.getEndDate().equals(leaveApplication.getEndDate()) &&
                    leaveApplicationCurrent.getEndTime().equals(leaveApplication.getEndTime())) {
                return ResponseResult.error("请勿重复申请");
            }
            applyId = this.update(id, leaveApplication);
            if (applyId == null) {
                return ResponseResult.error("更新失败");
            }
        }
        if (!CollectionUtils.isEmpty(leaveApplication.getAttachmentList())) {
            List<Attachment> attachmentList = Lists.newArrayList();
            for (Attachment attachment : leaveApplication.getAttachmentList()) {
                attachment.setResourcesId(applyId);
                attachment.setResourcesType(AttachmentEnum.LEAVE_APPLICATION_TYPE.getCode());
                attachment.setSourceFileName(attachment.getSuffix());
                attachment.setCreatedDateTime(new Date());
                attachment.setCreatedUserId(this.getUsers().getId());
                attachment.setCreatedUserName(this.getUsers().getUsername());
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
            log.info("附件保存成功");
        }
        return ResponseResult.success(result);
    }

    public Integer selectOrgId(Integer empId){
        return employeesMapper.selectOrgId(empId);
    }

    @Override
    public LeaveApplicationOutput selectById(Integer id){
        LeaveApplicationOutput leaveApplicationOutput = leaveApplicationMapper.selectByPrimaryKey(id);
        PageData pageData = new PageData();
        pageData.put("id",id.toString());
        pageData.put("type",AttachmentEnum.LEAVE_APPLICATION_TYPE.getCode().toString());
        ResponseResult call = ServiceCall.getResult(loadBalancerClient,"attachment/getAttachmentListByResouceIdAndType","BIGDATA",pageData);
        if(call.getCode()!=200){
            return leaveApplicationOutput;
        }
        List<Attachment> attachmentList =(List<Attachment>) call.getData();
        if(leaveApplicationOutput!=null){
            leaveApplicationOutput.setAttachmentList(attachmentList);
        }
        return leaveApplicationOutput;

    }

    /**
     * 计算请假天数
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 请假的天数
     */
    public double calLeaveDays(Date startDate,Date endDate,String startTime,String endTime) {
        double leaveDays = 0;
        //从startTime开始循环，若该日期不是节假日或者不是周六日则请假天数+1
        //设置循环开始日期
        Date flag = startDate;
        Calendar cal = Calendar.getInstance();

        //从数据库得到节假日的起始日期和终止日期-非工作日列表
        List<HolidayOutput> maps = null;
        try{
            maps = holidayMapper.holidayAll(startDate,endDate,1);
        }catch (Exception e){
            e.printStackTrace();
        }

        List<HolidayOutput> workMaps = null;
        try{
            //得到是工作日的列表
            workMaps = holidayMapper.holidayAll(startDate,endDate,3);
        }catch (Exception e){
            e.printStackTrace();
        }
        int week;
        double countDay;
        outer:while(flag.compareTo(endDate)!=1){
            countDay= 1;
            cal.setTime(flag);
            if(flag.compareTo(startDate)==0) {
                List<String> startRuleNames = attendanceRuleNewMapper.selectNameByValue(startTime);

                if(startRuleNames.size() == 0||startRuleNames==null){
                   countDay=0;
                }else {
                    if(AttendanceRuleEnum.AFTERNOON_GO_WORK.getMsg().contains(startRuleNames.get(0))) {
                        countDay = 0.5;
                    }
                    else {
                        countDay = 1;
                    }
                }
            }
            if(flag.compareTo(endDate)==0) {
                List<String> endRuleNames = attendanceRuleNewMapper.selectNameByValue(endTime);
                if(endRuleNames == null||endRuleNames.size() ==0){
                    countDay=0;
                }else {
                    if(AttendanceRuleEnum.MORNING_AFTER_WORK.getMsg().contains(endRuleNames.get(0))) {
                        countDay = 0.5;
                    }
                    else {
                        countDay = 1;
                    }
                }
            }
            //判断是否为周六日
            week = cal.get(Calendar.DAY_OF_WEEK) - 1;
            //0为周日，6为周六
            if(week == 0 || week == 6){
                // 如果为周六或周日时需判断节假日表中是否存在该日期加班记录
                if(workMaps.size()>0 && workMaps.get(0).getStartDate().compareTo(flag)<=0 && workMaps.get(0).getEndDate().compareTo(flag)>=0){
                    leaveDays = leaveDays + countDay;
                }
                //跳出循环进入下一个日期
                cal.add(Calendar.DAY_OF_MONTH, +1);
                flag = cal.getTime();
                continue outer;
            }else{
                //判断是否为节假日
                if(maps != null || !maps.isEmpty()){
                    for (HolidayOutput holidayOutput : maps){
                        if(flag.compareTo(holidayOutput.getStartDate())>-1&&flag.compareTo(holidayOutput.getEndDate())<1){
                            cal.add(Calendar.DAY_OF_MONTH, +1);
                            flag = cal.getTime();
                            continue outer;
                        }
                    }
                }
            }
            leaveDays = leaveDays + countDay;
            //日期往后加一天
            cal.add(Calendar.DAY_OF_MONTH, +1);
            flag = cal.getTime();
        }
        return leaveDays;
    }

    public static void main(String[] args) {
        String time1 = "11:00:00";
        String time2 = "11:30:00";
        if(time1.compareTo(time2)>0){
            System.out.println("正确");
        }else {
            System.out.println("错误");
        }

    }

    public List<LeaveApplicationOutput> getLeaveDetailByEmployeeIdAndDate(PageData pageData) {
        //根据日期和员工id查找该员工在该天或者包含该天的请假信息
        List<LeaveApplicationOutput> list = leaveApplicationMapper.findLeaveDetailByEmployeeIdAndDate(pageData);
        return list;
    }


    /**导出*/
    public String export(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "请假申请详情表";
        String excelName = "请假申请详情表";
        String[] rowsName = new String[]{"序列","组织名称","人员名称","请假类型", "请假开始时间", "请假结束时间","最后提交申请时间","申请状态","请假原因"};
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs = null;
        PageData pageData = new PageData(request);
        //如果不是管理员账户
        if(getUsers().getAdministratorLevel()!=9){
            pageData.put("userId",getUsers().getId().toString());
            if(getUsers().getUserType()==0){
                pageData.put("employeeId",getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",getUsers().getOrganizationId().toString());
            }
        }
        List<LeaveApplicationOutput> leaveApplicationOutputList =  leaveApplicationMapper.selectAll(pageData);
        if(pageData.containsKey("idList")){
            pageData = new PageData(request);
            String idList = (String) pageData.get("idList");
            StringBuilder stringBuilder = new StringBuilder(idList);

            List<String> resourceList;

            stringBuilder = stringBuilder.delete(0,1);
            stringBuilder = stringBuilder.deleteCharAt(stringBuilder.indexOf("]"));
            idList = stringBuilder.toString();
            String[]str = idList.split(",");
            resourceList = Arrays.asList(str);
            leaveApplicationOutputList = new ArrayList<>();
            for(String id :resourceList){
                LeaveApplicationOutput leaveApplicationOutput = leaveApplicationMapper.selectByPrimaryKey(Integer.parseInt(id.trim()));
                leaveApplicationOutputList.add(leaveApplicationOutput);
            }
        }
        if(leaveApplicationOutputList.size()>0){
            int i=1;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for(LeaveApplicationOutput leaveApplicationOutput:leaveApplicationOutputList){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=leaveApplicationOutput.getOrganizationName();
                objs[2]=leaveApplicationOutput.getEmployeesName();
                objs[3]=leaveApplicationOutput.getApplicationTypeName();
                objs[4]=sdf.format(leaveApplicationOutput.getStartDate())+" "+leaveApplicationOutput.getStartTime();
                objs[5]=sdf.format(leaveApplicationOutput.getEndDate())+" "+leaveApplicationOutput.getEndTime();
                objs[6]=sdf.format(leaveApplicationOutput.getLastUpdateDateTime());
                objs[7]=leaveApplicationOutput.getStatusName();
                objs[8]=leaveApplicationOutput.getDescription();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    public List<LeaveApplicationOutput>  getLeaveDetail(PageData pageData) {
        //根据日期和员工no查找该员工在该时间段审核通过的请假记录
        EmployeesOutput employeeNo = employeesMapper.selectByEmployeesNo(pageData.getString("employeeNo"));
        pageData.put("employeeId",employeeNo.getId());
        List<LeaveApplicationOutput> list = leaveApplicationMapper.getLeaveDetailByEmployeeNoAndDate(pageData);
        return list;
    }


    //微信端直接调用无需审批
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult saveLeaveWechat(Integer id, LeaveApplication leaveApplication) throws Exception {

        //因公外出，开始时间不能大于当前时间
        if(leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.BOUT_LEAVE.getCode())){
            String startTime1 = sdf.format(leaveApplication.getStartDate()).substring(0, 10) + " " + leaveApplication.getStartTime();
            if(sdf.parse(startTime1).getTime()<System.currentTimeMillis()){
                return ResponseResult.error("开始时间必须超出当前时间");
            }
        }
        //如果是哺乳假，产假,事假,陪产假，年休假
        if (leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.BREASTFEEDING_LEAVE.getCode()) ||
                leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.MATERNITY_LEAVE.getCode()) ||
                leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.PERSONAL_LEAVE.getCode()) ||
                leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.PATERNITY_LEAVE.getCode()) ||
                leaveApplication.getApplicationType().equals(LeaveApplicationStatusEnum.ANNUAL_LEAVE.getCode())) {
            //设置成循环假
            leaveApplication.setIsCycle(IsCyleEnum.IS_CYLE.getCode());
        } else {
            //非循环假
            leaveApplication.setIsCycle(IsCyleEnum.UN_CYLE.getCode());
        }
        //设置为非补录
        leaveApplication.setIsCollection(CollectionStatusEnum.UN_COLLECTION.getCode());

        Integer applyId;
        var result = "";
        //设置来源是微信端
        leaveApplication.setSource(1);
        //设置为未同步到正式库的状态
        leaveApplication.setIsSync(0);
        if (id == null) {

            List<Integer> statusList = Lists.newArrayList();
            //审核完成状态和待审核状态
            statusList.add(CheckStatusEnum.CHECK_FINISH.getCode());
            statusList.add(CheckStatusEnum.UN_CHECK.getCode());

            //根据员工id,请假开始时间和结束时间
            Integer count  = leaveApplicationMapper.selectCountByEmpAndDate(leaveApplication.getEmployeesId(),
                    leaveApplication.getStartDate(),
                    leaveApplication.getEndDate(),
                    leaveApplication.getStartTime(),
                    leaveApplication.getEndTime(),  statusList);
            //如果已经存在相同的请假申请
            if (count > 0) {
                return ResponseResult.error("请勿重复申请");
            }
            String startTime = sdf.format(leaveApplication.getStartDate()).substring(0, 10) + " " + leaveApplication.getStartTime();
            leaveApplication.setReportStartDate(sdf.parse(startTime));

            String endTime = sdf.format(leaveApplication.getEndDate()).substring(0, 10) + " " + leaveApplication.getEndTime();
            leaveApplication.setReportEndDate(sdf.parse(endTime));

            //判断如果请假
            if (sdf.parse(startTime).compareTo(sdf.parse(endTime))>=0) {
                return ResponseResult.error("请重新选择结束时间");
            }
            //不存在相同的请假申请则新增请假申请
            applyId = this.add(leaveApplication);

            //设置微信端请假的id,在通知微信端是否审批通过时使用
            leaveApplication.setId(applyId);
            leaveApplication.setLeaveWeChatId(applyId);
            leaveApplicationRepository.save(leaveApplication);
        } else {
            //否则进行跟新操作
            if (leaveApplication.getStatus().equals(CheckStatusEnum.CHECK_FINISH.getCode())) {
                return ResponseResult.error("审核已经完成无法更改请假信息");
            }

            //根据传入的id在数据库中查找该条请假申请
            LeaveApplication leaveApplicationCurrent = leaveApplicationRepository.findLeaveApplicationById(id);
            if (leaveApplicationCurrent == null) {
                return ResponseResult.error("未找到该条请假记录");
            }
            //如果不是本人创建无法编辑
            if(!leaveApplicationCurrent.getCreatedUserId().equals(this.getUsers().getId())){
                return ResponseResult.error("非本人创建无法编辑");
            }

            if (leaveApplicationCurrent.getStartDate().equals(leaveApplication.getStartDate()) &&
                    leaveApplicationCurrent.getStartTime().equals(leaveApplication.getStartTime()) &&
                    leaveApplicationCurrent.getEndDate().equals(leaveApplication.getEndDate()) &&
                    leaveApplicationCurrent.getEndTime().equals(leaveApplication.getEndTime())) {
                return ResponseResult.error("请勿重复申请");
            }
            applyId = this.update(id, leaveApplication);
            if (applyId == null) {
                return ResponseResult.error("更新失败");
            }
        }
        return ResponseResult.success(result);
    }

    /**定时每5分钟拉取微信端的请假数据并且插入到正式库*/
    @Transactional(rollbackFor = Exception.class)
    public void addLeaveWechat() throws Exception {
        //不需要查询出微信端的主键，但是一定需要拉取出LeaveWeChatId
        List<LeaveApplicationOutput> leaveWechatOutputs =  leaveApplicationMapper.addLeaveWechat();
        for(LeaveApplicationOutput leaveApplicationOutput:leaveWechatOutputs){
            String result;
            LeaveApplication leaveApplication = new LeaveApplication();
            leaveApplication.setIsSync(1);
            BeanUtils.copyProperties(leaveApplicationOutput,leaveApplication);
            Integer applyId;
            //不存在相同的请假申请则新增请假申请
            applyId = leaveApplicationRepository.save(leaveApplication).getId();

            //根据组织id查询配置的审批流规则
            Double approveRule = organizationMapper.selectApproveRule(leaveApplication.getOrganizationId());

            //计算时长
            Double duration = this.calLeaveDays(
                    leaveApplication.getStartDate(),
                    leaveApplication.getEndDate(),
                    leaveApplication.getStartTime(),
                    leaveApplication.getEndTime());

            //如果配置审批流且请假时长与规则配置的审批流时长相等
            if(approveRule!=null&&duration.equals(approveRule)){
                //就走配置level为99的审批流,审批账号为部门账号

                PageData pageData = new PageData();
                pageData.put("resourceId", applyId.toString());
                pageData.put("applicantId", leaveApplication.getCreatedUserId().toString());
                pageData.put("approvalType",  ApprovalTypeEnum.LEAVE_TYPE.getCode().toString());
                pageData.put("successivelyLevel", "1");
                ResponseResult result1 = HttpRequestUtil.
                        sendMyGetRequest("http://localhost:8770/auditNew/applyNewApproveRule?resourceId=" +applyId
                                +"&applicantId="+leaveApplication.getCreatedUserId()
                                +"&approvalType="+ApprovalTypeEnum.LEAVE_TYPE.getCode()
                                +"&successivelyLevel=1", pageData);
                if(result1.getCode()==200){
                    result = "提交审批成功";
                }else {
                    this.deleteById(String.valueOf(applyId));
                    log.info("提交审批失败");
                    return;
                }
            }else {

                PageData pageData = new PageData();
                pageData.put("resourceId", applyId.toString());
                pageData.put("applicantId", leaveApplication.getCreatedUserId().toString());
                pageData.put("approvalType",  ApprovalTypeEnum.LEAVE_TYPE.getCode().toString());
                pageData.put("successivelyLevel", "0");
                ResponseResult result1 = HttpRequestUtil.sendMyGetRequest("http://localhost:8770/auditNew/applyNew?resourceId=" +applyId
                        +"&applicantId="+leaveApplication.getCreatedUserId()
                        +"&approvalType="+ApprovalTypeEnum.LEAVE_TYPE.getCode()
                        +"&successivelyLevel=0", pageData);
                if (result1.getCode()==200) {
                    result = "提交审批成功";

                } else {
                    this.deleteById(String.valueOf(applyId));
                    log.info("提交审批失败");
                    return;
                }
            }
            log.info("result:{}",result);
            leaveApplicationMapper.updateIsSync(leaveApplication.getLeaveWeChatId());
        }
    }



    /**将审批的结果推送到微信端的请假*/
    @Transactional(rollbackFor = Exception.class)
    public void approvalResult(updateStateInput updateStateInput){
        Integer id = updateStateInput.getId();
        Integer state = updateStateInput.getState();

        LeaveApplication leaveApplication = leaveApplicationRepository.findLeaveApplicationById(id);
        if(leaveApplication.getLeaveWeChatId()!=null){
            leaveApplicationMapper.updateApprovalResult(leaveApplication.getLeaveWeChatId(), state);
        }
    }
}
