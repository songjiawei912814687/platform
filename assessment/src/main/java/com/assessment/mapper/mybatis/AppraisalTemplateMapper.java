package com.assessment.mapper.mybatis;

import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AppraisalRuleOutput;
import com.assessment.domian.output.AppraisalTemplateOutput;
import com.assessment.model.AppraisalTemplate;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppraisalTemplateMapper extends MybatisBaseMapper<AppraisalTemplateOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(AppraisalTemplate record);

    int insertSelective(AppraisalTemplate record);

    AppraisalTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AppraisalTemplate record);

    int updateByPrimaryKey(AppraisalTemplate record);

    List<AppraisalTemplate> findByNameNotId(AppraisalTemplate appraisalTemplate);

    List<AppraisalTemplateOutput> selectByTemplateIdAndType(PageData pageData);

    List<AppraisalTemplateOutput> selectAppraisalEmployees(PageData pageData);

    List<AppraisalTemplateOutput> selectAppraisalOrganization(PageData pageData);

    List<AppraisalTemplateOutput> selectByTemplateIdAndWindowType(PageData pageData);

    List<AppraisalTemplateOutput> selectByTemplateIdAndRoleType(PageData pageData);

    List<AppraisalTemplateOutput> selectByObjectTypeAndState(AppraisalTemplate appraisalTemplate);

    List<AppraisalTemplateOutput> selectAllAppraisalEmployees();
}
