package com.assistdecision.mapper.mybatis;


import com.assistdecision.core.base.MybatisBaseMapper;
import com.assistdecision.domain.output.AppraisalPlanOutput;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AppraisalPlanMapper extends MybatisBaseMapper<AppraisalPlanOutput> {

    List<AppraisalPlanOutput> selectScore(PageData pageData);

    List<AppraisalPlanOutput> selectScoreByDate(PageData pageData);
}
