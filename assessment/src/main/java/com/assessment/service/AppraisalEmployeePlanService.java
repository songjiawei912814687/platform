package com.assessment.service;

import com.assessment.core.base.BaseMapper;
import com.assessment.core.base.BaseService;
import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AppraisalEmployeePlanOutput;
import com.assessment.domian.output.AppraisalPlanOutput;
import com.assessment.mapper.jpa.AppraisalPlanRepository;
import com.assessment.mapper.mybatis.AppraisalPlanMapper;
import com.assessment.model.AppraisalPlan;
import com.common.model.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AppraisalEmployeePlanService extends BaseService<AppraisalPlanOutput, AppraisalPlan,Integer> {
    @Autowired
    private AppraisalPlanMapper appraisalPlanMapper;
    @Autowired
    private AppraisalPlanRepository appraisalPlanRepository;
    @Override
    public BaseMapper<AppraisalPlan, Integer> getMapper() {
        return appraisalPlanRepository;
    }

    @Override
    public MybatisBaseMapper<AppraisalPlanOutput> getMybatisBaseMapper() {
        return appraisalPlanMapper;
    }

    public List<AppraisalPlanOutput> getByYear(String year){
        PageData pageData=new PageData();
        pageData.put("year",year);
        return appraisalPlanMapper.selectEmployeePlanByYear(pageData);
    }


    public List<AppraisalEmployeePlanOutput> getEmployeeRecord(String year){
        List<AppraisalPlanOutput> pageList = getByYear(year);
        List<AppraisalPlanOutput> list=pageList.stream().sorted(Comparator.comparingInt(AppraisalPlanOutput::getEmployeeId)).collect(toList());
        List<AppraisalEmployeePlanOutput> planOutputs=new ArrayList<AppraisalEmployeePlanOutput>();
        if(list!=null &list.size()>0){
            int employeeId=list.get(0).getEmployeeId();
            int count=0;
            BigDecimal bigDecimal=new BigDecimal("0");
            BigDecimal bigDecimal1=new BigDecimal("0");
            for (int i = 0; i < list.size(); i++) {
                AppraisalEmployeePlanOutput output=new AppraisalEmployeePlanOutput();
                AppraisalPlanOutput record = list.get(i);
                AppraisalPlanOutput record1=null;
                if(record.getIsEnabled()!=1){
                    count++;
                    bigDecimal=bigDecimal.add(record.getFinalScore());
                }
                if(record.getIsStar()==1){
                    bigDecimal1=bigDecimal1.add(new BigDecimal("0.2"));
                }
                if ((i + 1) < list.size()) {
                    record1 = list.get(i + 1);
                }else {
                    record1 =new AppraisalPlanOutput();
                    record1.setEmployeeId(-1);
                }
                if(!record.getEmployeeId().equals(record1.getEmployeeId())){
                    output.setScore(bigDecimal.divide(new BigDecimal(""+count)).add(bigDecimal1));
                    output.setEffectiveMonth(count);
                    bigDecimal=new BigDecimal("0");
                    bigDecimal1=new BigDecimal("0");
                    count=0;
                    output.setBankCardNumber(record.getBankCardNumber());
                    output.setName(record.getEmployeeName());
                    output.setOrganizationName(record.getOrganizationName());
                    planOutputs.add(output);
                }
            }
        }
        return planOutputs;
    }











}
