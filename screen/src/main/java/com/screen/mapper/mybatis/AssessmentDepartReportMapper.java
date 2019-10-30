package com.screen.mapper.mybatis;

import com.common.model.PageData;
import com.screen.core.base.MybatisBaseMapper;
import com.screen.domain.output.AssessmentDepartReportOutput;
import com.screen.model.AssessmentDepartReport;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 根据组织的id年份和月份查询出部门该月的总人均办件量
     * @param id
     * @param year
     * @param month
     * @return
     */
    Integer selectDoingThingById(@Param("id") Integer id,
                                 @Param("year") Integer year,
                                 @Param("month") Integer month);
}
