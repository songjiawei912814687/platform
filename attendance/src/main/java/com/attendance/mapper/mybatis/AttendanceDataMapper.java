package com.attendance.mapper.mybatis;

import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttendanceDailyDate;
import com.attendance.domian.output.AttendanceDataOutput;
import com.attendance.domian.output.HolidayOutput;
import com.attendance.model.AttendanceData;
import com.common.model.PageData;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceDataMapper extends MybatisBaseMapper<AttendanceDataOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(AttendanceData record);

    int insertSelective(AttendanceData record);

    @Override
    AttendanceDataOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AttendanceData record);

    int updateByPrimaryKey(AttendanceData record);

    List<AttendanceDailyDate> attendanceObjData(PageData pageData);

    //加班人员考核表  审核状态  3
    List<AttendanceDailyDate> overtimeObjData(PageData pageData);

    AttendanceData getInfoByEmployId(PageData pageData);

    //月报表
    Page<AttendanceData> selectAttendancePage(PageData pageData);
    //补录次数
    Integer selectSupplementTimes(PageData pageData);

    //工作日要考勤的人
    List<AttendanceDailyDate> attendanceDailyData(PageData pageData);

    //非工作日要考勤的人
    List<AttendanceDailyDate> overtimeDailyData(PageData pageData);

    //加班日的打卡记录
    AttendanceDailyDate selectByEmployeeId(PageData pageData);

    /**
     * 根据当天所有的打卡记录
     * @param pageData
     * @return
     */
    List<AttendanceDataOutput> selectByDate(PageData pageData);

    /**
     * 查询当月员工的加班次数
     * @param pageData
     * @return
     */
    List<AttendanceDataOutput> selectBuluCont(PageData pageData) ;
}
