package com.message.mapper.mybatis;


import com.common.model.PageData;
import com.message.domain.output.AttendanceDailyDate;
import com.message.domain.output.EmployeesOutput;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceStatisticsMapper {

    List<AttendanceDailyDate> findAttendanceDailyDate(PageData pageData);

    int selectOrganizationByName(String name);

    EmployeesOutput selectByEmployeesId(Integer employeesId);
}
