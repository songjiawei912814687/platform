package com.attendance.mapper.mybatis;

import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttendanceDailyDate;
import com.attendance.model.AttendanceStatistics;
import com.attendance.model.AttendanceStatisticsNew;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceStatisticsNewMapper extends MybatisBaseMapper<AttendanceDailyDate> {
    int deleteByPrimaryKey(Long id);

    int insert(AttendanceStatisticsNew record);

    int insertSelective(AttendanceStatisticsNew record);

    AttendanceStatisticsNew selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AttendanceStatisticsNew record);

    int updateByPrimaryKey(AttendanceStatisticsNew record);

    List<AttendanceDailyDate> findAttendanceDailyDate(PageData pageData);

    void deleteByDate(PageData pageData);

    //
    Integer selectSumLate(PageData pageData);
    Integer selectSumLeave(PageData pageData);
    Integer selectSumNotPunch(PageData pageData);
    Integer selectSumDays(PageData pageData);

    List<AttendanceDailyDate> findAttendanceDailyDate1(PageData pageData);

    List<AttendanceDailyDate> selectSum(PageData pageData);

    List<AttendanceStatistics> selectByEmployeeId(PageData pageData);
}