package com.assessment.mapper.mybatis;

import com.assessment.domian.output.AppraisalTemplateRuleOutput;
import com.assessment.model.AppraisalPlanItem;
import com.assessment.model.AppraisalTemplateRule;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppraisalTemplateRuleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AppraisalTemplateRule record);

    int insertSelective(AppraisalTemplateRule record);

    AppraisalTemplateRule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AppraisalTemplateRule record);

    int updateByPrimaryKey(AppraisalTemplateRule record);

    List<AppraisalPlanItem> selectByTemplateId(PageData pageData);
}
