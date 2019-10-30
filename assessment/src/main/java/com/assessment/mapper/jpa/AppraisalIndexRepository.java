package com.assessment.mapper.jpa;

import com.assessment.core.base.BaseMapper;
import com.assessment.model.AppraisalEmployeeRecord;
import com.assessment.model.AppraisalIndex;

import java.util.List;

public interface AppraisalIndexRepository extends BaseMapper<AppraisalIndex,Integer> {

    List<AppraisalIndex> findByNameAndAmputated(String name, Integer amputated);
}
