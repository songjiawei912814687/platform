package com.assessment.mapper.mybatis;

import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AppraisalComplaintOutput;
import com.assessment.domian.output.AppraisalPlanOutput;
import com.assessment.model.AppraisalComplaint;
import com.common.model.PageData;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppraisalComplaintMapper extends MybatisBaseMapper<AppraisalComplaintOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(AppraisalComplaint record);

    int insertSelective(AppraisalComplaint record);

    AppraisalComplaint selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AppraisalComplaint record);

    int updateByPrimaryKey(AppraisalComplaint record);

    String selectPathById(int id);


    @Override
    List<AppraisalComplaintOutput> selectAll(PageData t);

    List<AppraisalComplaintOutput> selectAssessmentRepresentation(PageData pageData);

    List<AppraisalComplaintOutput> selectByPlanIdAndState(PageData pageData);
}

