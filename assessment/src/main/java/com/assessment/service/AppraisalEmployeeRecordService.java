package com.assessment.service;

import com.assessment.core.base.BaseMapper;
import com.assessment.core.base.BaseService;
import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AppraisalEmployeeRecordOutput;
import com.assessment.domian.output.EmployeesRecordOutput;
import com.assessment.mapper.jpa.AppraisalEmployeeRecordRepository;
import com.assessment.mapper.mybatis.AppraisalEmployeeRecordMapper;
import com.assessment.mapper.mybatis.AttachmentMapper;
import com.assessment.model.AppraisalEmployeeRecord;
import com.assessment.model.Attachment;
import com.common.model.PageData;
import com.common.utils.ExportExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
public class AppraisalEmployeeRecordService extends BaseService<AppraisalEmployeeRecordOutput, AppraisalEmployeeRecord,Integer> {

    @Autowired
    private AppraisalEmployeeRecordMapper appraisalEmployeeRecordMapper;

    @Autowired
    private AppraisalEmployeeRecordRepository repository;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Override
    public BaseMapper<AppraisalEmployeeRecord, Integer> getMapper() {
        return repository;
    }


    public MybatisBaseMapper<AppraisalEmployeeRecordOutput> getMybatisBaseMapper() {
        return appraisalEmployeeRecordMapper;
    }

    public AppraisalEmployeeRecordOutput getById(Integer id){
        AppraisalEmployeeRecordOutput meetingApplyOutput=appraisalEmployeeRecordMapper.selectByPrimaryKey(id);
        List<Attachment>  attachmentList=attachmentMapper.selectByRecordId(id);
        meetingApplyOutput.setAttachmentList(attachmentList);
        return meetingApplyOutput;
    }


    public String export(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "员工加减分记录";
        String excelName = "员工加减分记录";
        String[] rowsName = new String[]{"序号","指标名称","指标考核计分项","所属部门","人员","记录日期","得分","说明","状态"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        if(pageData.getMap().get("organizationId")!=null&&!pageData.getMap().get("organizationId").equals("")){
            String  s=pageData.getMap().get("organizationId");
            pageData.put("path",s+",");
        }
        pageData.put("userId",getUsers().getId().toString());
        if(getUsers().getAdministratorLevel()!=9){
            if(getUsers().getUserType()==0){
                pageData.put("employeeId",getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",getUsers().getOrganizationId().toString());
            }
        }
        List<AppraisalEmployeeRecordOutput> list=selectAll(false,pageData);
        if(list.size()>0){
            int i=1;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for(AppraisalEmployeeRecordOutput appraisal:list){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=appraisal.getIndexName();
                objs[2]=appraisal.getRuleName();
                objs[3]=appraisal.getOrganizationName();
                objs[4]=appraisal.getEmployeeName();
                objs[5]=formatter.format(appraisal.getRecordDateTime());
                objs[6]=appraisal.getScore();
                objs[7]=appraisal.getDescription();
                objs[8]=appraisal.getStateName();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }


     public List<EmployeesRecordOutput> getEmployeeRecord(PageData pageData){
         List<AppraisalEmployeeRecordOutput> pageList = appraisalEmployeeRecordMapper.selectByPlan(pageData);
         List<AppraisalEmployeeRecordOutput> list=pageList.stream().sorted(Comparator.comparingInt(AppraisalEmployeeRecordOutput::getRuleId)).collect(toList());
         List<EmployeesRecordOutput> employeesRecordOutputList=new ArrayList<EmployeesRecordOutput>();
         if(list!=null &list.size()>0){
             int ruleId=list.get(0).getRuleId();
             int count=0;
             BigDecimal bigDecimal=new BigDecimal("0");
             for (int i = 0; i < list.size(); i++) {
                 EmployeesRecordOutput output=new EmployeesRecordOutput();
                 AppraisalEmployeeRecordOutput record = list.get(i);
                 AppraisalEmployeeRecordOutput record1=null;
                 count++;
                 bigDecimal=bigDecimal.add(record.getScore());
                 if ((i + 1) < list.size()) {
                     record1 = list.get(i + 1);
                 }else {
                     record1 =new AppraisalEmployeeRecordOutput();
                     record1.setRuleId(-1);
                 }
                 if(!record.getRuleId().equals(record1.getRuleId())){
                     output.setCount(count);
                     count=0;
                     output.setEmployeeName(record.getEmployeeName());
                     output.setRoleName(record.getRuleName());
                     output.setScore(bigDecimal);
                     bigDecimal=new BigDecimal("0");
                     employeesRecordOutputList.add(output);
                 }
             }
         }else {
             employeesRecordOutputList=null;
         }
        return employeesRecordOutputList;
     }


    public List<AppraisalEmployeeRecordOutput> getByState(int i) {
        return  appraisalEmployeeRecordMapper.getByState(0);
    }
}
