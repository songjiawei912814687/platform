package com.assessment.mapper.mybatis;

import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AppraisalRuleOutput;
import com.assessment.model.AppraisalRule;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppraisalRuleMapper extends MybatisBaseMapper<AppraisalRuleOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(AppraisalRule record);

    int insertSelective(AppraisalRule record);

    AppraisalRule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AppraisalRule record);

    int updateByPrimaryKey(AppraisalRule record);

    List<AppraisalRule> findByNameNotId(AppraisalRule appraisalRule);

    List<AppraisalRuleOutput> selectRuleRelationIndex(PageData pageData);

    List<AppraisalRuleOutput> selectByIndexId(PageData pageData);

    List<AppraisalRuleOutput> findRuleByIndexId(Integer indexId);
}
