package com.assessment.mapper.mybatis;

import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AppraisalPlanOutput;
import com.assessment.model.PlanItemAddition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanItemAdditionMapper  extends MybatisBaseMapper<PlanItemAddition> {
    int deleteByPrimaryKey(Long id);

    int insert(PlanItemAddition record);

    int insertSelective(PlanItemAddition record);

    PlanItemAddition selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlanItemAddition record);

    int updateByPrimaryKey(PlanItemAddition record);

    void deleteByItemId(Integer id);

    List<PlanItemAddition> findByPlanItemId(Integer id);

    List<PlanItemAddition> findSumByPlanId(Integer planId);
}
