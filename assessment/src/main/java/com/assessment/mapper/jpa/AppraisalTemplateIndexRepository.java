package com.assessment.mapper.jpa;

import com.assessment.core.base.BaseMapper;
import com.assessment.model.AppraisalIndex;
import com.assessment.model.AppraisalTemplate;
import com.assessment.model.AppraisalTemplateIndex;
import com.assessment.model.AppraisalTemplateTarget;

import java.util.List;

public interface AppraisalTemplateIndexRepository extends BaseMapper<AppraisalTemplateIndex,Integer> {
    int deleteAllByTemplateId(Integer templatetId);

    List<AppraisalTemplateIndex> findByIndexId(Integer indexId);

}
