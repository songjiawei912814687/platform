package com.assessment.mapper.jpa;

import com.assessment.core.base.BaseMapper;
import com.assessment.domian.input.AppraisalTemplateInput;
import com.assessment.model.AppraisalIndex;
import com.assessment.model.AppraisalTemplate;

import java.util.List;

public interface AppraisalTemplateRepository extends BaseMapper<AppraisalTemplate,Integer> {

    List<AppraisalTemplate> findByNameAndAmputated(String name, Integer amputated);

    List<AppraisalTemplate> findByTypeAndAmputated(Integer monthly_assessment, int i);
}
