package com.api.mapper.mybatis;

import com.api.domain.output.PerformanceIndexOutput;
import com.common.model.PageData;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppraisalPlanMapper{

    Page<PerformanceIndexOutput> selectPage(PageData pageData);
}
