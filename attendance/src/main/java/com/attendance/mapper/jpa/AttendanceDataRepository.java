package com.attendance.mapper.jpa;

import com.attendance.core.base.BaseMapper;
import com.attendance.model.AttendanceData;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceDataRepository extends BaseMapper<AttendanceData, Integer> {

}
