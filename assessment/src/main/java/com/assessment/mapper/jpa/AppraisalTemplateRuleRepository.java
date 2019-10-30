package com.assessment.mapper.jpa;

import com.assessment.core.base.BaseMapper;
import com.assessment.model.*;

import java.util.List;

public interface AppraisalTemplateRuleRepository extends BaseMapper<AppraisalTemplateRule, Integer> {
    int deleteAllByTemplateId(Integer templatetId);

    List<AppraisalTemplateRule> findByRuleId(Integer ruleId);

}
