package com.assessment.mapper.mybatis;

import com.assessment.model.AppraisalTemplateTarget;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AppraisalTemplateTargetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AppraisalTemplateTarget record);

    int insertSelective(AppraisalTemplateTarget record);

    AppraisalTemplateTarget selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AppraisalTemplateTarget record);

    int updateByPrimaryKey(AppraisalTemplateTarget record);

    List<AppraisalTemplateTarget> findByTemplateId(Integer templateId);

    int deleteByTemplate(Integer templateId);
}
