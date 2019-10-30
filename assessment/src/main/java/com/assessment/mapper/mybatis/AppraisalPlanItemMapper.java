package com.assessment.mapper.mybatis;

import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AppraisalPlanItemOutput;
import com.assessment.model.AppraisalPlanItem;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppraisalPlanItemMapper extends MybatisBaseMapper<AppraisalPlanItemOutput>{
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalPlanItem record);

    int insertSelective(AppraisalPlanItem record);

    @Override
    AppraisalPlanItemOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalPlanItem record);

    int updateByPrimaryKey(AppraisalPlanItem record);

    List<AppraisalPlanItemOutput> selectByPlanId(Integer planId);

    List<AppraisalPlanItemOutput> selectByIndexId(Integer indexId);

    int updateByIndexIdAndPlanId(PageData pageData);

    List<AppraisalPlanItem> findByIndexIdAndPlanId(PageData pageData);

    List<AppraisalPlanItemOutput> getPlanItemInfoAndOrgByDate(PageData pageData);

    Integer getPersonQuantity(Integer id);

    int getSumQualitityByPlanItemId(PageData pageData);

    List<AppraisalPlanItem> getAllJointRateByItemId(PageData pageData);

    List<AppraisalPlanItemOutput> getOthersRule(PageData pageData1);

    List<AppraisalPlanItemOutput> selectByYearAndMonthAndIndexId(PageData pageData);
}
