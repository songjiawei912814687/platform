package com.screen.mapper.mybatis;

import com.common.model.PageData;
import com.github.pagehelper.Page;
import com.screen.domain.output.PerformanceIndexOutput;
import org.springframework.stereotype.Repository;

@Repository
public interface AppraisalPlanMapper {

    Page<PerformanceIndexOutput> selectPage(PageData pageData);
}
