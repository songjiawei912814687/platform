package com.assessment.mapper.mybatis;

import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AppraisalAwardReportOutput;
import com.assessment.domian.output.AppraisalPlanOutput;
import com.assessment.model.AppraisalPlan;
import com.common.model.PageData;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppraisalPlanMapper extends MybatisBaseMapper<AppraisalPlanOutput>{
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalPlan record);

    int insertSelective(AppraisalPlan record);

    @Override
    AppraisalPlanOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalPlan record);

    int updateByPrimaryKey(AppraisalPlan record);

    int deleteByTypeAndTemplateIdAndYear(AppraisalPlan appraisalPlan);

    int deleteByTypeAndTemplateIdAndYearAndMonth(AppraisalPlan appraisalPlan);


    Page<AppraisalPlanOutput> selectEmployeesPlan(PageData pageData);
    //
    Page<AppraisalPlanOutput> selectEmployees(PageData pageData);


    Page<AppraisalPlanOutput> selectDeparmentPlan(PageData pageData);

    AppraisalPlanOutput selectInfoById(Integer planId);

    String selectPathById(Integer id);

    //获取这个月的员工考核计划
    List<AppraisalPlanOutput> selectEmployeePlanByDate(PageData pageData);

    List<AppraisalPlanOutput> selectEmployeePlanByYear(PageData pageData);

    Page<AppraisalPlanOutput> returnPageInfo(PageData pageData);

    List<AppraisalPlanOutput> selectEmployeesPlanExport(PageData pageData);

    AppraisalPlanOutput findByItemId(Integer id);
}
