package com.assessment.mapper.mybatis;

import com.assessment.model.AppraisalTemplateIndex;

public interface AppraisalTemplateIndexMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AppraisalTemplateIndex record);

    int insertSelective(AppraisalTemplateIndex record);

    AppraisalTemplateIndex selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AppraisalTemplateIndex record);

    int updateByPrimaryKey(AppraisalTemplateIndex record);
}