package com.assistdecision.mapper.mybatis;


import com.assistdecision.core.base.MybatisBaseMapper;
import com.assistdecision.domain.output.AttendanceStatisticsOutput;
import com.assistdecision.model.AttendanceStatistics;
import com.common.model.PageData;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AttendanceStatisticsMapper extends MybatisBaseMapper<AttendanceStatisticsOutput> {

    /**
     * 迟到次数
     * @param pageData
     * @return
     */
    Integer selectSumLate(PageData pageData);

    /**
     * 早退次数
     * @param pageData
     * @return
     */
    Integer selectEarly(PageData pageData);

    /**
     * 未打卡次数
     * @param pageData
     * @return
     */
    Integer selectSumNotPunch(PageData pageData);

    /**
     * 请假次数
     * @param pageData
     * @return
     */
    Integer selectSumLeave(PageData pageData);

    List<AttendanceStatisticsOutput> selectEarlyDetail(PageData pageData);

    List<AttendanceStatisticsOutput> selectLeaveDetail(PageData pageData);

    List<AttendanceStatisticsOutput> selectNotPunchDetail(PageData pageData);

    List<AttendanceStatisticsOutput> selectLateDetail(PageData pageData);

    List<AttendanceStatisticsOutput> workerDetail(PageData pageData);
}
