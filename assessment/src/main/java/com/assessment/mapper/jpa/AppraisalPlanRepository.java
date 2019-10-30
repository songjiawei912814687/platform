package com.assessment.mapper.jpa;

import com.assessment.core.base.BaseMapper;
import com.assessment.model.AppraisalPlan;
import com.assessment.model.AppraisalRule;
import com.assessment.model.AssessmentDepartReport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppraisalPlanRepository extends BaseMapper<AppraisalPlan,Integer> {

    List<AppraisalRule> findByNameAndAmputated(String name, Integer amputated);

}
