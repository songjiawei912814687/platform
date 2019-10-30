package com.api.mapper.mybatis;

import com.api.domain.output.AttendanceStatistics;
import com.api.domain.output.StaffManagementOutput;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceStatisticsMapper {
    StaffManagementOutput selectByDate(PageData pageData);

    String selectMaxDate();
}
