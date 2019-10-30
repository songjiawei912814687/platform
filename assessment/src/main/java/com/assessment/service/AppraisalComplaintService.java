package com.assessment.service;

import com.assessment.core.base.BaseMapper;
import com.assessment.core.base.BaseService;
import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.core.util.AppConsts;
import com.assessment.domian.output.AppraisalComplaintOutput;
import com.assessment.domian.output.AppraisalEmployeeRecordOutput;
import com.assessment.domian.output.AppraisalPlanOutput;
import com.assessment.mapper.jpa.AppraisalComplaintRepository;
import com.assessment.mapper.jpa.AppraisalPlanRepository;
import com.assessment.mapper.mybatis.AppraisalComplaintMapper;
import com.assessment.mapper.mybatis.AppraisalPlanMapper;
import com.assessment.mapper.mybatis.AttachmentMapper;
import com.assessment.model.AppraisalComplaint;
import com.assessment.model.AppraisalPlan;
import com.assessment.model.Attachment;
import com.common.Enum.AttachmentEnum;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.ExportExcel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.assessment.core.base.BaseController.SYS_EORRO;

@Service
public class AppraisalComplaintService extends BaseService<AppraisalComplaintOutput, AppraisalComplaint,Integer> {
    @Autowired
    private AppraisalComplaintMapper appraisalComplaintMapper;

    @Autowired
    private AppraisalComplaintRepository appraisalComplaintRepository;

    @Autowired
    private AppraisalPlanMapper appraisalPlanMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Override
    public BaseMapper<AppraisalComplaint,Integer> getMapper(){
        return appraisalComplaintRepository;
    }

    public MybatisBaseMapper<AppraisalComplaintOutput> getMybatisBaseMapper(){
        return appraisalComplaintMapper;
    }

    public ResponseResult findAssessmentRepresentation(PageData pageData){
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        List<AppraisalComplaintOutput> list = appraisalComplaintMapper.selectAssessmentRepresentation(pageData);
        PageInfo pageInfo = new PageInfo(list);
        return ResponseResult.success(pageInfo);
    }


    public AppraisalComplaintOutput getById(Integer id){
        AppraisalComplaintOutput appraisalComplaintOutput=appraisalComplaintMapper.selectByPrimaryKey(id);
        PageData pageDate = new PageData();
        pageDate.put("applyId",id);
        pageDate.put("resourceType", AttachmentEnum.APPRAISAL_COMPLAIN_TYPE.getCode());
        List<Attachment>  attachmentList=attachmentMapper.selectByRecordIdAndResourceType(pageDate);
        appraisalComplaintOutput.setAttachmentList(attachmentList);
        return appraisalComplaintOutput;
    }

    public String export(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "考核申诉记录";
        String excelName = "考核申诉记录";
        String[] rowsName = new String[]{"序号","年","月","考核计划标题","模板类型","所属部门","考核人员","申诉日期","申诉原因","状态"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        pageData.put("userId",getUsers().getId().toString());
        if(getUsers().getAdministratorLevel()!=9){
            if(getUsers().getUserType()==0){
                pageData.put("employeeId",getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",getUsers().getOrganizationId().toString());
            }
        }
        List<AppraisalComplaintOutput> list=selectAll(false,pageData);
//        List<AppraisalComplaintOutput> list=getappraisalComplaint(pageData);
        if(list.size()>0){
            int i=1;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for(AppraisalComplaintOutput appraisalComplaintOutput:list){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=appraisalComplaintOutput.getYear();
                objs[2]=appraisalComplaintOutput.getMonth();
                objs[3]=appraisalComplaintOutput.getName();
                objs[4]=appraisalComplaintOutput.getTypeName();
                objs[5]=appraisalComplaintOutput.getOrganizationName();
                objs[6]=appraisalComplaintOutput.getInspectionpersonnelName();
                objs[7]=formatter.format(appraisalComplaintOutput.getCreatedDateTime());
                objs[8]=appraisalComplaintOutput.getDescription();
                objs[9]=appraisalComplaintOutput.getStateName();
                dataList.add(objs);
                i++;
            }
        }

        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }


    public int assessmentRepresentation(Integer planId, String representationReason) throws Exception{
        AppraisalPlanOutput byId = appraisalPlanMapper.selectInfoById(planId);
        if(byId==null){
            return  -1;
        }
        if(byId.getFinalScore()==null||"".equals(byId.getFinalScore())){
            return -2;
        }
        //判断该考核计划是否存在未审核的数据
        PageData pageData = new PageData();
        pageData.put("planId",planId);
        pageData.put("state", 0);
        List<AppraisalComplaintOutput> list = appraisalComplaintMapper.selectByPlanIdAndState(pageData);
        if(list!=null&&list.size()>0){
            return  -3;
        }
        AppraisalComplaint appraisalComplaint = new AppraisalComplaint();
        appraisalComplaint.setYear(byId.getYear());
        appraisalComplaint.setMonth(byId.getMonth());
        appraisalComplaint.setName(byId.getName());
        appraisalComplaint.setTemplateType(byId.getType());
        appraisalComplaint.setOrganizationId(byId.getOrganizationId());
        appraisalComplaint.setEmployeeId(byId.getEmployeeId());
        appraisalComplaint.setDescription(representationReason);
        appraisalComplaint.setPlanId(planId);
        appraisalComplaint.setState(0);
        int add = this.add(appraisalComplaint);
       return add;
    }

    public int  representationAdjustment(Integer complaintId, String representationReason) throws Exception {
        AppraisalComplaint appraisalComplaint = new AppraisalComplaint();
        appraisalComplaint.setId(complaintId);
        appraisalComplaint.setDescription(representationReason);
        int update = this.update(complaintId, appraisalComplaint);
        return update;
    }




    public String getPathById(Integer id){
        return appraisalComplaintMapper.selectPathById(id);
    }



}
