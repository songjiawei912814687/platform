package com.assessment.mapper.jpa;

import com.assessment.core.base.BaseMapper;
import com.assessment.model.AppraisalEmployeeRecord;
import com.assessment.model.AppraisalIndex;
import com.assessment.model.AppraisalRule;

import java.util.List;

public interface AppraisalRuleRepository extends BaseMapper<AppraisalRule,Integer> {

    List<AppraisalRule> findByNameAndAmputated(String name, Integer amputated);
}
