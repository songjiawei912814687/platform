package com.assessment.mapper.mybatis;

import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AssessmentDepartReportOutput;
import com.assessment.model.AssessmentDepartReport;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AssessmentDepartReportMapper extends MybatisBaseMapper<AssessmentDepartReportOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(AssessmentDepartReport record);

    int insertSelective(AssessmentDepartReport record);

    AssessmentDepartReportOutput selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AssessmentDepartReport record);

    int updateByPrimaryKey(AssessmentDepartReport record);

    List<AssessmentDepartReportOutput> selectDepartReportByCondition(PageData pageData);

    int deleteByTemplateIdAndDate(AssessmentDepartReport assessmentDepartReport);

    int updateByPlanId(AssessmentDepartReport assessmentDepartReport);

    List<AssessmentDepartReportOutput> selectAverageDepartReportByCondition(PageData pageData);

    AssessmentDepartReportOutput findByPlanId(Integer id);
}
