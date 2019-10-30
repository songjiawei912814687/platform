package com.attendance.mapper.mybatis;

import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttendanceDailyDate;
import com.attendance.domian.output.AttendanceMonthReportOutput;
import com.attendance.model.AttendanceStatistics;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceStatisticsMapper extends MybatisBaseMapper<AttendanceStatistics>{
    int deleteByPrimaryKey(Integer id);

    int insert(AttendanceStatistics record);

    int insertSelective(AttendanceStatistics record);

    @Override
    AttendanceStatistics selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AttendanceStatistics record);

    int updateByPrimaryKey(AttendanceStatistics record);

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

    List<AttendanceStatistics> getUnusualDetail(PageData pageData);
}
