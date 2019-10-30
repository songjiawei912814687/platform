package com.attendance.mapper.mybatis;

import com.attendance.model.AttendanceSourceData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository
public interface AttendanceSourceDataMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AttendanceSourceData record);

    int insertSelective(AttendanceSourceData record);

    AttendanceSourceData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AttendanceSourceData record);

    int updateByPrimaryKey(AttendanceSourceData record);

    /**
     * 根据员工工号和时间条件以及打卡时间去考勤源数据表中去查
     * @param empNo
     * @param startTime
     * @param endTime
     * @param attendanceTime
     * @return
     */
    Integer selectByEmpNoAndDate(@Param("empNo") String empNo,
                                                    @Param("startTime") String startTime,
                                                    @Param("endTime") String endTime,
                                                    @Param("attendanceTime") String attendanceTime);

    Integer selectSourceDateByCardNoAndEventTime(@Param("cardNo") String cardNo,
                                                 @Param("eventTime") Date eventTime);
}